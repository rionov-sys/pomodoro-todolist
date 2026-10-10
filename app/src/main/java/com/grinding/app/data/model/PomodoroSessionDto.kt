package com.grinding.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PomodoroSessionDto(
    val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("task_id") val taskId: String? = null,
    @SerialName("started_at") val startedAt: String? = null,
    @SerialName("session_type") val sessionType: String = "focus",
    val status: String = "completed"
)
