package com.painandpanic.blossombuddy.util.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable

@Composable
@NonRestartableComposable
fun EventEffect(
    event: BasicStateEvent,
    onConsumed: () -> Unit = {},
    action: suspend () -> Unit
) {
    LaunchedEffect(key1 = event, key2 = onConsumed) {
        if (event is BasicStateEvent.Triggered) {
            action()
            onConsumed()
        }
    }
}

@Composable
@NonRestartableComposable
fun <T>EventEffect(
    event: StateEventWithContent<T>,
    onCaptured: () -> Unit,
    action: suspend (T) -> Unit
) {
    LaunchedEffect(key1 = event, key2 = onCaptured) {
        if (event is StateEventWithContent.Triggered<T>) {
            action(event.content)
            onCaptured()
        }
    }
}