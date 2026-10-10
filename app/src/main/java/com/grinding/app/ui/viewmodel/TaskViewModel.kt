package com.grinding.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grinding.app.data.model.TaskDto
import com.grinding.app.data.repository.TaskRepository
import com.grinding.app.ui.components.TaskFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class TaskUiState(
    val todayTasks: List<TaskDto> = emptyList(),
    val calendarTasks: List<TaskDto> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedFilter: TaskFilter = TaskFilter.ALL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    init {
        loadTodayTasks()
        loadCalendarTasks(LocalDate.now())
    }

    fun loadTodayTasks() {
        val todayStr = LocalDate.now().format(dateFormatter)
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = taskRepository.getTasksForDate(todayStr)
            result.onSuccess { tasks ->
                _uiState.update { it.copy(todayTasks = tasks, isLoading = false) }
            }.onFailure { err ->
                _uiState.update { it.copy(isLoading = false, errorMessage = err.localizedMessage) }
            }
        }
    }

    fun loadCalendarTasks(date: LocalDate) {
        val dateStr = date.format(dateFormatter)
        viewModelScope.launch {
            val result = taskRepository.getTasksForDate(dateStr)
            result.onSuccess { tasks ->
                _uiState.update { it.copy(calendarTasks = tasks, selectedDate = date) }
            }
        }
    }

    fun setFilter(filter: TaskFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun toggleTaskDone(task: TaskDto) {
        val taskId = task.id ?: return
        val newDone = !task.isDone
        viewModelScope.launch {
            // Optimistic update
            _uiState.update { state ->
                state.copy(
                    todayTasks = state.todayTasks.map { if (it.id == taskId) it.copy(isDone = newDone) else it },
                    calendarTasks = state.calendarTasks.map { if (it.id == taskId) it.copy(isDone = newDone) else it }
                )
            }
            val res = taskRepository.toggleTaskDone(taskId, newDone)
            if (res.isFailure) {
                // Revert
                loadTodayTasks()
                loadCalendarTasks(_uiState.value.selectedDate)
            }
        }
    }

    fun createTask(
        title: String,
        estimatedPomodoros: Int,
        taskDate: LocalDate,
        color: String,
        startTime: String? = null,
        endTime: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val newTask = TaskDto(
                title = title,
                estimatedPomodoros = estimatedPomodoros,
                completedPomodoros = 0,
                isDone = false,
                taskDate = taskDate.format(dateFormatter),
                color = color,
                startTime = startTime,
                endTime = endTime
            )
            val res = taskRepository.createTask(newTask)
            _uiState.update { it.copy(isLoading = false) }
            res.onSuccess {
                loadTodayTasks()
                loadCalendarTasks(_uiState.value.selectedDate)
                onSuccess()
            }.onFailure { err ->
                _uiState.update { it.copy(errorMessage = err.localizedMessage) }
            }
        }
    }

    fun updateTask(
        task: TaskDto,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val res = taskRepository.updateTask(task)
            _uiState.update { it.copy(isLoading = false) }
            res.onSuccess {
                loadTodayTasks()
                loadCalendarTasks(_uiState.value.selectedDate)
                onSuccess()
            }.onFailure { err ->
                _uiState.update { it.copy(errorMessage = err.localizedMessage) }
            }
        }
    }

    fun deleteTask(
        taskId: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val res = taskRepository.deleteTask(taskId)
            _uiState.update { it.copy(isLoading = false) }
            res.onSuccess {
                loadTodayTasks()
                loadCalendarTasks(_uiState.value.selectedDate)
                onSuccess()
            }.onFailure { err ->
                _uiState.update { it.copy(errorMessage = err.localizedMessage) }
            }
        }
    }

    fun getTaskById(taskId: String): TaskDto? {
        return _uiState.value.todayTasks.find { it.id == taskId }
            ?: _uiState.value.calendarTasks.find { it.id == taskId }
    }
}
