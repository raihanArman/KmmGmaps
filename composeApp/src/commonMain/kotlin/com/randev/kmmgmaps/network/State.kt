package com.randev.kmmgmaps.network

/**
 * @author Raihan Arman
 * @date 24/08/24
 */
sealed class State <out T> {
    data object Idle: State<Nothing>()
    data object Loading: State<Nothing>()
    data class Success<T>(val data: T): State<T>()
    data class Failure(val throwable: Throwable): State<Nothing>()
}