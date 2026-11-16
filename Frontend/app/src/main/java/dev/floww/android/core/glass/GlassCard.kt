package dev.floww.android.core.glass

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeChild
import dev.floww.android.core.theme.FlowwColors
import dev.floww.android.core.theme.FlowwShapes

/**
 * Glassy UI Glass Card / Panel.
 * Bento-style squircle container with real-time backdrop blur,
 * specular lit rim highlight, and surface sheen.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    hazeState: HazeState? = null,
    shape: Shape = FlowwShapes.SquircleCard,
    backgroundColor: Color = FlowwColors.GlassCardDark,
    blurRadius: Dp = 24.dp,
    borderWidth: Dp = 1.dp,
    rimStartColor: Color = FlowwColors.SpecularRimHigh,
    rimEndColor: Color = FlowwColors.SpecularRimLow,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.982f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "card_spring_press"
    )

    val baseModifier = modifier
        .scale(scale)
        .clip(shape)

    val blurModifier = if (hazeState != null) {
        baseModifier.hazeChild(
            state = hazeState,
            shape = shape,
            style = HazeDefaults.style(
                backgroundColor = backgroundColor,
                tint = HazeTint(backgroundColor),
                blurRadius = blurRadius,
                noiseFactor = 0.02f
            )
        )
    } else {
        baseModifier.background(backgroundColor, shape)
    }

    val clickableModifier = if (onClick != null) {
        blurModifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    } else {
        blurModifier
    }

    Box(
        modifier = clickableModifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.08f),
                        Color.White.copy(alpha = 0.01f)
                    )
                ),
                shape = shape
            )
            .glassBorder(
                width = borderWidth,
                shape = shape,
                startColor = rimStartColor,
                endColor = rimEndColor
            )
            .padding(16.dp),
        content = content
    )
}
