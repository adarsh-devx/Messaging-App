package dev.floww.android.core.theme

import androidx.compose.ui.graphics.Color

object FlowwColors {
    // Obsidian Canvas & Dark Glass
    val Obsidian = Color(0xFF05030A)
    val ObsidianSurface = Color(0xFF0C0916)
    val ObsidianElevated = Color(0xFF140F24)
    val ObsidianStroke = Color(0xFF221A38)

    // Glass Translucency Tokens
    val GlassSurfaceDark = Color(0xFF161224).copy(alpha = 0.55f)
    val GlassCardDark = Color(0xFF1F1A33).copy(alpha = 0.40f)
    val GlassPillDark = Color(0xFF282242).copy(alpha = 0.60f)
    val GlassInputDark = Color(0xFF0D0A17).copy(alpha = 0.50f)

    // Specular Rim Highlights
    val SpecularRimHigh = Color.White.copy(alpha = 0.35f)
    val SpecularRimLow = Color.White.copy(alpha = 0.05f)
    val SpecularRimFocused = Color.White.copy(alpha = 0.60f)

    // Living Aurora Ambient Pastels (From GlassyUI Reference)
    val AuroraSkyBlue = Color(0xFF9BC5FF)
    val AuroraLilac = Color(0xFFD0BFFF)
    val AuroraBlushPeach = Color(0xFFFFB5C2)
    val AuroraSoftMint = Color(0xFF9EF0D0)
    val AuroraWarmSunset = Color(0xFFFFD4A9)

    // Brand Neon Accents
    val FlowwIris = Color(0xFF8B5CF6)
    val FlowwIrisBright = Color(0xFFA78BFA)
    val FlowwCyan = Color(0xFF06B6D4)
    val FlowwPink = Color(0xFFEC4899)

    // Text & Content Hierarchy
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFFFFFFF).copy(alpha = 0.72f)
    val TextMuted = Color(0xFFFFFFFF).copy(alpha = 0.45f)
    val TextDisabled = Color(0xFFFFFFFF).copy(alpha = 0.25f)
}
