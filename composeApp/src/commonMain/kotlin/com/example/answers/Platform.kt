package com.example.answers

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun loadCsvContent(): String

// состояние загрузки задач
class TaskState {
    companion object {
        val repository = TaskRepository()
        val csvParser = CsvParser()
        var isLoaded = false
    }
}