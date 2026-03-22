package com.example.answers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Колбэки для платформенных действий
// onLoadCsv - открыть файловый менеджер
// onOpenLink - открыть ссылку в браузере
@Composable
fun App(
    onLoadCsv: () -> Unit = {},
    onOpenLink: (String) -> Unit = {}
) {
    var kimText by remember { mutableStateOf("") }
    var taskText by remember { mutableStateOf("") }
    var answerText by remember { mutableStateOf("ответ") }
    var linkText by remember { mutableStateOf("") }
    var dbCount by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }

    // Загружаем CSV при первом запуске
    LaunchedEffect(Unit) {
        if (!TaskState.isLoaded) {
            isLoading = true
            val content = loadCsvContent()
            if (content.isNotEmpty()) {
                val tasks = TaskState.csvParser.parse(content)
                TaskState.repository.insertTasks(tasks)
                dbCount = TaskState.repository.getCount()
                TaskState.isLoaded = true
            }
            isLoading = false
        } else {
            dbCount = TaskState.repository.getCount()
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F4C75))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "КИМ",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = kimText,
                onValueChange = { kimText = it },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "задача",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = taskText,
                onValueChange = { taskText = it },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопка ОТВЕТ
            Button(
                onClick = {
                    if (kimText.isNotEmpty() && taskText.isNotEmpty()) {
                        val result = TaskState.repository.getTask(
                            kim = kimText.trim(),
                            task = taskText.trim()
                        )
                        answerText = result?.answer ?: "Не найдено"
                        linkText = result?.videoLink ?: ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(60.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B9D)
                ),
                enabled = !isLoading
            ) {
                Text(
                    text = if (isLoading) "Загрузка..." else "ОТВЕТ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Ответ
            Text(
                text = answerText,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            // Кнопка видео
            if (linkText.isNotEmpty() && linkText.startsWith("http")) {
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = { onOpenLink(linkText) }
                ) {
                    Text(
                        text = "Открыть видео",
                        fontSize = 20.sp,
                        color = Color.White,
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Счётчик задач
            Text(
                text = if (isLoading) "Загружаем..." else "Задач: $dbCount",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Кнопка загрузки своего CSV
            Button(
                onClick = { onLoadCsv() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.2f)
                ),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Загрузить CSV",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}