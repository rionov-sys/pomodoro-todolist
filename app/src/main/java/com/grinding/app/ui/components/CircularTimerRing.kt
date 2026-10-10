package com.grinding.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grinding.app.ui.theme.*

@Composable
fun CircularTimerRing(
    progress: Float, // 0.0 to 1.0
    timeText: String, // "24:36"
    modeText: String = "Deep Flow Mode",
    phaseSubtext: String = "Sesi 1 dari 4 • Siklus Pomodoro",
    completedCyclesInRound: Int = 1,
    totalCyclesInRound: Int = 4,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        label = "timerProgress"
    )

    Box(
        modifier = modifier
            .size(280.dp),
        contentAlignment = Alignment.Center
    ) {
        // Canvas Progress Rings
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 14.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2
            val center = this.center

            // Background Track
            drawCircle(
                color = SurfaceContainerHighest,
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth)
            )

            // Progress Fill Arc
            val sweepAngle = 360f * animatedProgress
            drawArc(
                color = PrimaryContainer,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        // Center Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            // Mode Badge
            Surface(
                shape = PillShape,
                color = Primary.copy(alpha = 0.1f),
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = modeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = Primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Big Timer Numbers
            Text(
                text = timeText,
                style = DisplayTimerStyle,
                color = OnSurface,
                letterSpacing = (-1.5).sp
            )

            // Phase Info Subtext
            Text(
                text = phaseSubtext,
                style = MaterialTheme.typography.labelMedium,
                color = OnSurfaceVariant,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 2.dp)
            )

            // Cycle Dots Indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                for (i in 1..totalCyclesInRound) {
                    val isDone = i <= completedCyclesInRound
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (isDone) PrimaryContainer else SurfaceContainerHighest)
                    )
                }
            }
        }
    }
}
