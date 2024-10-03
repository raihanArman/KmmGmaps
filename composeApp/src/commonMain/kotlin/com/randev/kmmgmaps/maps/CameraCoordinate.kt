package com.randev.kmmgmaps.maps

import com.randev.kmmgmaps.network.data.Coordinate

/**
 * @author Raihan Arman
 * @date 02/10/24
 */
data class CameraCoordinate(
    val coordinate: Coordinate = Coordinate(),
    val zoom: Float? = null,
    val initializer: Int? = null
) {
    fun zoomWithDefault(): Float {
        return zoom ?: 16f
    }
}

fun CameraCoordinate.isZeroCoordinate(): Boolean {
    return coordinate.latitude == 0.0 && coordinate.longitude == 0.0
}
