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
            var reloadTrigger by remember { mutableStateOf(0) }

            val csvLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let {
                    val content = loadCsvFromUri(it)
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
                reloadTrigger = reloadTrigger,
                onLoadCsv = { csvLauncher.launch("text/*") },
                onOpenLink = { url ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            )
        }
    }
}