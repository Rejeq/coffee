package com.rejeq.sws.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> NavEventSideEffect(navigation: Flow<T>, handler: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(navigation) {
        navigation
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect(handler)
    }
}
