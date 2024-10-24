package com.randev.kmmgmaps

import androidx.compose.runtime.Composable
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual val isAndroid: Boolean = false

@Composable
actual fun BackPressed(enable: Boolean, handler: () -> Unit) {}