import Foundation
import ComposeApp

// Мост между Swift и Kotlin
// Swift вызывает onPickFile когда нужно открыть файл
// Kotlin вызывает onFileLoaded когда файл выбран
class IosFileLoader {
    static let shared = IosFileLoader()

    var onPickFile: (() -> Void)?
    var onFileLoaded: ((String) -> Void)?
}