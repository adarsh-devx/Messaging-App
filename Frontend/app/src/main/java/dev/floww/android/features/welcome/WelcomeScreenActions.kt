package dev.floww.android.features.welcome

sealed interface WelcomeScreenActions {
    data class NavigateToAuth(val isRegister: Boolean) : WelcomeScreenActions
    data object ShowInfoDialog : WelcomeScreenActions
}
