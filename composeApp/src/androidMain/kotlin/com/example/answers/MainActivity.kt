package com.example.answers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        appContext = applicationContext

        setContent {
            // Лаунчер для выбора CSV файла
            val csvLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let {
                    // Читаем файл и загружаем задачи
                    val content = loadCsvFromUri(it)
                    if (content.isNotEmpty()) {
                        TaskState.repository.deleteAll()
                        val tasks = TaskState.csvParser.parse(content)
                        TaskState.repository.insertTasks(tasks)
                        TaskState.isLoaded = true
                    }
                }
            }

            App(
                // Открываем файловый менеджер
                onLoadCsv = {
                    csvLauncher.launch("text/*")
                },
                // Открываем ссылку в браузере
                onOpenLink = { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }
            )
        }
    }
}