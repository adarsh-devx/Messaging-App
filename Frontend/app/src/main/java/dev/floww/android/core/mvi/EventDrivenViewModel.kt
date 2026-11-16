package dev.floww.android.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Signal-grade EventDrivenViewModel pattern.
 * Decouples state changes from UI events through serialized Kotlin Channels.
 */
abstract class EventDrivenViewModel<Event : Any, State : Any, Action : Any>(
    initialState: State
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<Action>(extraBufferCapacity = 64)
    val actions: SharedFlow<Action> = _actions.asSharedFlow()

    private val eventChannel = Channel<Event>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            eventChannel.consumeAsFlow().collect { event ->
                handleEvent(event)
            }
        }
    }

    fun onEvent(event: Event) {
        eventChannel.trySend(event)
    }

    protected abstract suspend fun handleEvent(event: Event)

    protected fun setState(reducer: (State) -> State) {
        _state.update(reducer)
    }

    protected fun sendAction(action: Action) {
        viewModelScope.launch {
            _actions.emit(action)
        }
    }
}
