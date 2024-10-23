package com.randev.kmmgmaps.maps.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize
import com.randev.kmmgmaps.maps.CameraCoordinate
import com.randev.kmmgmaps.maps.GoogleMapsMarker
import com.randev.kmmgmaps.maps.MoveGesture
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Raihan Arman
 * @date 02/10/24
 */
interface GoogleMapsState {
    val cameraCoordinate: StateFlow<CameraCoordinate>
    val markerList: StateFlow<List<GoogleMapsMarker>>

    val mapLoaded: StateFlow<Boolean>

    val gesture: StateFlow<MoveGesture>

    fun animatedCamera(cameraCoordinate: CameraCoordinate)
    fun zoomIn()
    fun zoomOut()

    fun addMarker(marker: GoogleMapsMarker)
    fun removeMarker(marker: GoogleMapsMarker)
    fun removeAllMarker()

    companion object {
        val Saver: Saver<GoogleMapsState, GoogleMapsStateSaveable> = Saver(
            save = {
                val cameraCoordinate = it.cameraCoordinate.value
                val markerList = it.markerList.value

                GoogleMapsStateSaveable(
                    cameraCoordinate, markerList
                )
            },
            restore = {
                val initialCameraCoordinate = it.cameraCoordinate
                val initialMarkerList = it.markerList
                GoogleMapsStateImpl(
                    _initialCameraCoordinate = initialCameraCoordinate,
                    _initialMarkerList = initialMarkerList
                )
            }
        )
    }

    @Parcelize
    data class GoogleMapsStateSaveable(
        val cameraCoordinate: CameraCoordinate,
        val markerList: List<GoogleMapsMarker>
    ): Parcelable
}

expect fun buildGoogleMapsState(initialCameraCoordinate: CameraCoordinate): GoogleMapsState

@Composable
fun rememberGoogleMapsState(initialCameraCoordinate: CameraCoordinate): GoogleMapsState {
    return rememberSaveable(saver = GoogleMapsState.Saver) {
        GoogleMapsStateImpl(initialCameraCoordinate, emptyList())
    }
}