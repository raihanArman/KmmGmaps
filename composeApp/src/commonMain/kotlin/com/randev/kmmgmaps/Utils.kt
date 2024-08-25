package com.randev.kmmgmaps

import androidx.compose.runtime.Composable
import com.randev.kmmgmaps.network.State

/**
 * @author Raihan Arman
 * @date 24/08/24
 */
@Composable
fun <T> State<T>.onIdle(block: @Composable () -> Unit) {
    when(val state = this) {
        is State.Idle -> {
            block.invoke()
        }
        else -> {}
    }
}

@Composable
fun <T> State<T>.onLoading(block: @Composable () -> Unit) {
    when(val state = this) {
        is State.Loading -> {
            block.invoke()
        }
        else -> {}
    }
}


@Composable
fun <T> State<T>.onSuccess(block: @Composable (T) -> Unit) {
    when(val state = this) {
        is State.Success -> {
            block.invoke(state.data)
        }
        else -> {}
    }
}

@Composable
fun <T> State<T>.onError(block: @Composable (Throwable) -> Unit) {
    when(val state = this) {
        is State.Failure -> {
            block.invoke(state.throwable)
        }
        else -> {}
    }
}