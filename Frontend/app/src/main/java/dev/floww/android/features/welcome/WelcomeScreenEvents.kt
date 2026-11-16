package dev.floww.android.features.welcome

sealed interface WelcomeScreenEvents {
    data class FeatureSelected(val index: Int) : WelcomeScreenEvents
    data object GetStartedClicked : WelcomeScreenEvents
    data object SignInClicked : WelcomeScreenEvents
    data object InfoClicked : WelcomeScreenEvents
}
