package dev.floww.android.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val FlowwDarkColorScheme = darkColorScheme(
    primary = FlowwColors.FlowwIris,
    onPrimary = FlowwColors.TextPrimary,
    secondary = FlowwColors.FlowwCyan,
    background = FlowwColors.Obsidian,
    surface = FlowwColors.ObsidianSurface,
    onSurface = FlowwColors.TextPrimary
)

@Composable
fun FlowwTheme(
    darkTheme: Boolean = true, // Floww defaults to immersive dark glass aesthetic
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = android.graphics.Color.TRANSPARENT
                window.navigationBarColor = android.graphics.Color.TRANSPARENT
                WindowCompat.setDecorFitsSystemWindows(window, false)
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = FlowwDarkColorScheme,
        typography = FlowwTypography,
        content = content
    )
}
