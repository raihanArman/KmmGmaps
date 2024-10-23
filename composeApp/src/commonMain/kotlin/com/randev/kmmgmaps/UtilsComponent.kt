package com.randev.kmmgmaps

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity

/**
 * @author Raihan Arman
 * @date 23/10/24
 */
@Composable
fun isKeyboardOpen(): State<Boolean> {
    val isKeyboardOpen = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isKeyboardOpen)
}