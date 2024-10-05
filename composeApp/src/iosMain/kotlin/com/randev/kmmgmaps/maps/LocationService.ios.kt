package com.randev.kmmgmaps.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * @author Raihan Arman
 * @date 04/10/24
 */
@Composable
actual fun rememberLocationService(): LocationService {
    return remember {
        IosLocationService()
    }
}