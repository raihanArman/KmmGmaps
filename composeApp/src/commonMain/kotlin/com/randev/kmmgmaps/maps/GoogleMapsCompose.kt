package com.randev.kmmgmaps.maps

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.randev.kmmgmaps.maps.state.GoogleMapsState

/**
 * @author Raihan Arman
 * @date 01/10/24
 */

@Composable
expect fun GoogleMapsCompose(
    modifier: Modifier,
    googleMapsState: GoogleMapsState,
    mapSettings: MapSettings = MapSettings(),
    onMarkerClick: (GoogleMapsMarker) -> Unit = {}
)

val DefaultMapsPadding: PaddingValues
    @Composable
    get() = PaddingValues(
        top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
        bottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding() -
                WindowInsets.systemGestures.asPaddingValues().calculateBottomPadding()
    )