package com.example.answers

// Парсер CSV файлов
class CsvParser {

    // Принимает текст CSV и возвращает список задач
    fun parse(csvContent: String): List<TaskEntity> {
        val tasks = mutableListOf<TaskEntity>()
        val lines = csvContent.lines()

        if (lines.isEmpty()) return tasks

        // Первая строка — заголовки (номера КИМов)
        val headers = lines[0].split(",")
        val kimNumbers = headers.drop(1)

        // Остальные строки(задачи)
        for (i in 1 until lines.size) {
            val cells = lines[i].split(",")
            if (cells.isEmpty()) continue

            val taskNumber = cells[0].trim()
            if (taskNumber.isEmpty()) continue

            // Для каждого КИМа берём ответ
            for (j in 1 until cells.size) {
                if (j - 1 >= kimNumbers.size) break

                val kimNumber = kimNumbers[j - 1].trim()
                val answer = cells[j].trim()

                if (answer.isNotEmpty() && kimNumber.isNotEmpty()) {
                    tasks.add(
                        TaskEntity(
                            taskNumber = taskNumber,
                            kimNumber = kimNumber,
                            answer = answer
                        )
                    )
                }
            }
        }

        return tasks
    }
}