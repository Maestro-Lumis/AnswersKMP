package com.example.answers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun App(
    reloadTrigger: Int = 0,
    onLoadCsv: () -> Unit = {},
    onOpenLink: (String) -> Unit = {}
) {
    var kimText by remember { mutableStateOf("") }
    var taskText by remember { mutableStateOf("") }
    var answerText by remember { mutableStateOf("ответ") }
    var linkText by remember { mutableStateOf("") }
    var dbCount by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    var textSize by remember { mutableStateOf(48.sp) }

    LaunchedEffect(Unit) {
        if (!TaskState.isLoaded) {
            isLoading = true
            val content = loadCsvContent()
            if (content.isNotEmpty()) {
                val tasks = TaskState.csvParser.parse(content)
                TaskState.repository.insertTasks(tasks)
                TaskState.isLoaded = true
            }
            isLoading = false
        }
        dbCount = TaskState.repository.getCount()
    }

    LaunchedEffect(reloadTrigger) {
        if (reloadTrigger > 0) {
            dbCount = TaskState.repository.getCount()
            snackbarHostState.showSnackbar("Загружено $dbCount задач")
        }
    }

    LaunchedEffect(answerText) {
        textSize = when {
            answerText.length > 50 -> 32.sp
            answerText.length > 20 -> 40.sp
            else -> 48.sp
        }
    }

    MaterialTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val sizes = getAdaptiveSizes(maxWidth.value)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0F4C75))
                        .padding(sizes.padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    // КИМ
                    Text(text = "КИМ",
                        fontSize = sizes.titleSize,
                        fontWeight = FontWeight.Bold,
                        color = Color.White)
                    Spacer(modifier = Modifier.height(sizes.verticalSpacing))
                    OutlinedTextField(
                        value = kimText,
                        onValueChange = { kimText = it },
                        modifier = Modifier.fillMaxWidth(sizes.fieldWidth).height(sizes.inputHeight),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = sizes.inputTextSize),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.weight(0.5f))

                    // Задача
                    Text(text = "Задача",
                        fontSize = sizes.titleSize,
                        fontWeight = FontWeight.Bold,
                        color = Color.White)
                    Spacer(modifier = Modifier.height(sizes.verticalSpacing))
                    OutlinedTextField(
                        value = taskText,
                        onValueChange = { taskText = it },
                        modifier = Modifier.fillMaxWidth(sizes.fieldWidth).height(sizes.inputHeight),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = sizes.inputTextSize),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.weight(0.6f))

                    // Кнопка ОТВЕТ
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            if (kimText.isNotEmpty() && taskText.isNotEmpty()) {
                                val result = TaskState.repository.getTask(kimText.trim(), taskText.trim())
                                answerText = result?.answer ?: "Не найдено"
                                linkText = result?.videoLink ?: ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(sizes.fieldWidth).height(sizes.buttonHeight),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B9D)),
                        enabled = !isLoading
                    ) {
                        Text(
                            text = if (isLoading) "Загрузка..." else "ОТВЕТ",
                            fontSize = sizes.buttonTextSize, fontWeight = FontWeight.Bold, color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.3f))

                    // Ответ с прокруткой
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(sizes.fieldWidth)
                            .weight(3f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = answerText,
                            fontSize = textSize,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = (textSize.value * 1.2f).sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        )
                    }

                    if (linkText.isNotEmpty() && linkText.startsWith("http")) {
                        Spacer(modifier = Modifier.height(sizes.verticalSpacing))
                        TextButton(onClick = { onOpenLink(linkText) }) {
                            Text(
                                text = "Открыть видео",
                                fontSize = sizes.buttonTextSize,
                                color = Color.White,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(0.3f))

                    // Счётчик и CSV
                    Text(
                        text = if (isLoading) "Загружаем..." else "Задач: $dbCount",
                        fontSize = 16.sp, color = Color.White.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onLoadCsv() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                        modifier = Modifier.height(sizes.buttonHeight)
                    ) {
                        Text(text = "Загрузить CSV", color = Color.White, fontSize = sizes.buttonTextSize)
                    }

                    Spacer(modifier = Modifier.weight(0.5f))
                }
            }
        }
    }
}