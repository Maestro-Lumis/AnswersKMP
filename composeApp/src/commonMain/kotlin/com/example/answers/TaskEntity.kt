package com.example.answers

// Это модель одной записи (КИМ + задача + ответ + ссылка)
data class TaskEntity(
    val id: Int = 0,
    val taskNumber: String,    // номер задачи: "1", "2", "3"
    val kimNumber: String,     // номер КИМа: "4", "5", "6"
    val answer: String,        // ответ на задачу
    val videoLink: String = "" // ссылка на видео
)