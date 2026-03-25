package com.example.answers

class CsvParser {

    fun parse(csvContent: String): List<TaskEntity> {
        val tasks = mutableListOf<TaskEntity>()
        val lines = csvContent.lines().toMutableList()
        var lineIndex = 0

        try {
            // Первая строка - заголовки
            val headerResult = readRecord(lines, lineIndex)
            val headerCells = headerResult.first
            lineIndex = headerResult.second

            val kimNumbers = headerCells.drop(1).map {
                it.split("\n").first().trim()
            }

            // Остальные строки - задачи
            while (lineIndex < lines.size) {
                val result = readRecord(lines, lineIndex)
                val cells = result.first
                lineIndex = result.second

                if (cells.isEmpty()) continue
                val taskNumber = cells[0].split("\n").first().trim()
                if (taskNumber.isEmpty()) continue

                for (i in 1 until cells.size) {
                    if (i - 1 >= kimNumbers.size) break

                    val kimNumber = kimNumbers[i - 1]
                    val cellContent = cells[i].trim()
                    if (cellContent.isEmpty() || kimNumber.isEmpty()) continue

                    val cellLines = cellContent.split("\n").map { it.trim() }

                    val answerLines = cellLines.filter {
                        it.isNotEmpty() &&
                                !it.contains("http://", ignoreCase = true) &&
                                !it.contains("https://", ignoreCase = true) &&
                                !it.contains("youtu.be", ignoreCase = true) &&
                                !it.contains("vk.com", ignoreCase = true)
                    }
                    val answer = answerLines.joinToString("\n").trim().trimEnd('"')

                    val link = cellLines.firstOrNull {
                        it.startsWith("http://", ignoreCase = true) ||
                                it.startsWith("https://", ignoreCase = true)
                    }?.trimEnd('"') ?: ""

                    if (answer.isNotEmpty()) {
                        tasks.add(TaskEntity(
                            taskNumber = taskNumber,
                            kimNumber = kimNumber,
                            answer = answer,
                            videoLink = link
                        ))
                    }
                }
            }
        } catch (e: Exception) {
            println("Ошибка парсинга: ${e.message}")
        }

        return tasks
    }

    // Читает одну запись CSV возвращаем список ячеек и следующий индекс строки
    private fun readRecord(lines: List<String>, startIndex: Int): Pair<List<String>, Int> {
        val cells = mutableListOf<String>()
        val currentCell = StringBuilder()
        var inQuotes = false
        var lineIndex = startIndex

        while (lineIndex < lines.size) {
            val line = lines[lineIndex]
            lineIndex++

            for (char in line) {
                when {
                    char == '"' -> inQuotes = !inQuotes
                    char == ',' && !inQuotes -> {
                        cells.add(currentCell.toString())
                        currentCell.clear()
                    }
                    else -> currentCell.append(char)
                }
            }

            if (inQuotes) {
                // Многострочная ячейка
                currentCell.append('\n')
            } else {
                // Строка закончена
                cells.add(currentCell.toString())
                return Pair(cells, lineIndex)
            }
        }

        cells.add(currentCell.toString())
        return Pair(cells, lineIndex)
    }
}