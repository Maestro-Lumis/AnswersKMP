package com.example.answers

import androidx.compose.runtime.*
import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

fun MainViewController() = ComposeUIViewController {
    var reloadTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        // загружаем задачи
        IosFileBridge.onFileLoaded = { content ->
            if (content.isNotEmpty()) {
                TaskState.repository.deleteAll()
                val tasks = TaskState.csvParser.parse(content)
                TaskState.repository.insertTasks(tasks)
                TaskState.isLoaded = true
                reloadTrigger++
            }
        }
    }

    App(
        onLoadCsv = {
            // Вызываем Swift через мост
            IosFileBridge.onPickFile?.invoke()
        },
        onOpenLink = { url ->
            NSURL.URLWithString(url)?.let {
                UIApplication.sharedApplication.openURL(it)
            }
        }
    )
}