package com.grinding.app.ui.screens.profil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.grinding.app.ui.screens.auth.AuthViewModel
import com.grinding.app.ui.screens.statistik.StatCard
import com.grinding.app.ui.theme.*
import com.grinding.app.ui.viewmodel.StatistikViewModel

@Composable
fun ProfilScreen(
    onLogoutClick: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel(),
    statistikViewModel: StatistikViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val authUiState by authViewModel.uiState.collectAsState()
    val statUiState by statistikViewModel.uiState.collectAsState()
    var soundVibrateEnabled by remember { mutableStateOf(true) }

    val totalFocusText = if (statUiState.totalFocusHours > 0) {
        stringResource(R.string.stat_focus_hours_format, statUiState.totalFocusHours)
    } else {
        stringResource(R.string.stat_focus_minutes_format, statUiState.totalFocusMinutes)
    }

    Scaffold(
        topBar = {
            GrindingTopBar(
                title = stringResource(R.string.nav_profil)
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
            // Hero Profile Card
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = SurfaceContainerLowest,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Avatar with ring
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(SecondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Text(
                            text = authUiState.currentUserName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Text(
                            text = authUiState.currentUserEmail.ifBlank { "akun@grinding.app" },
                            style = MaterialTheme.typography.bodySmall,
                            color = Secondary
                        )

                        Surface(
                            shape = PillShape,
                            color = SecondaryContainer
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = stringResource(R.string.profile_member_badge),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSecondaryFixed
                                )
                            }
                        }
                    }
                }
            }

            // Quick Stats 3-Col (Live from Supabase)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
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
                    StatCard(
                        icon = Icons.Default.TaskAlt,
                        iconTint = Primary,
                        iconBg = SecondaryContainer,
                        value = stringResource(R.string.stat_completed_tasks_format, statUiState.totalCompletedTasks),
                        label = stringResource(R.string.profile_stat_completed_tasks),
                        cardBg = SecondaryContainer.copy(alpha = 0.5f),
                        valueColor = OnSecondaryFixed,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = Icons.Default.LocalFireDepartment,
                        iconTint = Color(0xFFB06000),
                        iconBg = Color(0xFFFEEFC3),
                        value = stringResource(R.string.profile_stat_streak_value, statUiState.streakDays),
                        label = stringResource(R.string.profile_stat_streak),
                        cardBg = Color(0xFFFEF7E0),
                        valueColor = Color(0xFF8F4E00),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Group: Pengaturan Pomodoro
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.profile_settings_title).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Secondary,
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SurfaceContainerLowest,
                        shadowElevation = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            SettingRow(
                                icon = Icons.Default.HourglassTop,
                                title = stringResource(R.string.profile_setting_focus_duration),
                                subtitle = stringResource(R.string.profile_setting_focus_sub),
                                value = stringResource(R.string.profile_setting_focus_25m)
                            )
                            HorizontalDivider(color = SurfaceContainerLow, modifier = Modifier.padding(vertical = 4.dp))
                            SettingRow(
                                icon = Icons.Default.Coffee,
                                title = stringResource(R.string.profile_setting_short_break),
                                subtitle = stringResource(R.string.profile_setting_short_break_sub),
                                value = stringResource(R.string.profile_setting_short_5m)
                            )
                            HorizontalDivider(color = SurfaceContainerLow, modifier = Modifier.padding(vertical = 4.dp))
                            SettingRow(
                                icon = Icons.Default.Hotel,
                                title = stringResource(R.string.profile_setting_long_break),
                                subtitle = stringResource(R.string.profile_setting_long_break_sub),
                                value = stringResource(R.string.profile_setting_long_15m)
                            )
                            HorizontalDivider(color = SurfaceContainerLow, modifier = Modifier.padding(vertical = 4.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(SurfaceContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                            contentDescription = null,
                                            tint = OnSurfaceVariant,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = stringResource(R.string.profile_setting_sound_vibrate),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = OnSurface
                                        )
                                        Text(
                                            text = stringResource(R.string.profile_setting_sound_sub),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Secondary
                                        )
                                    }
                                }
                                Switch(
                                    checked = soundVibrateEnabled,
                                    onCheckedChange = { soundVibrateEnabled = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = SurfaceContainerLowest, checkedTrackColor = PrimaryContainer)
                                )
                            }
                        }
                    }
                }
            }

            // Logout Button (Error Container Pill)
            item {
                Surface(
                    shape = PillShape,
                    color = ErrorContainer.copy(alpha = 0.6f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(PillShape)
                        .clickable {
                            authViewModel.signOut()
                            onLogoutClick()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 14.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = ErrorColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.auth_logout),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = ErrorColor
                        )
                    }
                }
            }

            // Version info footer
            item {
                Text(
                    text = stringResource(R.string.app_version_info),
                    style = MaterialTheme.typography.labelSmall,
                    color = Secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun SettingRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(PrimaryFixed.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = OnPrimaryContainer, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = OnSurface)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Secondary)
            }
        }

        Surface(shape = PillShape, color = SurfaceContainer) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = OnSurface,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}
