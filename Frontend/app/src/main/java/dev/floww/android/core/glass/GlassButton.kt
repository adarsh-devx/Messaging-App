package dev.floww.android.core.glass

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.floww.android.core.theme.FlowwColors
import dev.floww.android.core.theme.FlowwShapes

enum class GlassButtonVariant {
    Prominent,
    Frosted,
    Ghost
}

/**
 * Glassy UI Button Component.
 * Supports Prominent, Frosted, and Icon-only variants with spring touch physics.
 */
@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: GlassButtonVariant = GlassButtonVariant.Prominent,
    shape: Shape = FlowwShapes.SquircleButton,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 500f),
        label = "btn_press_scale"
    )

    val backgroundBrush = when (variant) {
        GlassButtonVariant.Prominent -> Brush.linearGradient(
            colors = listOf(
                FlowwColors.FlowwIrisBright,
                FlowwColors.FlowwIris,
                Color(0xFF6D28D9)
            )
        )
        GlassButtonVariant.Frosted -> Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.14f),
                Color.White.copy(alpha = 0.04f)
            )
        )
        GlassButtonVariant.Ghost -> Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color.Transparent
            )
        )
    }

    val rimHigh = when (variant) {
        GlassButtonVariant.Prominent -> Color.White.copy(alpha = 0.50f)
        GlassButtonVariant.Frosted -> FlowwColors.SpecularRimHigh
        GlassButtonVariant.Ghost -> Color.White.copy(alpha = 0.12f)
    }

    Box(
        modifier = modifier
            .scale(scale)
            .defaultMinSize(minHeight = 52.dp)
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .background(backgroundBrush, shape)
            .glassBorder(
                width = 1.dp,
                shape = shape,
                startColor = rimHigh,
                endColor = FlowwColors.SpecularRimLow
            )
            .padding(horizontal = 24.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

/**
 * Circular / Squircle Icon Capsule Button.
 */
@Composable
fun GlassIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    shape: Shape = FlowwShapes.SquirclePill,
    icon: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "icon_btn_scale"
    )

    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .background(Color.White.copy(alpha = 0.12f), shape)
            .glassBorder(
                width = 1.dp,
                shape = shape,
                startColor = FlowwColors.SpecularRimHigh,
                endColor = FlowwColors.SpecularRimLow
            ),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}
