package dev.floww.android.features.rooms

enum class RoomFilterTab(val label: String) {
    ALL("All"),
    UNSEND("Unsend"),
    GRUPS("Grups"),
    FAVORITE("Favorite")
}

enum class RoomsBottomTab(val label: String, val badgeCount: Int = 0) {
    CHAT("Chat", badgeCount = 9),
    CALLING("Calling"),
    PROFILE("Profile")
}

data class StoryUser(
    val id: String,
    val name: String,
    val avatarSeed: String,
    val avatarBgColor: Long
)

data class ChatMessageItem(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: String = "17:45",
    val unreadCount: Int = 0,
    val isDoubleTick: Boolean = false,
    val isSingleTick: Boolean = false,
    val isTyping: Boolean = false,
    val avatarBgColor: Long = 0xFF2A2E3D
)

data class RoomsScreenState(
    val userId: String = "",
    val username: String = "",
    val activeFilter: RoomFilterTab = RoomFilterTab.ALL,
    val activeBottomTab: RoomsBottomTab = RoomsBottomTab.CHAT,
    val isSearching: Boolean = false,
    val searchQuery: String = "",
    val stories: List<StoryUser> = emptyList(),
    val chats: List<ChatMessageItem> = emptyList()
) {
    val filteredChats: List<ChatMessageItem>
        get() {
            var result = chats
            if (searchQuery.isNotBlank()) {
                val q = searchQuery.trim().lowercase()
                result = result.filter {
                    it.name.lowercase().contains(q) || it.lastMessage.lowercase().contains(q)
                }
            }
            return when (activeFilter) {
                RoomFilterTab.ALL -> result
                RoomFilterTab.CHATS -> result.filter { it.unreadCount == 0 && !it.name.contains("Roomie") }
                RoomFilterTab.GRUPS -> result.filter { it.name.contains("Roomie") || it.unreadCount > 2 }
                RoomFilterTab.FAVORITE -> result.take(3)
            }
        }
}
