package com.randev.kmmgmaps.maps

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize

/**
 * @author Raihan Arman
 * @date 07/10/24
 */

data class MapSettings(
    val myLocationEnabled: Boolean = false,
    val myLocationButtonEnabled: Boolean = false,
    val composeEnabled: Boolean = false,
    val padding: PaddingValues = PaddingValues()
)