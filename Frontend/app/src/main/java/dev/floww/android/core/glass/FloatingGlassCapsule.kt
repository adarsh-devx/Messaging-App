package dev.floww.android.core.glass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeChild
import dev.floww.android.core.theme.FlowwColors
import dev.floww.android.core.theme.FlowwShapes

/**
 * Floating Glass Capsule Component.
 * Seen in Glassy UI & Apple VisionOS dock: a floating frosted pill with high blur,
 * specular rim, and elevated ambient drop shadow.
 */
@Composable
fun FloatingGlassCapsule(
    modifier: Modifier = Modifier,
    hazeState: HazeState? = null,
    height: Dp = 64.dp,
    content: @Composable RowScope.() -> Unit
) {
    val pillShape = FlowwShapes.SquirclePill

    val baseModifier = modifier
        .height(height)
        .shadow(
            elevation = 20.dp,
            shape = pillShape,
            ambientColor = Color.Black.copy(alpha = 0.45f),
            spotColor = Color.Black.copy(alpha = 0.35f)
        )
        .clip(pillShape)

    val blurModifier = if (hazeState != null) {
        baseModifier.hazeChild(
            state = hazeState,
            shape = pillShape,
            style = HazeDefaults.style(
                backgroundColor = FlowwColors.GlassPillDark,
                tint = HazeTint(Color.White.copy(alpha = 0.05f)),
                blurRadius = 32.dp,
                noiseFactor = 0.02f
            )
        )
    } else {
        baseModifier.background(FlowwColors.GlassPillDark, pillShape)
    }

    Box(
        modifier = blurModifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.12f),
                        Color.White.copy(alpha = 0.02f)
                    )
                ),
                shape = pillShape
            )
            .glassBorder(
                width = 1.2.dp,
                shape = pillShape,
                startColor = FlowwColors.SpecularRimHigh,
                endColor = FlowwColors.SpecularRimLow
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}
