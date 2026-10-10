package com.grinding.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grinding.app.ui.theme.*

enum class TaskFilter {
    ALL, RUNNING, TODO, DONE
}

@Composable
fun FilterTabs(
    selectedFilter: TaskFilter,
    counts: Map<TaskFilter, Int>,
    onFilterSelect: (TaskFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterTabPill(
            title = "Semua",
            isSelected = selectedFilter == TaskFilter.ALL,
            onClick = { onFilterSelect(TaskFilter.ALL) }
        )
        FilterTabPill(
            title = "Berjalan (${counts[TaskFilter.RUNNING] ?: 0})",
            isSelected = selectedFilter == TaskFilter.RUNNING,
            onClick = { onFilterSelect(TaskFilter.RUNNING) }
        )
        FilterTabPill(
            title = "Belum (${counts[TaskFilter.TODO] ?: 0})",
            isSelected = selectedFilter == TaskFilter.TODO,
            onClick = { onFilterSelect(TaskFilter.TODO) }
        )
        FilterTabPill(
            title = "Selesai (${counts[TaskFilter.DONE] ?: 0})",
            isSelected = selectedFilter == TaskFilter.DONE,
            onClick = { onFilterSelect(TaskFilter.DONE) }
        )
    }
}

@Composable
private fun FilterTabPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) InverseSurface else SurfaceContainer
    val contentColor = if (isSelected) InverseOnSurface else OnSurfaceVariant

    Surface(
        shape = PillShape,
        color = backgroundColor,
        modifier = Modifier
            .clip(PillShape)
            .clickable { onClick() }
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
