package dev.floww.android.core.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Custom 2.2px thick-stroke, round-capped, round-joined vector icons in the
 * spirit of Telegram and Dial / BitChord's icon family.
 * Uniform stroke weight ensures tactile, polished visual hierarchy.
 */
object FlowwIcons {
    private const val STROKE = 2.2f
    private val stroke = SolidColor(Color.Black)
    private val fill = SolidColor(Color.Black)

    /** 2.2px Telegram-style round speech bubble (Line / Outline) */
    val ChatOutline: ImageVector by lazy {
        ImageVector.Builder(
            name = "floww_chat_outline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = stroke,
                strokeLineWidth = STROKE,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 4f)
                lineTo(18f, 4f)
                quadTo(20.5f, 4f, 20.5f, 6.5f)
                lineTo(20.5f, 14.5f)
                quadTo(20.5f, 17f, 18f, 17f)
                lineTo(10f, 17f)
                lineTo(6f, 20.2f)
                lineTo(6f, 17f)
                quadTo(3.5f, 17f, 3.5f, 14.5f)
                lineTo(3.5f, 6.5f)
                quadTo(3.5f, 4f, 6f, 4f)
                close()
            }
        }.build()
    }

    /** 2.2px Telegram-style round speech bubble (Solid / Fill) */
    val ChatFilled: ImageVector by lazy {
        ImageVector.Builder(
            name = "floww_chat_filled",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = fill) {
                moveTo(6f, 4f)
                lineTo(18f, 4f)
                quadTo(20.5f, 4f, 20.5f, 6.5f)
                lineTo(20.5f, 14.5f)
                quadTo(20.5f, 17f, 18f, 17f)
                lineTo(10f, 17f)
                lineTo(6f, 20.2f)
                lineTo(6f, 17f)
                quadTo(3.5f, 17f, 3.5f, 14.5f)
                lineTo(3.5f, 6.5f)
                quadTo(3.5f, 4f, 6f, 4f)
                close()
            }
        }.build()
    }

    val Chat: ImageVector get() = ChatOutline

    /** 2.2px Telegram-style curved phone handset (Line / Outline) */
    val CallingOutline: ImageVector by lazy {
        ImageVector.Builder(
            name = "floww_calling_outline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = stroke,
                strokeLineWidth = STROKE,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5f, 4f)
                lineTo(8.2f, 4f)
                lineTo(9.6f, 8.2f)
                lineTo(7.8f, 9.8f)
                quadTo(9.2f, 12.8f, 11.8f, 14.2f)
                lineTo(13.4f, 12.4f)
                lineTo(17.6f, 13.8f)
                lineTo(17.6f, 17f)
                quadTo(17.6f, 19f, 15.6f, 19.3f)
                quadTo(10.8f, 19.7f, 6.3f, 15.2f)
                quadTo(1.8f, 10.7f, 2.2f, 5.9f)
                quadTo(2.5f, 4f, 5f, 4f)
                close()
            }
        }.build()
    }

    /** 2.2px Telegram-style curved phone handset (Solid / Fill) */
    val CallingFilled: ImageVector by lazy {
        ImageVector.Builder(
            name = "floww_calling_filled",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = fill) {
                moveTo(5f, 4f)
                lineTo(8.2f, 4f)
                lineTo(9.6f, 8.2f)
                lineTo(7.8f, 9.8f)
                quadTo(9.2f, 12.8f, 11.8f, 14.2f)
                lineTo(13.4f, 12.4f)
                lineTo(17.6f, 13.8f)
                lineTo(17.6f, 17f)
                quadTo(17.6f, 19f, 15.6f, 19.3f)
                quadTo(10.8f, 19.7f, 6.3f, 15.2f)
                quadTo(1.8f, 10.7f, 2.2f, 5.9f)
                quadTo(2.5f, 4f, 5f, 4f)
                close()
            }
        }.build()
    }

    val Calling: ImageVector get() = CallingOutline

    /** 2.2px Telegram-style profile user icon (Line / Outline) */
    val ProfileOutline: ImageVector by lazy {
        ImageVector.Builder(
            name = "floww_profile_outline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Head (circle)
            path(
                stroke = stroke,
                strokeLineWidth = STROKE,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(8.5f, 8f)
                arcToRelative(3.5f, 3.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 7f, 0f)
                arcToRelative(3.5f, 3.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -7f, 0f)
            }
            // Torso (shoulders curve)
            path(
                stroke = stroke,
                strokeLineWidth = STROKE,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4.5f, 19f)
                quadTo(4.5f, 14.8f, 12f, 14.8f)
                quadTo(19.5f, 14.8f, 19.5f, 19f)
            }
        }.build()
    }

    /** 2.2px Telegram-style profile user icon (Solid / Fill) */
    val ProfileFilled: ImageVector by lazy {
        ImageVector.Builder(
            name = "floww_profile_filled",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Head (solid circle)
            path(fill = fill) {
                moveTo(8.5f, 8f)
                arcToRelative(3.5f, 3.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 7f, 0f)
                arcToRelative(3.5f, 3.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -7f, 0f)
                close()
            }
            // Torso (solid shoulders)
            path(fill = fill) {
                moveTo(4.5f, 19.5f)
                quadTo(4.5f, 14.5f, 12f, 14.5f)
                quadTo(19.5f, 14.5f, 19.5f, 19.5f)
                close()
            }
        }.build()
    }

    val Profile: ImageVector get() = ProfileOutline

    /** 2.2px Telegram/Dial-style round plus icon (Action / New Chat) */
    val Plus: ImageVector by lazy {
        ImageVector.Builder(
            name = "floww_plus",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = stroke,
                strokeLineWidth = STROKE,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Horizontal bar
                moveTo(6f, 12f)
                lineTo(18f, 12f)
                // Vertical bar
                moveTo(12f, 6f)
                lineTo(12f, 18f)
            }
        }.build()
    }

    /** 2.2px BitChord round search magnifier */
    val Search: ImageVector by lazy {
        ImageVector.Builder(
            name = "floww_search",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = stroke,
                strokeLineWidth = STROKE,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Lens (circle)
                moveTo(4.6f, 11f)
                arcToRelative(6.4f, 6.4f, 0f, isMoreThanHalf = true, isPositiveArc = true, 12.8f, 0f)
                arcToRelative(6.4f, 6.4f, 0f, isMoreThanHalf = true, isPositiveArc = true, -12.8f, 0f)
                // Handle
                moveTo(15.9f, 15.9f)
                lineTo(20.4f, 20.4f)
            }
        }.build()
    }
}
