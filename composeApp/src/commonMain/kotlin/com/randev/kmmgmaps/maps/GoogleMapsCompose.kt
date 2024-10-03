package com.randev.kmmgmaps.maps

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.randev.kmmgmaps.maps.state.GoogleMapsState

/**
 * @author Raihan Arman
 * @date 01/10/24
 */

@Composable
expect fun GoogleMapsCompose(modifier: Modifier, googleMapsState: GoogleMapsState)