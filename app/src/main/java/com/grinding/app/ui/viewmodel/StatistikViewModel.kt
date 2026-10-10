package com.grinding.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grinding.app.data.repository.PomodoroRepository
import com.grinding.app.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

data class DayActivity(
    val dayName: String,
    val count: Int,
    val heightRatio: Float,
    val isToday: Boolean
)

data class StatisticsUiState(
    val totalFocusHours: Int = 0,
    val totalFocusMinutes: Int = 0,
    val totalCompletedTasks: Int = 0,
    val streakDays: Int = 0,
    val weeklyProgressPercent: Int = 0,
    val weeklyCompletedSessions: Int = 0,
    val weeklyTargetSessions: Int = 20,
    val last7Days: List<DayActivity> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class StatistikViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val pomodoroRepository: PomodoroRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    fun loadStatistics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val tasksResult = taskRepository.getAllTasks()
            val sessionsResult = pomodoroRepository.getAllSessions()

            val tasks = tasksResult.getOrDefault(emptyList())
            val sessions = sessionsResult.getOrDefault(emptyList())

            val completedTasks = tasks.count { it.isDone }
            val completedSessions = sessions.count { it.status == "completed" }

            val totalMinutes = maxOf(
                completedSessions * 25,
                tasks.sumOf { it.completedPomodoros * 25 }
            )
            val focusHours = totalMinutes / 60
            val focusMins = totalMinutes % 60

            val weeklySessions = completedSessions.coerceAtLeast(tasks.sumOf { it.completedPomodoros })
            val weeklyTarget = 20
            val weeklyPercent = if (weeklyTarget > 0) {
                ((weeklySessions.toFloat() / weeklyTarget.toFloat()) * 100).toInt().coerceIn(0, 100)
            } else 0

            // 7 Days activity calculation
            val today = LocalDate.now()
            val past7DaysList = (6 downTo 0).map { dayOffset ->
                val date = today.minusDays(dayOffset.toLong())
                val dayStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val shortName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("id", "ID"))
                val count = tasks.count { it.taskDate == dayStr && (it.isDone || it.completedPomodoros > 0) }
                val isToday = dayOffset == 0
                Triple(shortName, count, isToday)
            }
            val maxCount = past7DaysList.maxOfOrNull { it.second }?.coerceAtLeast(1) ?: 1
            val dayActivities = past7DaysList.map { (name, count, isToday) ->
                DayActivity(
                    dayName = name,
                    count = count,
                    heightRatio = if (count > 0) (count.toFloat() / maxCount.toFloat()).coerceIn(0.25f, 1f) else 0.12f,
                    isToday = isToday
                )
            }

            val streak = if (tasks.any { it.isDone || it.completedPomodoros > 0 }) 1 else 0

            _uiState.update {
                it.copy(
                    totalFocusHours = focusHours,
                    totalFocusMinutes = focusMins,
                    totalCompletedTasks = completedTasks,
                    streakDays = streak,
                    weeklyProgressPercent = weeklyPercent,
                    weeklyCompletedSessions = weeklySessions,
                    weeklyTargetSessions = weeklyTarget,
                    last7Days = dayActivities,
                    isLoading = false
                )
            }
        }
    }
}
