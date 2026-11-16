package dev.floww.android.core.glass

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import dev.floww.android.core.theme.FlowwColors

/**
 * Living Aura Mesh Canvas.
 * Renders an ethereal, breathing pastel & iridescent ambient background
 * that shines dynamically through Glassy UI frosted surfaces.
 */
@Composable
fun LivingAuraBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "living_aura_motion")

    // Slow organic phase offsets (6 to 10 second cycles)
    val phaseA by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "phaseA"
    )

    val phaseB by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 11000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "phaseB"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FlowwColors.Obsidian)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Base deep atmospheric gradient
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF090614),
                        Color(0xFF0F0B20),
                        Color(0xFF06040C)
                    )
                )
            )

            // Orb 1: Soft Sky Blue Glow (Top Right)
            val orb1Center = Offset(
                x = w * (0.75f + (phaseA * 0.15f)),
                y = h * (0.15f + (phaseB * 0.10f))
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        FlowwColors.AuroraSkyBlue.copy(alpha = 0.38f),
                        FlowwColors.AuroraSkyBlue.copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = orb1Center,
                    radius = w * 0.85f
                ),
                radius = w * 0.85f,
                center = orb1Center
            )

            // Orb 2: Radiant Lilac / Lavender Bloom (Center Left)
            val orb2Center = Offset(
                x = w * (0.20f - (phaseB * 0.12f)),
                y = h * (0.45f + (phaseA * 0.12f))
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        FlowwColors.AuroraLilac.copy(alpha = 0.35f),
                        FlowwColors.FlowwIris.copy(alpha = 0.18f),
                        Color.Transparent
                    ),
                    center = orb2Center,
                    radius = w * 0.90f
                ),
                radius = w * 0.90f,
                center = orb2Center
            )

            // Orb 3: Warm Blush Peach Sunset (Bottom Right)
            val orb3Center = Offset(
                x = w * (0.80f - (phaseA * 0.10f)),
                y = h * (0.82f - (phaseB * 0.08f))
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        FlowwColors.AuroraBlushPeach.copy(alpha = 0.32f),
                        FlowwColors.AuroraWarmSunset.copy(alpha = 0.14f),
                        Color.Transparent
                    ),
                    center = orb3Center,
                    radius = w * 0.80f
                ),
                radius = w * 0.80f,
                center = orb3Center
            )

            // Orb 4: Subtle Mint Breeze Accent (Top Left)
            val orb4Center = Offset(
                x = w * (0.10f + (phaseA * 0.08f)),
                y = h * (0.08f + (phaseB * 0.06f))
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        FlowwColors.AuroraSoftMint.copy(alpha = 0.22f),
                        Color.Transparent
                    ),
                    center = orb4Center,
                    radius = w * 0.55f
                ),
                radius = w * 0.55f,
                center = orb4Center
            )
        }

        content()
    }
}
