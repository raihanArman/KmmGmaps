package com.randev.kmmgmaps.maps

import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize
import com.randev.kmmgmaps.network.data.Coordinate

/**
 * @author Raihan Arman
 * @date 03/10/24
 */
@Parcelize
data class GoogleMapsMarker(
    val coordinate: Coordinate,
    val title: String? = null
): Parcelable
