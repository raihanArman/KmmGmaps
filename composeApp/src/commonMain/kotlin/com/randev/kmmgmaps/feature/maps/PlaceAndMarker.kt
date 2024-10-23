package com.randev.kmmgmaps.feature.maps

import com.randev.kmmgmaps.maps.GoogleMapsMarker
import com.randev.kmmgmaps.network.data.Coordinate
import com.randev.kmmgmaps.network.data.Place

/**
 * @author Raihan Arman
 * @date 23/10/24
 */
data class PlaceAndMarker(
    val place: Place = Place.Empty,
    val marker: GoogleMapsMarker = GoogleMapsMarker(
        coordinate = Coordinate()
    )
)
