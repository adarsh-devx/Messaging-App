package dev.floww.android.features.auth

sealed interface AuthScreenEvents {
    data class SwitchMode(val isRegister: Boolean) : AuthScreenEvents
    data class DisplayNameChanged(val value: String) : AuthScreenEvents
    data class UsernameChanged(val value: String) : AuthScreenEvents
    data class EmailChanged(val value: String) : AuthScreenEvents
    data class PasswordChanged(val value: String) : AuthScreenEvents
    data object TogglePasswordVisibility : AuthScreenEvents
    data object SubmitClicked : AuthScreenEvents
    data object BackClicked : AuthScreenEvents
    data object ErrorDismissed : AuthScreenEvents
}
