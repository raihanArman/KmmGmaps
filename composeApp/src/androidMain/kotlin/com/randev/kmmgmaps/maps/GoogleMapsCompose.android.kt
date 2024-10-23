package com.randev.kmmgmaps.maps

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.randev.kmmgmaps.maps.state.GoogleMapsState
import com.randev.kmmgmaps.maps.state.GoogleMapsStateImpl
import com.randev.kmmgmaps.maps.state.asImplement
import com.randev.kmmgmaps.network.data.Coordinate

/**
 * @author Raihan Arman
 * @date 01/10/24
 */
@Composable
actual fun GoogleMapsCompose(
    modifier: Modifier,
    googleMapsState: GoogleMapsState,
    mapSettings: MapSettings,
    onMarkerClick: (GoogleMapsMarker) -> Unit
) {
    val androidCameraPositionState = rememberCameraPositionState()

    val initialCamera by googleMapsState.asImplement().initialCameraCoordinate.collectAsState()
    val moveCamera by googleMapsState.asImplement().moveCameraCoordinate.collectAsState()
    val zoomCamera by googleMapsState.asImplement().zoomCamera.collectAsState()
    val isNeedZoom by googleMapsState.asImplement().isNeedZoom.collectAsState()

    val markerList by googleMapsState.asImplement().markerList.collectAsState()

    val gestureManager = remember {
        GestureManager()
    }

    val gesture by gestureManager.gesture.collectAsState()

    LaunchedEffect(gesture) {
        googleMapsState.asImplement().setMoveGesture(gesture)
    }

    LaunchedEffect(Unit) {
        googleMapsState.asImplement().setMapLoaded(false)
    }

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
        println("Ampas kuda ->>> MOVE CAMERA $moveCamera")
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

        // Gesture Manager
        gestureManager.setCoordinate(coordinate)
    }

    LaunchedEffect(zoomCamera) {
        if (isNeedZoom) {
            androidCameraPositionState.animate(
                CameraUpdateFactory.zoomTo(zoomCamera)
            )
        }
    }

    LaunchedEffect(androidCameraPositionState.isMoving) {
        val isGestureReason = androidCameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE
        val isMoveFromInput = androidCameraPositionState.isMoving && isGestureReason

        gestureManager.setIsMoving(isMoveFromInput)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = androidCameraPositionState,
        onMapLoaded = {
            googleMapsState.asImplement().setMapLoaded(true)
        },
        properties = MapProperties(
            isMyLocationEnabled = mapSettings.myLocationEnabled
        ),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = mapSettings.myLocationButtonEnabled,
            compassEnabled = mapSettings.composeEnabled
        ),
        contentPadding = PaddingValues(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {
        for (marker in markerList) {
            val markerState = rememberMarkerState(
                position = LatLng(
                    marker.coordinate.latitude,
                    marker.coordinate.longitude
                )
            )
            Marker(
                state = markerState,
                title = marker.title,
                onClick = { androidMarker ->
                    val googleMapsMarker = markerList.find {
                        it.coordinate.toString() == androidMarker.position
                            .asString()
                    }

                    if (googleMapsMarker != null) {
                        onMarkerClick.invoke(googleMapsMarker)
                    }
                    true
                }
            )
        }
    }
}

fun LatLng.asString(): String {
    return "$latitude,$longitude"
}