package com.example.answers

// Хранилище задач
class TaskRepository {

    // Список всех задач в памяти
    private val tasks = mutableListOf<TaskEntity>()

    // Добавить список задач
    fun insertTasks(newTasks: List<TaskEntity>) {
        tasks.addAll(newTasks)
    }

    // Найти задачу по КИМу и номеру задачи
    fun getTask(kim: String, task: String): TaskEntity? {
        return tasks.find {
            it.kimNumber == kim && it.taskNumber == task
        }
    }

    // Удалить все задачи
    fun deleteAll() {
        tasks.clear()
    }

    // Сколько задач в хранилище
    fun getCount(): Int {
        return tasks.size
    }
}