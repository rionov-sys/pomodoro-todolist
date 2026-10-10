package com.grinding.app.ui.screens.timer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.grinding.app.R
import com.grinding.app.ui.components.CircularTimerRing
import com.grinding.app.ui.components.GrindingTopBar
import com.grinding.app.ui.theme.*
import com.grinding.app.ui.viewmodel.TimerViewModel

@Composable
fun TimerScreen(
    timerViewModel: TimerViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val timerState by timerViewModel.uiState.collectAsState()

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
    }

    val progress = if (timerState.totalDurationSeconds > 0) {
        (1f - (timerState.remainingSeconds.toFloat() / timerState.totalDurationSeconds.toFloat())).coerceIn(0f, 1f)
    } else 0f

    val minutes = timerState.remainingSeconds / 60
    val seconds = timerState.remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Scaffold(
        topBar = {
            GrindingTopBar(
                title = stringResource(R.string.nav_timer)
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Permission Banner (if not granted on Android 13+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SecondaryContainer.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = Primary
                                )
                                Text(
                                    text = stringResource(R.string.permission_notification_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnSurface
                                )
                            }
                            TextButton(
                                onClick = {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.permission_allow),
                                    fontWeight = FontWeight.Bold,
                                    color = Primary
                                )
                            }
                        }
                    }
                }
            }

            // Sub-header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (timerState.isRunning) PrimaryContainer else SurfaceDim)
                            )
                            Text(
                                text = stringResource(R.string.timer_active_session).uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (timerState.isRunning) Primary else OnSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = stringResource(R.string.timer_productive_target),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                    }

                    Surface(
                        shape = PillShape,
                        color = SurfaceContainerHigh
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Lo-Fi",
                                style = MaterialTheme.typography.labelMedium,
                                color = OnSurfaceVariant,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Active Task Banner Card
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
                                color = SecondaryContainer
                            ) {
                                Text(
                                    text = "Fokus Utama",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = OnSecondaryFixedVariant,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "Prioritas P1",
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant
                            )
                        }

                        Text(
                            text = timerState.taskTitle,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )

                        // Progress Bar
                        LinearProgressIndicator(
                            progress = {
                                if (timerState.totalCycles > 0) {
                                    (timerState.completedCycles.toFloat() / timerState.totalCycles.toFloat()).coerceIn(0f, 1f)
                                } else 0f
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(PillShape),
                            color = PrimaryContainer,
                            trackColor = SurfaceContainerHigh
                        )
                    }
                }
            }

            // Circular Timer Area
            item {
                CircularTimerRing(
                    progress = progress,
                    timeText = timeFormatted,
                    modeText = if (timerState.isFinished) stringResource(R.string.timer_completed_title) else stringResource(R.string.timer_mode_flow),
                    phaseSubtext = stringResource(R.string.timer_phase_format, timerState.completedCycles + 1, timerState.totalCycles),
                    completedCyclesInRound = timerState.completedCycles,
                    totalCyclesInRound = timerState.totalCycles
                )
            }

            // Duration Presets Pills
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val presets = listOf(5, 10, 25, 30)
                    presets.forEach { minutesPreset ->
                        val isCurrent = (timerState.totalDurationSeconds / 60) == minutesPreset.toLong()
                        Surface(
                            shape = PillShape,
                            color = if (isCurrent) PrimaryContainer else SurfaceContainerLowest,
                            shadowElevation = if (isCurrent) 3.dp else 1.dp,
                            modifier = Modifier
                                .weight(1f)
                                .clip(PillShape)
                                .clickable(enabled = !timerState.isRunning) {
                                    timerViewModel.setPreset(minutesPreset)
                                }
                        ) {
                            Text(
                                text = "$minutesPreset mnt",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                color = if (isCurrent) OnPrimaryFixed else OnSurfaceVariant,
                                modifier = Modifier.padding(vertical = 10.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Main Action Button (Black Sleek Pill)
            item {
                Surface(
                    shape = PillShape,
                    color = InverseSurface,
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(PillShape)
                        .clickable {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                            timerViewModel.togglePlayPause(context)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (timerState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = InverseOnSurface,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (timerState.isRunning) stringResource(R.string.action_jeda) else stringResource(R.string.action_mulai),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = InverseOnSurface
                        )
                    }
                }
            }

            // Secondary Controls (Reset & Selesai)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { timerViewModel.resetTimer(context) },
                        shape = PillShape,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OnSurfaceVariant),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = stringResource(R.string.action_reset), style = MaterialTheme.typography.labelMedium)
                    }

                    OutlinedButton(
                        onClick = { timerViewModel.skipTimer(context) },
                        shape = PillShape,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = stringResource(R.string.action_selesai), style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}
