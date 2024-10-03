package com.randev.kmmgmaps.maps

import cocoapods.GoogleMaps.GMSCameraPosition
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMapViewDelegateProtocol
import com.randev.kmmgmaps.maps.state.GoogleMapsStateImpl
import com.randev.kmmgmaps.network.data.Coordinate
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.darwin.NSObject

/**
 * @author Raihan Arman
 * @date 03/10/24
 */
@OptIn(ExperimentalForeignApi::class)
class IosGoogleMapsDelegate(
    private val stateImpl: GoogleMapsStateImpl
): NSObject(), GMSMapViewDelegateProtocol {
    override fun mapView(mapView: GMSMapView, didChangeCameraPosition: GMSCameraPosition) {
        val coordinate = didChangeCameraPosition.target.useContents {
            Coordinate(
                latitude = latitude,
                longitude = longitude
            )
        }

        val zoom = didChangeCameraPosition.zoom
        stateImpl.saveCameraPosition(cameraCoordinate = CameraCoordinate(coordinate, zoom))
        println("Ampas kuda -> zoom $zoom | coordinate $coordinate")
    }
}