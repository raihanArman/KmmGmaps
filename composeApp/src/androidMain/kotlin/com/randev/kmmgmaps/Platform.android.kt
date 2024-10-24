package com.randev.kmmgmaps

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val isAndroid: Boolean = true


@Composable
actual fun BackPressed(enable: Boolean, handler: () -> Unit) {
    BackHandler(enable, handler)
}