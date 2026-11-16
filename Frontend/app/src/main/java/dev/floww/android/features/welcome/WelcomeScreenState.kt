package dev.floww.android.features.welcome

data class WelcomeFeature(
    val title: String,
    val subtitle: String,
    val badge: String,
    val demoMessage: String,
    val demoTimerText: String
)

data class WelcomeScreenState(
    val activeFeatureIndex: Int = 0,
    val features: List<WelcomeFeature> = listOf(
        WelcomeFeature(
            title = "Disappearing\nMoments.",
            subtitle = "Messages self-destruct once read. No traces left behind.",
            badge = "5m Timer",
            demoMessage = "Hey! This secret will vanish in 5 minutes after you read it...",
            demoTimerText = "04:59"
        ),
        WelcomeFeature(
            title = "Zero Footprint\nStorage.",
            subtitle = "True RAM-only hygiene. Nothing is stored permanently on disk or cloud.",
            badge = "Signal Grade",
            demoMessage = "End-to-end encrypted with zero server logs.",
            demoTimerText = "Permanent"
        ),
        WelcomeFeature(
            title = "Liquid Glass\nExperience.",
            subtitle = "Designed with pure optical physics and 120 FPS fluid gestures.",
            badge = "VisionOS",
            demoMessage = "Frosted specular cards with living ambient aura.",
            demoTimerText = "30s"
        )
    ),
    val isLoading: Boolean = false
)
