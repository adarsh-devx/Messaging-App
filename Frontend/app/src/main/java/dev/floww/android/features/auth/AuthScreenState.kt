package dev.floww.android.features.auth

data class AuthScreenState(
    val isRegisterMode: Boolean = false,
    val displayName: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
