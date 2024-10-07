package com.randev.kmmgmaps.feature.googlemaps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.randev.kmmgmaps.maps.CameraCoordinate
import com.randev.kmmgmaps.maps.GoogleMapsCompose
import com.randev.kmmgmaps.maps.GoogleMapsMarker
import com.randev.kmmgmaps.maps.MapSettings
import com.randev.kmmgmaps.maps.rememberLocationService
import com.randev.kmmgmaps.maps.state.rememberGoogleMapsState
import com.randev.kmmgmaps.navigation.appyx.LocalNavigator
import com.randev.kmmgmaps.navigation.appyx.NavTarget
import com.randev.kmmgmaps.network.data.Coordinate

/**
 * @author Raihan Arman
 * @date 01/10/24
 */
val pamulangCoordinate = Coordinate(-5.151604, 119.452925)
val makassar = Coordinate(-5.124287, 119.489632)
@Composable
fun GoogleMapsScreen() {
    val navigator = LocalNavigator.current
    val locationService = rememberLocationService()

    val googleMapsState = rememberGoogleMapsState(
        CameraCoordinate(
            coordinate = makassar,
            zoom = 16f
        )
    )

//    val savedCameraCoordinate by googleMapsState.cameraCoordinate.collectAsState()
//
//    LaunchedEffect(savedCameraCoordinate) {
//        println("Ampas kuda -> ${savedCameraCoordinate.coordinate}")
//    }

    val isMapLoaded by googleMapsState.mapLoaded.collectAsState()
    val myLocation by locationService.myLocation.collectAsState()

    LaunchedEffect(myLocation, isMapLoaded) {
        if (isMapLoaded) {
            googleMapsState.animatedCamera(
                cameraCoordinate = CameraCoordinate(
                    coordinate = myLocation
                )
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMapsCompose(
            modifier = Modifier.fillMaxSize(),
            googleMapsState = googleMapsState,
            mapSettings = MapSettings(
                myLocationEnabled = myLocation.latitude != 0.0,
                composeEnabled = true,
                myLocationButtonEnabled = true
            )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    googleMapsState.animatedCamera(
                        CameraCoordinate(
                            coordinate = pamulangCoordinate,
                            zoom = 16f
                        )
                    )

                    googleMapsState.addMarker(
                        GoogleMapsMarker(
                            coordinate = pamulangCoordinate,
                            title = "Pamulang"
                        )
                    )
                }
            ) {
                Text("Move Camera to pamulang and add marker")
            }

            Button(
                onClick = {
                    googleMapsState.animatedCamera(
                        CameraCoordinate(
                            coordinate = makassar,
                        )
                    )

                    googleMapsState.addMarker(
                        GoogleMapsMarker(
                            coordinate = makassar,
                            title = "Makassar"
                        )
                    )
                }
            ) {
                Text("Move Camera to makassar and add marker")
            }

            Button(
                onClick = {
                    googleMapsState.removeMarker(
                        GoogleMapsMarker(
                            coordinate = makassar,
                            title = "Makassar"
                        )
                    )
                }
            ) {
                Text("Remove makassar marker")
            }

            Button(
                onClick = {
                    googleMapsState.zoomIn()
                },
            ) {
                Text("Zoom In")
            }
            Button(
                onClick = {
                    googleMapsState.zoomOut()
                },
            ) {
                Text("Zoom Out")
            }

            Button(
                onClick = {
                    navigator.navigate(NavTarget.ReservedLocation)
                },
            ) {
                Text("Go To Reserved Location")
            }
        }
    }
}