package com.grinding.app.ui.screens.kalender

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grinding.app.R
import com.grinding.app.ui.components.GrindingTopBar
import com.grinding.app.ui.components.defaultCategories
import com.grinding.app.ui.theme.*
import com.grinding.app.ui.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun KalenderScreen(
    onTaskClick: (String) -> Unit = {},
    taskViewModel: TaskViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val taskUiState by taskViewModel.uiState.collectAsState()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    LaunchedEffect(selectedDate) {
        taskViewModel.loadCalendarTasks(selectedDate)
    }

    val calendarTasks = taskUiState.calendarTasks
    val totalTasks = calendarTasks.size
    val totalFocusMinutes = calendarTasks.sumOf { it.completedPomodoros * 25 }
    val focusHours = totalFocusMinutes / 60
    val focusMinutes = totalFocusMinutes % 60
    val totalEst = calendarTasks.sumOf { it.estimatedPomodoros }
    val progressPercent = if (totalEst > 0) {
        ((calendarTasks.sumOf { it.completedPomodoros }.toFloat() / totalEst.toFloat()) * 100).toInt().coerceIn(0, 100)
    } else if (calendarTasks.isNotEmpty() && calendarTasks.all { it.isDone }) {
        100
    } else {
        0
    }

    Scaffold(
        topBar = {
            GrindingTopBar(
                title = stringResource(R.string.nav_kalender)
            )
        },
        containerColor = Background,
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Month Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.cal_header_subtitle).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                        val monthYearFormat = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("id", "ID"))
                        Text(
                            text = selectedDate.format(monthYearFormat),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                    }

                    // Month Chevrons
                    Row(
                        modifier = Modifier
                            .clip(PillShape)
                            .background(SurfaceContainerHigh)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = { selectedDate = selectedDate.minusMonths(1) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = stringResource(R.string.cal_month_prev),
                                tint = OnSurfaceVariant
                            )
                        }
                        IconButton(
                            onClick = { selectedDate = selectedDate.plusMonths(1) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = stringResource(R.string.cal_month_next),
                                tint = OnSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Horizontal Date Strip (Pastel & Teal Pill)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Generate 14 days centered around selectedDate
                    val start = selectedDate.minusDays(3)
                    for (i in 0..13) {
                        val date = start.plusDays(i.toLong())
                        val isSelected = date == selectedDate

                        val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("id", "ID"))
                        val dayNumber = date.dayOfMonth.toString()

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) PrimaryContainer else SurfaceContainerLowest,
                            shadowElevation = if (isSelected) 4.dp else 0.dp,
                            modifier = Modifier
                                .width(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { selectedDate = date }
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = dayName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) OnPrimaryFixed else OnSurfaceVariant,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                                Text(
                                    text = dayNumber,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = if (isSelected) OnPrimaryFixed else OnSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) SurfaceContainerLowest else SecondaryFixedDim)
                                )
                            }
                        }
                    }
                }
            }

            // Daily Focus Summary Banner
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Primary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column {
                                val dayFormat = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale("id", "ID"))
                                Text(
                                    text = selectedDate.format(dayFormat),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurface
                                )
                                val focusSummaryText = if (focusHours > 0) {
                                    stringResource(R.string.cal_focus_hours_format, totalTasks, focusHours, focusMinutes)
                                } else {
                                    stringResource(R.string.cal_focus_minutes_format, totalTasks, focusMinutes)
                                }
                                Text(
                                    text = focusSummaryText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            shape = PillShape,
                            color = SurfaceContainerLowest
                        ) {
                            Text(
                                text = stringResource(R.string.target_percent_format, progressPercent),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Primary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Timeline Items
            if (calendarTasks.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SurfaceContainerLowest,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
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
                                    imageVector = Icons.Default.EventNote,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Text(
                                text = stringResource(R.string.cal_empty_timeline),
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(
                    count = calendarTasks.size,
                    key = { calendarTasks[it].id ?: it }
                ) { index ->
                    val task = calendarTasks[index]
                    val category = defaultCategories.find { it.colorKey == task.color }?.name ?: "Umum"
                    val containerColor = when (task.color) {
                        "pink" -> PastelPinkContainer
                        "lavender" -> PastelLavenderContainer
                        "mint" -> PastelMintContainer
                        "amber" -> PastelAmberContainer
                        "sky" -> PastelSkyContainer
                        else -> SurfaceContainerLow
                    }
                    val timeLabel = if (!task.startTime.isNullOrBlank() && !task.endTime.isNullOrBlank()) {
                        "${task.startTime} - ${task.endTime}"
                    } else if (!task.startTime.isNullOrBlank()) {
                        task.startTime!!
                    } else {
                        "Fleksibel"
                    }
                    val durationMinutes = task.estimatedPomodoros * 25

                    TimelineItemCard(
                        time = timeLabel,
                        title = task.title,
                        category = category,
                        durationMinutes = durationMinutes,
                        completedSessions = task.completedPomodoros,
                        totalSessions = task.estimatedPomodoros,
                        containerColor = containerColor,
                        onClick = { task.id?.let { onTaskClick(it) } }
                    )
                }
            }
        }
    }
}

@Composable
fun TimelineItemCard(
    time: String,
    title: String,
    category: String,
    durationMinutes: Int,
    completedSessions: Int,
    totalSessions: Int,
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = PillShape,
                    color = SurfaceContainerLowest.copy(alpha = 0.85f)
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "$durationMinutes mnt",
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceVariant
                    )
                }
            }

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "🍅 $completedSessions/$totalSessions Sesi",
                    style = MaterialTheme.typography.labelSmall,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
