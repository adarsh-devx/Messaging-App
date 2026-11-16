package dev.floww.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dev.floww.android.core.theme.FlowwColors
import dev.floww.android.core.theme.FlowwTheme
import dev.floww.android.features.welcome.WelcomeScreen
import dev.floww.android.features.welcome.WelcomeScreenViewModel

class MainActivity : ComponentActivity() {

    private val welcomeViewModel: WelcomeScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlowwTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = FlowwColors.Obsidian
                ) {
                    WelcomeScreen(
                        viewModel = welcomeViewModel,
                        onNavigateToAuth = { isRegister ->
                            // Will navigate to AuthScreen
                        }
                    )
                }
            }
        }
    }
}
