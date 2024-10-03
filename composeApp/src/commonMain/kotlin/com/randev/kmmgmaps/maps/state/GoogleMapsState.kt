package com.randev.kmmgmaps.maps.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.randev.kmmgmaps.maps.CameraCoordinate
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Raihan Arman
 * @date 02/10/24
 */
interface GoogleMapsState {
    val cameraCoordinate: StateFlow<CameraCoordinate>

    fun animatedCamera(cameraCoordinate: CameraCoordinate)
    fun zoomIn()
    fun zoomOut()

    companion object {
        val Saver: Saver<GoogleMapsState, CameraCoordinate> = Saver(
            save = {
                it.cameraCoordinate.value
            },
            restore = {
                buildGoogleMapsState(it)
            }
        )
    }
}

expect fun buildGoogleMapsState(initialCameraCoordinate: CameraCoordinate): GoogleMapsState

@Composable
fun rememberGoogleMapsState(initialCameraCoordinate: CameraCoordinate): GoogleMapsState {
    return rememberSaveable(saver = GoogleMapsState.Saver) {
        buildGoogleMapsState(initialCameraCoordinate)
    }
}