package com.example.answers

import android.os.Build
import android.content.Context
import android.net.Uri

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

lateinit var appContext: Context

// Читаем байты
private fun ByteArray.toStringSmartEncoding(): String {
    // Сначала пробуем UTF-8
    val utf8 = this.toString(Charsets.UTF_8)

    // Если есть кракозябры, значит не UTF-8, пробуем ISO-8859-1
    return if (utf8.contains("")) {
        this.toString(Charsets.ISO_8859_1)
    } else {
        utf8
    }
}

// Читаем из assets (встроенный файл)
actual fun loadCsvContent(): String {
    return try {
        val bytes = appContext.assets.open("answers.csv").readBytes()
        bytes.toStringSmartEncoding()
    } catch (e: Exception) {
        ""
    }
}

// Читаем из файла который выбрал пользователь
fun loadCsvFromUri(uri: Uri): String {
    return try {
        val bytes = appContext.contentResolver.openInputStream(uri)?.readBytes() ?: return ""
        bytes.toStringSmartEncoding()
    } catch (e: Exception) {
        ""
    }
}