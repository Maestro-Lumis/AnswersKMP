package com.example.answers

// Kotlin мост
object IosFileBridge {

    // Функция которую вызывает Kotlin чтобы открыть файловый менеджер
    var onPickFile: (() -> Unit)? = null

    // Функция которую вызывает Swift когда файл выбран
    var onFileLoaded: ((String) -> Unit)? = null

    // Swift вызывает эту функцию передавая содержимое файла
    fun fileLoaded(content: String) {
        onFileLoaded?.invoke(content)
    }
}