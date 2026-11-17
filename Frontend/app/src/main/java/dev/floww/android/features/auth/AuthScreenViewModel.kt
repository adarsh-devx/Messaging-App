package dev.floww.android.features.auth

import dev.floww.android.core.mvi.EventDrivenViewModel

class AuthScreenViewModel(
    initialState: AuthScreenState = AuthScreenState()
) : EventDrivenViewModel<AuthScreenEvents, AuthScreenState, AuthScreenActions>(initialState) {

    override suspend fun handleEvent(event: AuthScreenEvents) {
        when (event) {
            is AuthScreenEvents.SwitchMode -> {
                setState {
                    it.copy(
                        isRegisterMode = event.isRegister,
                        errorMessage = null
                    )
                }
            }

            is AuthScreenEvents.DisplayNameChanged -> {
                setState { it.copy(displayName = event.value, errorMessage = null) }
            }

            is AuthScreenEvents.UsernameChanged -> {
                setState { it.copy(username = event.value, errorMessage = null) }
            }

            is AuthScreenEvents.EmailChanged -> {
                setState { it.copy(email = event.value, errorMessage = null) }
            }

            is AuthScreenEvents.PasswordChanged -> {
                setState { it.copy(password = event.value, errorMessage = null) }
            }

            is AuthScreenEvents.TogglePasswordVisibility -> {
                setState { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is AuthScreenEvents.BackClicked -> {
                sendAction(AuthScreenActions.NavigateBack)
            }

            is AuthScreenEvents.ErrorDismissed -> {
                setState { it.copy(errorMessage = null) }
            }

            is AuthScreenEvents.SubmitClicked -> {
                performSubmit()
            }
        }
    }

    private suspend fun performSubmit() {
        val current = state.value
        if (current.isRegisterMode) {
            // Validate Registration
            if (current.displayName.isBlank()) {
                setState { it.copy(errorMessage = "Please enter your name") }
                return
            }
            if (current.username.isBlank() || current.username.length < 3) {
                setState { it.copy(errorMessage = "Username must be at least 3 characters") }
                return
            }
            if (current.email.isBlank() || !current.email.contains("@")) {
                setState { it.copy(errorMessage = "Please enter a valid email address") }
                return
            }
            if (current.password.length < 8) {
                setState { it.copy(errorMessage = "Password must be at least 8 characters") }
                return
            }

            setState { it.copy(isLoading = true, errorMessage = null) }
            // Simulated / Mock authentication dispatch until backend Retrofit client is plugged
            sendAction(
                AuthScreenActions.NavigateToRooms(
                    userId = "user_registered_${System.currentTimeMillis()}",
                    username = current.username.trim().removePrefix("@")
                )
            )
            setState { it.copy(isLoading = false) }
        } else {
            // Validate Login
            if (current.username.isBlank()) {
                setState { it.copy(errorMessage = "Please enter your username or email") }
                return
            }
            if (current.password.isBlank()) {
                setState { it.copy(errorMessage = "Please enter your password") }
                return
            }

            setState { it.copy(isLoading = true, errorMessage = null) }
            sendAction(
                AuthScreenActions.NavigateToRooms(
                    userId = "user_login_${System.currentTimeMillis()}",
                    username = current.username.trim().removePrefix("@")
                )
            )
            setState { it.copy(isLoading = false) }
        }
    }
}
