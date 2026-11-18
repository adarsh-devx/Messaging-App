package dev.floww.android.features.rooms

import dev.floww.android.core.mvi.EventDrivenViewModel

class RoomsScreenViewModel(
    initialState: RoomsScreenState = RoomsScreenState()
) : EventDrivenViewModel<RoomsScreenEvents, RoomsScreenState, RoomsScreenActions>(initialState) {

    init {
        loadMockData()
    }

    override suspend fun handleEvent(event: RoomsScreenEvents) {
        when (event) {
            is RoomsScreenEvents.Init -> {
                setState { it.copy(userId = event.userId, username = event.username) }
            }

            is RoomsScreenEvents.FilterSelected -> {
                setState { it.copy(activeFilter = event.filter) }
            }

            is RoomsScreenEvents.BottomTabSelected -> {
                setState { it.copy(activeBottomTab = event.tab) }
            }

            is RoomsScreenEvents.SearchQueryChanged -> {
                setState { it.copy(searchQuery = event.query) }
            }

            is RoomsScreenEvents.ToggleSearch -> {
                setState {
                    val next = !it.isSearching
                    it.copy(isSearching = next, searchQuery = if (!next) "" else it.searchQuery)
                }
            }

            is RoomsScreenEvents.RoomClicked -> {
                sendAction(RoomsScreenActions.OpenChatRoom(event.roomId, event.roomName))
            }

            is RoomsScreenEvents.FriendStoryClicked -> {
                sendAction(RoomsScreenActions.ShowToast("Story: ${event.friendName}"))
            }

            is RoomsScreenEvents.CreateRoomClicked -> {
                sendAction(RoomsScreenActions.OpenCreateRoomSheet)
            }

            is RoomsScreenEvents.ProfileMenuClicked -> {
                sendAction(RoomsScreenActions.OpenProfileSheet)
            }

            is RoomsScreenEvents.AddStoryClicked -> {
                sendAction(RoomsScreenActions.ShowToast("Add to story"))
            }

            is RoomsScreenEvents.RefreshTriggered -> {
                loadMockData()
            }
        }
    }

    private fun loadMockData() {
        val mockStories = listOf(
            StoryUser("s1", "Roger", "roger", 0xFF3F3B36),
            StoryUser("s2", "Julian", "julian", 0xFF2D3748),
            StoryUser("s3", "Silvana", "silvana", 0xFF4A3B32),
            StoryUser("s4", "Alice", "alice", 0xFF5A5B64),
            StoryUser("s5", "Joko", "joko", 0xFF2B3238)
        )

        val mockChats = listOf(
            ChatMessageItem(
                id = "c1",
                name = "Nina Flora",
                lastMessage = "See you",
                timestamp = "17:45",
                unreadCount = 0,
                isDoubleTick = true,
                avatarBgColor = 0xFF9A7B4A
            ),
            ChatMessageItem(
                id = "c2",
                name = "Danang Calvin",
                lastMessage = "Let's meet there at 9 PM?",
                timestamp = "17:45",
                unreadCount = 0,
                isDoubleTick = true,
                avatarBgColor = 0xFF2E3440
            ),
            ChatMessageItem(
                id = "c3",
                name = "Satria herman",
                lastMessage = "Please send me the updated pitch deck...",
                timestamp = "17:45",
                unreadCount = 3,
                isDoubleTick = false,
                avatarBgColor = 0xFF5E6572
            ),
            ChatMessageItem(
                id = "c4",
                name = "Alexander",
                lastMessage = "Typing...",
                timestamp = "17:45",
                unreadCount = 2,
                isTyping = true,
                avatarBgColor = 0xFF4C4B59
            ),
            ChatMessageItem(
                id = "c5",
                name = "Jake",
                lastMessage = "Let me know when you're free to jam...",
                timestamp = "17:45",
                unreadCount = 0,
                isSingleTick = true,
                avatarBgColor = 0xFF3A3F47
            ),
            ChatMessageItem(
                id = "c6",
                name = "Roomie",
                lastMessage = "We're out of milk again \uD83D\uDE43",
                timestamp = "17:45",
                unreadCount = 0,
                isDoubleTick = true,
                avatarBgColor = 0xFF8B6F55
            ),
            ChatMessageItem(
                id = "c7",
                name = "Danang Calvin",
                lastMessage = "Sounds good to me!",
                timestamp = "17:45",
                unreadCount = 0,
                isDoubleTick = true,
                avatarBgColor = 0xFF2A2E3D
            )
        )

        setState {
            it.copy(
                stories = mockStories,
                chats = mockChats
            )
        }
    }
}
