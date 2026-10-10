package com.grinding.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.grinding.app.R
import com.grinding.app.ui.theme.*

fun parseTaskCardColor(colorHex: String?): Color {
    return when (colorHex) {
        "pink", "#FFE4E6" -> PastelPinkContainer
        "lavender", "#EDE9FE" -> PastelLavenderContainer
        "mint", "#DCFCE7" -> PastelMintContainer
        "amber", "#FEF3C7" -> PastelAmberContainer
        "sky", "#E0F2FE" -> PastelSkyContainer
        else -> TertiaryFixed.copy(alpha = 0.5f)
    }
}

fun parseCategoryBadgeColor(colorHex: String?): Pair<Color, Color> {
    return when (colorHex) {
        "pink", "#FFE4E6" -> Pair(PastelPinkOnContainer, PastelPinkDot)
        "lavender", "#EDE9FE" -> Pair(PastelLavenderOnContainer, PastelLavenderDot)
        "mint", "#DCFCE7" -> Pair(PastelMintOnContainer, PastelMintDot)
        "amber", "#FEF3C7" -> Pair(PastelAmberOnContainer, PastelAmberDot)
        "sky", "#E0F2FE" -> Pair(PastelSkyOnContainer, PastelSkyDot)
        else -> Pair(OnTertiaryFixed, Primary)
    }
}

@Composable
fun TaskCard(
    title: String,
    categoryName: String,
    estimatedPomodoros: Int,
    completedPomodoros: Int,
    isDone: Boolean,
    colorHex: String?,
    startTime: String? = null,
    onToggleDone: () -> Unit,
    onStartPomodoro: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBackground = parseTaskCardColor(colorHex)
    val (catTextColor, catDotColor) = parseCategoryBadgeColor(colorHex)
    val cardAlpha by animateFloatAsState(targetValue = if (isDone) 0.65f else 1f, label = "cardAlpha")

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = cardBackground,
        modifier = modifier
            .fillMaxWidth()
            .alpha(cardAlpha)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Interactive Checkbox Button
            val checkmarkBg by animateColorAsState(
                targetValue = if (isDone) Primary else SurfaceContainerLowest,
                label = "checkBg"
            )
            val checkmarkTint by animateColorAsState(
                targetValue = if (isDone) OnPrimary else Color.Transparent,
                label = "checkTint"
            )

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(checkmarkBg)
                    .clickable { onToggleDone() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = checkmarkTint,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Task Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Category Tag & Status Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = PillShape,
                        color = SurfaceContainerLowest.copy(alpha = 0.85f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(catDotColor)
                            )
                            Text(
                                text = categoryName,
                                style = MaterialTheme.typography.labelSmall,
                                color = catTextColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (completedPomodoros > 0 && !isDone) {
                        Surface(
                            shape = PillShape,
                            color = PrimaryContainer
                        ) {
                            Text(
                                text = stringResource(R.string.task_status_focused),
                                style = MaterialTheme.typography.labelSmall,
                                color = OnPrimaryFixed,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                )

                // Meta Row (Time, Cycles)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${estimatedPomodoros * 25} mnt ($estimatedPomodoros sesi)",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceVariant
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceVariant
                    )
                    Text(
                        text = "🍅 $completedPomodoros/$estimatedPomodoros Siklus",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (completedPomodoros >= estimatedPomodoros) Primary else OnSurfaceVariant,
                        fontWeight = if (completedPomodoros >= estimatedPomodoros) FontWeight.Bold else FontWeight.Normal
                    )
                    if (!startTime.isNullOrBlank()) {
                        Text(text = "•", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                        Text(text = startTime, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                    }
                }
            }

            // Quick Play/Resume Pomodoro Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isDone) SurfaceContainerLowest.copy(alpha = 0.5f) else PrimaryContainer)
                    .clickable(enabled = !isDone) { onStartPomodoro() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = stringResource(R.string.action_mulai),
                    tint = if (isDone) Outline else OnPrimaryFixed,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
