package com.randev.kmmgmaps.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

/**
 * @author Raihan Arman
 * @date 26/08/24
 */
@Serializable
data object Screen1Route

@Serializable
data object Screen2Route

@Serializable
data object Screen3Route

object Routers {
    const val SCREEN_1 = "screen1"
    const val SCREEN_2 = "screen2/{name}"
    const val SCREEN_3 = "screen3"
}

val LocalNavigationController = staticCompositionLocalOf<NavHostController> {
    error("Error nav controller")
}