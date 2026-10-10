package com.grinding.app.data.repository

import com.grinding.app.data.model.TaskDto
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface TaskRepository {
    suspend fun getTasksForDate(date: String): Result<List<TaskDto>>
    suspend fun getAllTasks(): Result<List<TaskDto>>
    suspend fun getTaskById(taskId: String): Result<TaskDto?>
    suspend fun createTask(task: TaskDto): Result<Unit>
    suspend fun updateTask(task: TaskDto): Result<Unit>
    suspend fun deleteTask(taskId: String): Result<Unit>
    suspend fun toggleTaskDone(taskId: String, isDone: Boolean): Result<Unit>
    suspend fun incrementCompletedPomodoro(taskId: String): Result<Unit>
}

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val authRepository: AuthRepository
) : TaskRepository {

    override suspend fun getTasksForDate(date: String): Result<List<TaskDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val userId = authRepository.getCurrentUserId() ?: return@runCatching emptyList()
            postgrest["tasks"]
                .select {
                    filter {
                        eq("task_date", date)
                        eq("user_id", userId)
                    }
                }
                .decodeList<TaskDto>()
        }
    }

    override suspend fun getAllTasks(): Result<List<TaskDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val userId = authRepository.getCurrentUserId() ?: return@runCatching emptyList()
            postgrest["tasks"]
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeList<TaskDto>()
        }
    }

    override suspend fun getTaskById(taskId: String): Result<TaskDto?> = withContext(Dispatchers.IO) {
        runCatching {
            postgrest["tasks"]
                .select {
                    filter {
                        eq("id", taskId)
                    }
                }
                .decodeSingleOrNull<TaskDto>()
        }
    }

    override suspend fun createTask(task: TaskDto): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val userId = authRepository.getCurrentUserId()
                ?: throw IllegalStateException("User belum masuk.")
            val newTask = task.copy(userId = userId)
            postgrest["tasks"].insert(newTask)
            Unit
        }
    }

    override suspend fun updateTask(task: TaskDto): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val taskId = task.id ?: throw IllegalArgumentException("Task ID tidak boleh kosong.")
            postgrest["tasks"].update(
                {
                    set("title", task.title)
                    set("estimated_pomodoros", task.estimatedPomodoros)
                    set("completed_pomodoros", task.completedPomodoros)
                    set("is_done", task.isDone)
                    set("task_date", task.taskDate)
                    set("color", task.color)
                    set("start_time", task.startTime)
                    set("end_time", task.endTime)
                }
            ) {
                filter {
                    eq("id", taskId)
                }
            }
            Unit
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            postgrest["tasks"].delete {
                filter {
                    eq("id", taskId)
                }
            }
            Unit
        }
    }

    override suspend fun toggleTaskDone(taskId: String, isDone: Boolean): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            postgrest["tasks"].update(
                {
                    set("is_done", isDone)
                }
            ) {
                filter {
                    eq("id", taskId)
                }
            }
            Unit
        }
    }

    override suspend fun incrementCompletedPomodoro(taskId: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val currentTask = getTaskById(taskId).getOrNull()
            val newCount = (currentTask?.completedPomodoros ?: 0) + 1
            postgrest["tasks"].update(
                {
                    set("completed_pomodoros", newCount)
                }
            ) {
                filter {
                    eq("id", taskId)
                }
            }
            Unit
        }
    }
}
