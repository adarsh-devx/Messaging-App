package dev.floww.android.core.glass

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Living Aura Mesh Canvas.
 * Exact 1:1 color match extracted directly from the reference image pixels:
 * - Top-Left: Powder Sky Blue (#90ADCB)
 * - Top-Right: Soft Lavender (#9EAAD0)
 * - Mid-Right: Candy / Bubblegum Rose Pink (#F4A1D5)
 * - Mid-Left: Warm Glowing Peach/Apricot (#F2DACE)
 * - Bottom Half: Powder Ice Blue (#B3D7F1 fading to #A3BFD7)
 */
@Composable
fun LivingAuraBackground(
    modifier: Modifier = Modifier,
    isDark: Boolean = false,
    content: @Composable () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            if (!isDark) {
                // 1. Base Vertical Powder Blue Gradient
                drawRect(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color(0xFF90ADCB),
                            0.2f to Color(0xFFA4C2DB),
                            0.45f to Color(0xFFD4E2EE),
                            0.75f to Color(0xFFB3D7F1),
                            1.0f to Color(0xFFA3BFD7)
                        )
                    )
                )

                // 2. Mid-Right: Candy / Rose Pink Aura (Behind Good Morning & Highlights card)
                val pinkCenter = Offset(w * 0.94f, h * 0.34f)
                val pinkRadius = w * 0.70f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFF4A1D5),
                            Color(0xFFF4A1D5).copy(alpha = 0.65f),
                            Color(0xFFF4A1D5).copy(alpha = 0.0f)
                        ),
                        center = pinkCenter,
                        radius = pinkRadius
                    ),
                    radius = pinkRadius,
                    center = pinkCenter
                )

                // 3. Mid-Left: Warm Peach Apricot Glow (Behind Thought of the Day)
                val peachCenter = Offset(w * 0.04f, h * 0.44f)
                val peachRadius = w * 0.60f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFF2DACE),
                            Color(0xFFF2DACE).copy(alpha = 0.55f),
                            Color(0xFFF2DACE).copy(alpha = 0.0f)
                        ),
                        center = peachCenter,
                        radius = peachRadius
                    ),
                    radius = peachRadius,
                    center = peachCenter
                )

                // 4. Top-Right: Soft Lilac Lavender (Behind 4-dot disk)
                val lavenderCenter = Offset(w * 0.88f, h * 0.12f)
                val lavenderRadius = w * 0.55f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF9EAAD0),
                            Color(0xFF9EAAD0).copy(alpha = 0.40f),
                            Color.Transparent
                        ),
                        center = lavenderCenter,
                        radius = lavenderRadius
                    ),
                    radius = lavenderRadius,
                    center = lavenderCenter
                )

                // 5. Top-Left: Sky Blue
                val skyCenter = Offset(w * 0.12f, h * 0.10f)
                val skyRadius = w * 0.50f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF90ADCB),
                            Color(0xFF90ADCB).copy(alpha = 0.35f),
                            Color.Transparent
                        ),
                        center = skyCenter,
                        radius = skyRadius
                    ),
                    radius = skyRadius,
                    center = skyCenter
                )
            } else {
                drawRect(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFF090614),
                            Color(0xFF0F0B20),
                            Color(0xFF06040C)
                        )
                    )
                )
            }
        }

        content()
    }
}
