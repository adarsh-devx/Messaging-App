package dev.floww.android.core.glass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
 * Micro Glass Badge / Pill.
 * Ideal for self-destruct timers, privacy status tags, and online indicators.
 */
@Composable
fun GlassBadge(
    text: String,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    backgroundColor: Color = Color.White.copy(alpha = 0.10f),
    textColor: Color = Color.White.copy(alpha = 0.90f)
) {
    val shape = FlowwShapes.SquircleBadge

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor, shape)
            .glassBorder(
                width = 0.8.dp,
                shape = shape,
                startColor = FlowwColors.SpecularRimHigh.copy(alpha = 0.30f),
                endColor = FlowwColors.SpecularRimLow
            )
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icon != null) {
                icon()
            }
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                letterSpacing = 0.4.sp
            )
        }
    }
}
