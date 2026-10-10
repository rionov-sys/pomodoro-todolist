package com.grinding.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grinding.app.ui.theme.*

data class TaskCategory(
    val name: String,
    val colorKey: String,
    val containerColor: Color,
    val onContainerColor: Color,
    val dotColor: Color
)

val defaultCategories = listOf(
    TaskCategory("Desain", "pink", PastelPinkContainer, PastelPinkOnContainer, PastelPinkDot),
    TaskCategory("Koding", "lavender", PastelLavenderContainer, PastelLavenderOnContainer, PastelLavenderDot),
    TaskCategory("Riset", "mint", PastelMintContainer, PastelMintOnContainer, PastelMintDot),
    TaskCategory("Pribadi", "amber", PastelAmberContainer, PastelAmberOnContainer, PastelAmberDot),
    TaskCategory("Menulis", "sky", PastelSkyContainer, PastelSkyOnContainer, PastelSkyDot)
)

@Composable
fun CategorySelector(
    selectedCategory: String,
    onCategorySelected: (TaskCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        defaultCategories.forEach { category ->
            val isSelected = category.name.equals(selectedCategory, ignoreCase = true)
            Surface(
                shape = PillShape,
                color = category.containerColor,
                modifier = Modifier
                    .clip(PillShape)
                    .then(
                        if (isSelected) Modifier.border(2.dp, category.dotColor, PillShape)
                        else Modifier
                    )
                    .clickable { onCategorySelected(category) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(category.dotColor)
                    )
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = category.onContainerColor
                    )
                }
            }
        }
    }
}
