package com.grinding.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    val title: String,
    @SerialName("estimated_pomodoros") val estimatedPomodoros: Int = 1,
    @SerialName("completed_pomodoros") val completedPomodoros: Int = 0,
    @SerialName("is_done") val isDone: Boolean = false,
    @SerialName("task_date") val taskDate: String,
    val color: String = "lavender",
    @SerialName("start_time") val startTime: String? = null,
    @SerialName("end_time") val endTime: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)
