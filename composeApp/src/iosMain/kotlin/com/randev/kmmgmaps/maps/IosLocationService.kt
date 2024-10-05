package com.randev.kmmgmaps.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.randev.kmmgmaps.network.data.Coordinate
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Raihan Arman
 * @date 04/10/24
 */
class IosLocationService : LocationService {
    override val myLocation: StateFlow<Coordinate>
        get() = TODO("Not yet implemented")

    override suspend fun getMyLocation() {
        TODO("Not yet implemented")
    }

}