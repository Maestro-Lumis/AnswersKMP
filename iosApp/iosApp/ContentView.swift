import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    @State private var showFilePicker = false

    var body: some View {
        ZStack {
            ComposeView()
                .ignoresSafeArea()
        }
        .onAppear {
            IosFileBridge.shared.onPickFile = {
                showFilePicker = true
            }
        }
        .sheet(isPresented: $showFilePicker) {
            DocumentPicker { content in
                IosFileBridge.shared.fileLoaded(content: content)
            }
        }
    }
}

// Выбор файла через стандартный iOS picker
struct DocumentPicker: UIViewControllerRepresentable {
    let onPick: (String) -> Void

    func makeUIViewController(context: Context) -> UIDocumentPickerViewController {
        let picker = UIDocumentPickerViewController(forOpeningContentTypes: [.commaSeparatedText, .text])
        picker.delegate = context.coordinator
        return picker
    }

    func updateUIViewController(_ uiViewController: UIDocumentPickerViewController, context: Context) {}

    func makeCoordinator() -> Coordinator {
        Coordinator(onPick: onPick)
    }

    class Coordinator: NSObject, UIDocumentPickerDelegate {
        let onPick: (String) -> Void
        init(onPick: @escaping (String) -> Void) { self.onPick = onPick }

        func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
            guard let url = urls.first else { return }
            do {
                // Пробуем UTF-8 потом ISO-8859-1
                let content: String
                if let utf8Content = try? String(contentsOf: url, encoding: .utf8) {
                    content = utf8Content
                } else if let latinContent = try? String(contentsOf: url, encoding: .isoLatin1) {
                    content = latinContent
                } else {
                    return
                }
                onPick(content)
            } catch {
                print("Ошибка чтения файла: \(error)")
            }
        }
    }
}