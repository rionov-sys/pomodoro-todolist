package com.grinding.app.service

import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.content.ContextCompat
import com.grinding.app.data.repository.PomodoroRepository
import com.grinding.app.data.repository.TaskRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max

data class PomodoroState(
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val targetEndTimeMs: Long = 0L,
    val remainingSeconds: Long = 25 * 60L,
    val totalDurationSeconds: Long = 25 * 60L,
    val taskId: String? = null,
    val taskTitle: String = "Sprint Kilat",
    val completedCycles: Int = 0,
    val totalCycles: Int = 4,
    val isFinished: Boolean = false
)

@Singleton
class PomodoroManager @Inject constructor(
    private val pomodoroRepository: PomodoroRepository,
    private val taskRepository: TaskRepository
) {
    private val _state = MutableStateFlow(PomodoroState())
    val state: StateFlow<PomodoroState> = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var tickerJob: Job? = null

    fun setTask(taskId: String?, taskTitle: String) {
        _state.update {
            it.copy(
                taskId = taskId,
                taskTitle = if (taskTitle.isNotBlank()) taskTitle else "Sprint Kilat"
            )
        }
    }

    fun setPresetDuration(durationMinutes: Int) {
        if (!_state.value.isRunning && !_state.value.isPaused) {
            val seconds = durationMinutes * 60L
            _state.update {
                it.copy(
                    totalDurationSeconds = seconds,
                    remainingSeconds = seconds
                )
            }
        }
    }

    fun startTimer(
        context: Context,
        taskId: String? = null,
        taskTitle: String? = null,
        durationMinutes: Int? = null
    ) {
        val current = _state.value
        val taskName = taskTitle ?: current.taskTitle
        val durSeconds = if (durationMinutes != null) durationMinutes * 60L else current.totalDurationSeconds
        val now = System.currentTimeMillis()
        val target = now + durSeconds * 1000L

        _state.update {
            it.copy(
                isRunning = true,
                isPaused = false,
                isFinished = false,
                targetEndTimeMs = target,
                remainingSeconds = durSeconds,
                totalDurationSeconds = durSeconds,
                taskId = taskId ?: it.taskId,
                taskTitle = taskName
            )
        }

        startService(context, PomodoroService.ACTION_START)
        startTicker(context)
    }

    fun pauseTimer(context: Context) {
        val current = _state.value
        if (current.isRunning) {
            val now = System.currentTimeMillis()
            // Timestamp-based calculation: calculate exact remaining seconds at pause moment
            val remaining = max(0L, (current.targetEndTimeMs - now + 999L) / 1000L)

            tickerJob?.cancel()
            _state.update {
                it.copy(
                    isRunning = false,
                    isPaused = true,
                    targetEndTimeMs = 0L,
                    remainingSeconds = remaining
                )
            }

            startService(context, PomodoroService.ACTION_PAUSE)
        }
    }

    fun resumeTimer(context: Context) {
        val current = _state.value
        if (current.isPaused && current.remainingSeconds > 0) {
            val now = System.currentTimeMillis()
            // Calculate new target end time based on preserved remaining seconds
            val target = now + current.remainingSeconds * 1000L

            _state.update {
                it.copy(
                    isRunning = true,
                    isPaused = false,
                    targetEndTimeMs = target
                )
            }

            startService(context, PomodoroService.ACTION_RESUME)
            startTicker(context)
        }
    }

    fun resetTimer(context: Context) {
        tickerJob?.cancel()
        _state.update {
            it.copy(
                isRunning = false,
                isPaused = false,
                isFinished = false,
                targetEndTimeMs = 0L,
                remainingSeconds = it.totalDurationSeconds
            )
        }

        startService(context, PomodoroService.ACTION_STOP)
    }

    fun skipOrComplete(context: Context) {
        onTimerCompleted(context)
    }

    private fun startTicker(context: Context) {
        tickerJob?.cancel()
        tickerJob = scope.launch {
            while (isActive) {
                delay(500)
                val current = _state.value
                if (current.isRunning && current.targetEndTimeMs > 0) {
                    val now = System.currentTimeMillis()
                    // Timestamp-based calculation: completely immune to dropped ticks or screen off
                    val remaining = max(0L, (current.targetEndTimeMs - now + 999L) / 1000L)

                    if (remaining <= 0) {
                        _state.update { it.copy(remainingSeconds = 0L) }
                        onTimerCompleted(context)
                        break
                    } else {
                        if (remaining != current.remainingSeconds) {
                            _state.update { it.copy(remainingSeconds = remaining) }
                        }
                    }
                }
            }
        }
    }

    private fun onTimerCompleted(context: Context) {
        tickerJob?.cancel()
        val current = _state.value

        _state.update {
            it.copy(
                isRunning = false,
                isPaused = false,
                isFinished = true,
                targetEndTimeMs = 0L,
                remainingSeconds = 0L,
                completedCycles = it.completedCycles + 1
            )
        }

        // 1. Play sound & vibration
        triggerAlarmAndVibration(context)

        // 2. Notify Foreground Service of finish
        startService(context, PomodoroService.ACTION_FINISH)

        // 3. Save session to Supabase pomodoro_sessions and increment completed_pomodoros
        scope.launch(Dispatchers.IO) {
            val taskId = current.taskId
            pomodoroRepository.saveSession(
                taskId = taskId,
                sessionType = "focus",
                status = "completed"
            )

            if (!taskId.isNullOrBlank()) {
                taskRepository.incrementCompletedPomodoro(taskId)
            }
        }
    }

    private fun triggerAlarmAndVibration(context: Context) {
        try {
            // Vibrate
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createWaveform(longArrayOf(0, 500, 250, 500, 250, 700), -1)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(
                        VibrationEffect.createWaveform(longArrayOf(0, 500, 250, 500, 250, 700), -1)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(longArrayOf(0, 500, 250, 500, 250, 700), -1)
                }
            }

            // Sound
            val notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val ringtone = RingtoneManager.getRingtone(context.applicationContext, notificationUri)
            ringtone?.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startService(context: Context, action: String) {
        val intent = Intent(context, PomodoroService::class.java).apply {
            this.action = action
        }
        try {
            ContextCompat.startForegroundService(context, intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
