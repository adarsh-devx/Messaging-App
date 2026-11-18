package dev.floww.android.features.rooms

sealed interface RoomsScreenActions {
    data class OpenChatRoom(val roomId: String, val roomName: String) : RoomsScreenActions
    data object OpenCreateRoomSheet : RoomsScreenActions
    data object OpenProfileSheet : RoomsScreenActions
    data class ShowToast(val message: String) : RoomsScreenActions
}
