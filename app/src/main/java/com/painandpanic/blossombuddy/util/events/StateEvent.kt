package com.painandpanic.blossombuddy.util.events

sealed interface BasicStateEvent {
    data object Captured : BasicStateEvent
    data object Triggered : BasicStateEvent
}

sealed interface StateEventWithContent<T> {
    data class Captured<T>(val content: T) : StateEventWithContent<T>
    data class Triggered<T>(val content: T) : StateEventWithContent<T>
}
