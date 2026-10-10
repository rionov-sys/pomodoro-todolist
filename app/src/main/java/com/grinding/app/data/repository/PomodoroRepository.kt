package com.grinding.app.data.repository

import com.grinding.app.data.model.PomodoroSessionDto
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

interface PomodoroRepository {
    suspend fun saveSession(
        taskId: String?,
        sessionType: String = "focus",
        status: String = "completed"
    ): Result<Unit>

    suspend fun getAllSessions(): Result<List<PomodoroSessionDto>>
}

@Singleton
class PomodoroRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val authRepository: AuthRepository
) : PomodoroRepository {

    override suspend fun saveSession(
        taskId: String?,
        sessionType: String,
        status: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val userId = authRepository.getCurrentUserId()
                ?: throw IllegalStateException("Pengguna belum masuk.")
            val session = PomodoroSessionDto(
                userId = userId,
                taskId = taskId,
                startedAt = Instant.now().toString(),
                sessionType = sessionType,
                status = status
            )
            postgrest["pomodoro_sessions"].insert(session)
            Unit
        }
    }

    override suspend fun getAllSessions(): Result<List<PomodoroSessionDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val userId = authRepository.getCurrentUserId() ?: return@runCatching emptyList()
            postgrest["pomodoro_sessions"]
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeList<PomodoroSessionDto>()
        }
    }
}
