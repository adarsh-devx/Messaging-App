package dev.floww.android.features.welcome

import dev.floww.android.core.mvi.EventDrivenViewModel

class WelcomeScreenViewModel(
    initialState: WelcomeScreenState = WelcomeScreenState()
) : EventDrivenViewModel<WelcomeScreenEvents, WelcomeScreenState, WelcomeScreenActions>(initialState) {

    override suspend fun handleEvent(event: WelcomeScreenEvents) {
        when (event) {
            is WelcomeScreenEvents.FeatureSelected -> {
                setState { it.copy(activeFeatureIndex = event.index) }
            }
            is WelcomeScreenEvents.GetStartedClicked -> {
                sendAction(WelcomeScreenActions.NavigateToAuth(isRegister = false))
            }
            is WelcomeScreenEvents.SignInClicked -> {
                sendAction(WelcomeScreenActions.NavigateToAuth(isRegister = false))
            }
            is WelcomeScreenEvents.InfoClicked -> {
                sendAction(WelcomeScreenActions.ShowInfoDialog)
            }
        }
    }
}
