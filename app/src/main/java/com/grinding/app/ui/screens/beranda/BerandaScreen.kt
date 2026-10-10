package com.grinding.app.ui.screens.beranda

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grinding.app.R
import com.grinding.app.ui.components.*
import com.grinding.app.ui.theme.*
import com.grinding.app.ui.viewmodel.TaskViewModel

@Composable
fun BerandaScreen(
    userName: String = "Rio",
    onAddTaskClick: () -> Unit = {},
    onEditTaskClick: (taskId: String) -> Unit = {},
    onStartPomodoroForTask: (taskId: String, taskTitle: String) -> Unit = { _, _ -> },
    onQuickSprintClick: () -> Unit = {},
    taskViewModel: TaskViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val taskUiState by taskViewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf(TaskFilter.ALL) }

    LaunchedEffect(Unit) {
        taskViewModel.loadTodayTasks()
    }

    val todayTasks = taskUiState.todayTasks
    val totalEst = todayTasks.sumOf { it.estimatedPomodoros }
    val totalDonePomodoros = todayTasks.sumOf { it.completedPomodoros }
    val progressPercent = if (totalEst > 0) {
        ((totalDonePomodoros.toFloat() / totalEst.toFloat()) * 100).toInt().coerceIn(0, 100)
    } else if (todayTasks.isNotEmpty() && todayTasks.all { it.isDone }) {
        100
    } else {
        0
    }
    val totalMinutes = totalDonePomodoros * 25
    val focusHours = totalMinutes / 60
    val focusMinutes = totalMinutes % 60
    val remainingSessions = (totalEst - totalDonePomodoros).coerceAtLeast(0)
    val remainingMinutes = remainingSessions * 25

    val counts = remember(todayTasks) {
        mapOf(
            TaskFilter.ALL to todayTasks.size,
            TaskFilter.RUNNING to todayTasks.count { !it.isDone && it.completedPomodoros > 0 },
            TaskFilter.TODO to todayTasks.count { !it.isDone && it.completedPomodoros == 0 },
            TaskFilter.DONE to todayTasks.count { it.isDone }
        )
    }

    val filteredTasks = remember(todayTasks, selectedFilter) {
        when (selectedFilter) {
            TaskFilter.ALL -> todayTasks
            TaskFilter.RUNNING -> todayTasks.filter { !it.isDone && it.completedPomodoros > 0 }
            TaskFilter.TODO -> todayTasks.filter { !it.isDone && it.completedPomodoros == 0 }
            TaskFilter.DONE -> todayTasks.filter { it.isDone }
        }
    }

    Scaffold(
        topBar = {
            GrindingTopBar(
                title = stringResource(R.string.nav_beranda)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTaskClick,
                containerColor = Primary,
                contentColor = OnPrimary,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.action_tambah_tugas)
                )
            }
        },
        containerColor = Background,
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Profile & Welcome Header Banner
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.home_greeting, userName),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Text(
                            text = stringResource(R.string.home_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )
                    }

                    // Streak Pill
                    Surface(
                        shape = PillShape,
                        color = SecondaryContainer.copy(alpha = 0.6f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = "🔥")
                            Text(
                                text = stringResource(R.string.home_streak_format, 1),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        }
                    }
                }
            }

            // 2. Hero Daily Goal Progress Card
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    PrimaryFixed.copy(alpha = 0.35f),
                                    SurfaceContainerLow,
                                    SecondaryContainer.copy(alpha = 0.35f)
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = stringResource(R.string.home_summary_title).uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = stringResource(R.string.home_daily_target_title),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface
                                )
                                val focusTimeText = if (focusHours > 0) {
                                    stringResource(R.string.home_focus_time_format, focusHours, focusMinutes)
                                } else {
                                    stringResource(R.string.home_focus_time_short, focusMinutes)
                                }
                                Text(
                                    text = focusTimeText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnSurfaceVariant
                                )
                            }

                            // Radial Mini Percentage Display
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryContainer.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$progressPercent%",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Primary
                                )
                            }
                        }

                        HorizontalDivider(color = OutlineVariant.copy(alpha = 0.3f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryContainer)
                                )
                                Text(
                                    text = stringResource(R.string.home_sessions_completed_format, totalDonePomodoros, totalEst),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = OnSurfaceVariant
                                )
                            }

                            Text(
                                text = stringResource(R.string.home_sessions_remaining_format, remainingSessions, remainingMinutes),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Primary
                            )
                        }
                    }
                }
            }

            // 3. Quick Action Sprint Bar
            item {
                Surface(
                    shape = PillShape,
                    color = InverseSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = PrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = stringResource(R.string.home_quick_sprint_title),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = InverseOnSurface
                                )
                                Text(
                                    text = stringResource(R.string.home_quick_sprint_subtitle),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SurfaceDim
                                )
                            }
                        }

                        Surface(
                            shape = PillShape,
                            color = PrimaryContainer,
                            modifier = Modifier
                                .clip(PillShape)
                                .clickable { onQuickSprintClick() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.action_mulai),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = OnPrimaryFixed
                                )
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = OnPrimaryFixed,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 4. Task Section Header & Filter Tabs
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.home_today_task_list),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            Surface(
                                shape = PillShape,
                                color = SurfaceContainerHigh
                            ) {
                                Text(
                                    text = "${todayTasks.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    FilterTabs(
                        selectedFilter = selectedFilter,
                        counts = counts,
                        onFilterSelect = { selectedFilter = it }
                    )
                }
            }

            // 5. Task List / Empty State
            if (filteredTasks.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SurfaceContainerLowest,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryContainer.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Assignment,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Text(
                                text = stringResource(R.string.home_empty_tasks),
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(
                    count = filteredTasks.size,
                    key = { filteredTasks[it].id ?: it }
                ) { index ->
                    val task = filteredTasks[index]
                    val catName = defaultCategories.find { it.colorKey == task.color }?.name ?: "Umum"
                    TaskCard(
                        title = task.title,
                        categoryName = catName,
                        estimatedPomodoros = task.estimatedPomodoros,
                        completedPomodoros = task.completedPomodoros,
                        isDone = task.isDone,
                        colorHex = task.color ?: "pink",
                        startTime = task.startTime ?: "Hari ini",
                        onToggleDone = { taskViewModel.toggleTaskDone(task) },
                        onStartPomodoro = { onStartPomodoroForTask(task.id ?: "", task.title) },
                        onClick = { task.id?.let { onEditTaskClick(it) } }
                    )
                }
            }

            // 6. Motivational Tip
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "${stringResource(R.string.home_tip_title)} ${stringResource(R.string.home_tip_content)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
