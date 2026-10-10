package com.grinding.app.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.grinding.app.service.PomodoroManager
import com.grinding.app.service.PomodoroState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val pomodoroManager: PomodoroManager
) : ViewModel() {

    val uiState: StateFlow<PomodoroState> = pomodoroManager.state

    fun setTask(taskId: String?, taskTitle: String) {
        pomodoroManager.setTask(taskId, taskTitle)
    }

    fun setPreset(minutes: Int) {
        pomodoroManager.setPresetDuration(minutes)
    }

    fun togglePlayPause(context: Context) {
        val current = uiState.value
        if (current.isRunning) {
            pomodoroManager.pauseTimer(context)
        } else if (current.isPaused) {
            pomodoroManager.resumeTimer(context)
        } else {
            pomodoroManager.startTimer(context)
        }
    }

    fun resetTimer(context: Context) {
        pomodoroManager.resetTimer(context)
    }

    fun skipTimer(context: Context) {
        pomodoroManager.skipOrComplete(context)
    }
}
