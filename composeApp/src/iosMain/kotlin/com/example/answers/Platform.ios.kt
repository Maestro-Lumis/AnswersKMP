package com.example.answers

import platform.UIKit.UIDevice
import platform.Foundation.*
import kotlinx.cinterop.ExperimentalForeignApi

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

// Файл загружается пользователем через Swift
actual fun loadCsvContent(): String = ""

// Открыть ссылку в браузере на iOS
@OptIn(ExperimentalForeignApi::class)
fun openUrlIos(url: String) {
    NSURL.URLWithString(url)?.let {
        platform.UIKit.UIApplication.sharedApplication.openURL(it)
    }
}