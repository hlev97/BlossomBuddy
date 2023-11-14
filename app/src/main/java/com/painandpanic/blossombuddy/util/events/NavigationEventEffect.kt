package com.painandpanic.blossombuddy.util.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable

@Composable
@NonRestartableComposable
fun NavigationEventEffect(
    event: BasicStateEvent,
    onConsumed: () -> Unit = {},
    action: suspend () -> Unit
) {
    LaunchedEffect(key1 = event, key2 = onConsumed) {
        if (event is BasicStateEvent.Triggered) {
            onConsumed()
            action()
        }
    }
}

@Composable
@NonRestartableComposable
fun <T> NavigationEventEffect(event: StateEventWithContent<T>, onConsumed: () -> Unit, action: suspend (T) -> Unit) {
    LaunchedEffect(key1 = event, key2 = onConsumed) {
        if (event is StateEventWithContent.Triggered<T>) {
            onConsumed()
            action(event.content)
        }
    }
}