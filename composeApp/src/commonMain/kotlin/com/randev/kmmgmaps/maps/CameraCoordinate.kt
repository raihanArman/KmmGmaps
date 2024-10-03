package com.randev.kmmgmaps.maps

import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize
import com.randev.kmmgmaps.network.data.Coordinate

/**
 * @author Raihan Arman
 * @date 02/10/24
 */
@Parcelize
data class CameraCoordinate(
    val coordinate: Coordinate = Coordinate(),
    val zoom: Float? = null,
    val initializer: Int? = null
): Parcelable {
    fun zoomWithDefault(): Float {
        return zoom ?: 16f
    }
}

fun CameraCoordinate.isZeroCoordinate(): Boolean {
    return coordinate.latitude == 0.0 && coordinate.longitude == 0.0
}
