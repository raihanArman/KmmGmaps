package com.randev.kmmgmaps.maps

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.randev.kmmgmaps.network.data.Coordinate
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Raihan Arman
 * @date 04/10/24
 */
interface LocationService {
    val myLocation: StateFlow<Coordinate>
    suspend fun getMyLocation()
}

@Composable
expect fun rememberLocationService(): LocationService