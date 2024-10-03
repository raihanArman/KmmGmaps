package com.randev.kmmgmaps.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.randev.kmmgmaps.maps.state.GoogleMapsState
import com.randev.kmmgmaps.maps.state.GoogleMapsStateImpl
import com.randev.kmmgmaps.maps.state.asImplement
import com.randev.kmmgmaps.network.data.Coordinate

/**
 * @author Raihan Arman
 * @date 01/10/24
 */
@Composable
actual fun GoogleMapsCompose(modifier: Modifier, googleMapsState: GoogleMapsState) {
    val androidCameraPositionState = rememberCameraPositionState()

    val initialCamera by googleMapsState.asImplement().initialCameraCoordinate.collectAsState()
    val moveCamera by googleMapsState.asImplement().moveCameraCoordinate.collectAsState()
    val zoomCamera by googleMapsState.asImplement().zoomCamera.collectAsState()
    val isNeedZoom by googleMapsState.asImplement().isNeedZoom.collectAsState()

    LaunchedEffect(initialCamera) {
        val latLng = LatLng(
            initialCamera.coordinate.latitude,
            initialCamera.coordinate.longitude
        )
        androidCameraPositionState.move(
            CameraUpdateFactory.newLatLngZoom(latLng, initialCamera.zoomWithDefault())
        )
    }

    LaunchedEffect(moveCamera) {
        if (!moveCamera.isZeroCoordinate()) {
            val latLng = LatLng(
                moveCamera.coordinate.latitude,
                moveCamera.coordinate.longitude
            )
            androidCameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(latLng, moveCamera.zoomWithDefault())
            )
        }
    }

    LaunchedEffect(androidCameraPositionState.position) {
        val position = androidCameraPositionState.position
        val coordinate = Coordinate(
            latitude = position.target.latitude,
            longitude = position.target.longitude
        )
        val cameraCoordinate = CameraCoordinate(coordinate, position.zoom)

        val stateImpl = googleMapsState as GoogleMapsStateImpl
        stateImpl.saveCameraPosition(cameraCoordinate)
    }

    LaunchedEffect(zoomCamera) {
        if (isNeedZoom) {
            androidCameraPositionState.animate(
                CameraUpdateFactory.zoomTo(zoomCamera)
            )
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = androidCameraPositionState
    )
}