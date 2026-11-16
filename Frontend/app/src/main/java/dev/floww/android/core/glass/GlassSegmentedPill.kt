package dev.floww.android.core.glass

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.floww.android.core.theme.FlowwColors
import dev.floww.android.core.theme.FlowwShapes

/**
 * Glassy UI Segmented Pill Tabs.
 * Frosted capsule track with sliding active indicator pill.
 */
@Composable
fun GlassSegmentedPill(
    items: List<String>,
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val trackShape = FlowwShapes.SquirclePill

    Row(
        modifier = modifier
            .clip(trackShape)
            .background(Color.White.copy(alpha = 0.08f), trackShape)
            .glassBorder(
                width = 1.dp,
                shape = trackShape,
                startColor = FlowwColors.SpecularRimHigh.copy(alpha = 0.25f),
                endColor = FlowwColors.SpecularRimLow
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, label ->
            val isSelected = index == selectedIndex
            val interactionSource = remember { MutableInteractionSource() }

            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color(0xFF0C0916) else Color.White.copy(alpha = 0.75f),
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label = "pill_text_color"
            )

            val pillBgColor by animateColorAsState(
                targetValue = if (isSelected) Color(0xFFFDE047) else Color.Transparent, // Yellow/Lime accent or Frosted
                animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMediumLow),
                label = "pill_bg_color"
            )

            Box(
                modifier = Modifier
                    .clip(trackShape)
                    .background(pillBgColor, trackShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onSelectIndex(index) }
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    color = textColor
                )
            }
        }
    }
}
