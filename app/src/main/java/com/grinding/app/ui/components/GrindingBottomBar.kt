package com.grinding.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grinding.app.R
import com.grinding.app.ui.navigation.NavRoutes
import com.grinding.app.ui.theme.*

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val labelRes: Int
) {
    object Beranda : BottomNavItem(NavRoutes.BERANDA, Icons.Default.Home, R.string.nav_beranda)
    object Kalender : BottomNavItem(NavRoutes.KALENDER, Icons.Default.CalendarToday, R.string.nav_kalender)
    object Timer : BottomNavItem(NavRoutes.TIMER, Icons.Default.HourglassTop, R.string.nav_timer)
    object Statistik : BottomNavItem(NavRoutes.STATISTIK, Icons.Default.BarChart, R.string.nav_statistik)
    object Profil : BottomNavItem(NavRoutes.PROFIL, Icons.Default.Person, R.string.nav_profil)
}

@Composable
fun GrindingBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SurfaceContainerLowest.copy(alpha = 0.95f),
        tonalElevation = 8.dp,
        shadowElevation = 16.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(72.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Item 1: Beranda
                BottomNavTab(
                    item = BottomNavItem.Beranda,
                    selected = currentRoute == NavRoutes.BERANDA,
                    onClick = { onNavigate(NavRoutes.BERANDA) }
                )

                // Item 2: Kalender
                BottomNavTab(
                    item = BottomNavItem.Kalender,
                    selected = currentRoute == NavRoutes.KALENDER,
                    onClick = { onNavigate(NavRoutes.KALENDER) }
                )

                // Spacer for center elevated Timer
                Spacer(modifier = Modifier.width(64.dp))

                // Item 4: Statistik
                BottomNavTab(
                    item = BottomNavItem.Statistik,
                    selected = currentRoute == NavRoutes.STATISTIK,
                    onClick = { onNavigate(NavRoutes.STATISTIK) }
                )

                // Item 5: Profil
                BottomNavTab(
                    item = BottomNavItem.Profil,
                    selected = currentRoute == NavRoutes.PROFIL,
                    onClick = { onNavigate(NavRoutes.PROFIL) }
                )
            }

            // Center Elevated Timer Action Button
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-14).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(12.dp, CircleShape, spotColor = PrimaryContainer)
                        .clip(CircleShape)
                        .background(PrimaryContainer)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple()
                        ) {
                            onNavigate(NavRoutes.TIMER)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassTop,
                        contentDescription = stringResource(R.string.nav_timer),
                        tint = OnPrimaryFixed,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.nav_timer),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (currentRoute == NavRoutes.TIMER) Primary else OnSurfaceVariant,
                    fontWeight = if (currentRoute == NavRoutes.TIMER) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun BottomNavTab(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (selected) Primary else OnSurfaceVariant
    val fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium

    Column(
        modifier = modifier
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = stringResource(item.labelRes),
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = stringResource(item.labelRes),
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            fontWeight = fontWeight,
            fontSize = 11.sp
        )
    }
}
