package com.randev.kmmgmaps.maps.state

import com.randev.kmmgmaps.maps.CameraCoordinate

/**
 * @author Raihan Arman
 * @date 02/10/24
 */
actual fun buildGoogleMapsState(initialCameraCoordinate: CameraCoordinate): GoogleMapsState {
    return GoogleMapsStateImpl(initialCameraCoordinate)
}