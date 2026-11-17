package dev.floww.android.features.auth

sealed interface AuthScreenActions {
    data class NavigateToRooms(val userId: String, val username: String) : AuthScreenActions
    data object NavigateBack : AuthScreenActions
    data class ShowToast(val message: String) : AuthScreenActions
}
