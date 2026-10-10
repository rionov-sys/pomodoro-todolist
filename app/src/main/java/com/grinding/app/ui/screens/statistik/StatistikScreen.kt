package com.grinding.app.ui.screens.statistik

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grinding.app.R
import com.grinding.app.ui.components.GrindingTopBar
import com.grinding.app.ui.theme.*
import com.grinding.app.ui.viewmodel.StatistikViewModel

@Composable
fun StatistikScreen(
    viewModel: StatistikViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStatistics()
    }

    val totalFocusText = if (uiState.totalFocusHours > 0) {
        stringResource(R.string.stat_focus_hours_format, uiState.totalFocusHours)
    } else {
        stringResource(R.string.stat_focus_minutes_format, uiState.totalFocusMinutes)
    }

    val remainingWeekly = (uiState.weeklyTargetSessions - uiState.weeklyCompletedSessions).coerceAtLeast(0)

    Scaffold(
        topBar = {
            GrindingTopBar(
                title = stringResource(R.string.nav_statistik)
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
            // 3 Column Pastel Quick Metrics
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Metric 1: Total Fokus
                    StatCard(
                        icon = Icons.Default.Schedule,
                        iconTint = Color(0xFF137333),
                        iconBg = Color(0xFFCEEAD6),
                        value = totalFocusText,
                        label = stringResource(R.string.profile_stat_total_focus),
                        cardBg = Color(0xFFE6F4EA),
                        valueColor = Color(0xFF0D652D),
                        modifier = Modifier.weight(1f)
                    )

                    // Metric 2: Tugas Tuntas
                    StatCard(
                        icon = Icons.Default.TaskAlt,
                        iconTint = Primary,
                        iconBg = SecondaryContainer,
                        value = stringResource(R.string.stat_completed_tasks_format, uiState.totalCompletedTasks),
                        label = stringResource(R.string.profile_stat_completed_tasks),
                        cardBg = SecondaryContainer.copy(alpha = 0.5f),
                        valueColor = OnSecondaryFixed,
                        modifier = Modifier.weight(1f)
                    )

                    // Metric 3: Rekor Streak
                    StatCard(
                        icon = Icons.Default.LocalFireDepartment,
                        iconTint = Color(0xFFB06000),
                        iconBg = Color(0xFFFEEFC3),
                        value = stringResource(R.string.profile_stat_streak_value, uiState.streakDays),
                        label = stringResource(R.string.profile_stat_streak),
                        cardBg = Color(0xFFFEF7E0),
                        valueColor = Color(0xFF8F4E00),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Weekly Target Progress Card
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainerLowest,
                    shadowElevation = 2.dp,
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
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryContainer.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${uiState.weeklyProgressPercent}%",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Primary
                                )
                            }
                            Column {
                                Text(
                                    text = stringResource(R.string.profile_weekly_target_title),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface
                                )
                                Text(
                                    text = stringResource(R.string.profile_weekly_target_sub, remainingWeekly, uiState.weeklyTargetSessions),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Secondary
                                )
                            }
                        }
                    }
                }
            }

            // Bar Chart Visual (7 Hari Terakhir)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceContainerLowest,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.stat_activity_7_days),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            uiState.last7Days.forEach { dayActivity ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(24.dp)
                                            .fillMaxHeight(dayActivity.heightRatio)
                                            .clip(PillShape)
                                            .background(if (dayActivity.isToday) PrimaryContainer else SurfaceContainerHigh)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = dayActivity.dayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (dayActivity.isToday) Primary else OnSurfaceVariant,
                                        fontWeight = if (dayActivity.isToday) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    iconBg: Color,
    value: String,
    label: String,
    cardBg: Color,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = cardBg,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = valueColor.copy(alpha = 0.8f)
            )
        }
    }
}
