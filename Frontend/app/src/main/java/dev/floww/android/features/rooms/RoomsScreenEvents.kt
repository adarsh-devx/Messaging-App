package dev.floww.android.features.rooms

sealed interface RoomsScreenEvents {
    data class Init(val userId: String, val username: String) : RoomsScreenEvents
    data class FilterSelected(val filter: RoomFilterTab) : RoomsScreenEvents
    data class BottomTabSelected(val tab: RoomsBottomTab) : RoomsScreenEvents
    data class SearchQueryChanged(val query: String) : RoomsScreenEvents
    data object ToggleSearch : RoomsScreenEvents
    data class RoomClicked(val roomId: String, val roomName: String) : RoomsScreenEvents
    data class FriendStoryClicked(val friendId: String, val friendName: String) : RoomsScreenEvents
    data object CreateRoomClicked : RoomsScreenEvents
    data object ProfileMenuClicked : RoomsScreenEvents
    data object AddStoryClicked : RoomsScreenEvents
    data object RefreshTriggered : RoomsScreenEvents
}
