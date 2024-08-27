package com.randev.kmmgmaps.navigation.appyx

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * @author Raihan Arman
 * @date 27/08/24
 */
interface Navigator {
    fun navigate(navTarget: NavTarget)
    fun back()
}

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("navigator to provided")
}