package dev.floww.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.floww.android.core.theme.FlowwColors
import dev.floww.android.core.theme.FlowwTheme
import dev.floww.android.features.auth.AuthScreen
import dev.floww.android.features.auth.AuthScreenEvents
import dev.floww.android.features.auth.AuthScreenViewModel
import dev.floww.android.features.welcome.WelcomeScreen
import dev.floww.android.features.welcome.WelcomeScreenViewModel

sealed interface FlowwDestination {
    data object Welcome : FlowwDestination
    data class Auth(val isRegister: Boolean = false) : FlowwDestination
    data class Rooms(val userId: String, val username: String) : FlowwDestination
}

class MainActivity : ComponentActivity() {

    private val welcomeViewModel: WelcomeScreenViewModel by viewModels()
    private val authViewModel: AuthScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlowwTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = FlowwColors.Obsidian
                ) {
                    var currentDestination by remember { mutableStateOf<FlowwDestination>(FlowwDestination.Welcome) }

                    AnimatedContent(
                        targetState = currentDestination,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "floww_screen_navigation"
                    ) { destination ->
                        when (destination) {
                            is FlowwDestination.Welcome -> {
                                WelcomeScreen(
                                    viewModel = welcomeViewModel,
                                    onNavigateToAuth = { isRegister ->
                                        authViewModel.sendEvent(AuthScreenEvents.SwitchMode(isRegister))
                                        currentDestination = FlowwDestination.Auth(isRegister)
                                    }
                                )
                            }
                            is FlowwDestination.Auth -> {
                                AuthScreen(
                                    viewModel = authViewModel,
                                    onNavigateBack = {
                                        currentDestination = FlowwDestination.Welcome
                                    },
                                    onNavigateToRooms = { userId, username ->
                                        currentDestination = FlowwDestination.Rooms(userId, username)
                                    }
                                )
                            }
                            is FlowwDestination.Rooms -> {
                                // Placeholder for Screen 3: RoomListScreen
                            }
                        }
                    }
                }
            }
        }
    }
}
