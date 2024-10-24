package com.randev.kmmgmaps

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val isAndroid: Boolean

@Composable
expect fun BackPressed(enable: Boolean, handler: () -> Unit)