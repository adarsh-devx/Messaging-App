package dev.floww.android.core.glass

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.floww.android.core.theme.FlowwColors

/**
 * Glassy UI Specular Lit Rim Modifier.
 * Draws an optical 135° light reflection gradient along the boundary of the shape.
 * Simulates high-end liquid glass refraction.
 */
fun Modifier.glassBorder(
    width: Dp = 1.dp,
    shape: Shape,
    startColor: Color = FlowwColors.SpecularRimHigh,
    endColor: Color = FlowwColors.SpecularRimLow
): Modifier = this.drawWithContent {
    drawContent()
    val outline = shape.createOutline(size, layoutDirection, this)
    drawOutline(
        outline = outline,
        brush = Brush.linearGradient(
            colors = listOf(startColor, endColor),
            start = Offset.Zero,
            end = Offset(size.width, size.height)
        ),
        style = Stroke(width = width.toPx())
    )
}
