package com.randev.kmmgmaps.network

import com.randev.kmmgmaps.network.data.Place
import com.randev.kmmgmaps.network.response.PlaceResponse
import com.randev.kmmgmaps.network.data.Coordinate

/**
 * @author Raihan Arman
 * @date 01/10/24
 */
object PlaceMapper {
    fun mapResponseToPlaces(response: PlaceResponse): List<Place> {
        return response.items?.map {
            mapItem(it)
        } ?: emptyList()
    }

    private fun mapItem(item: PlaceResponse.Item?): Place {
        return Place(
            id = item?.id.orEmpty(),
            name = item?.title.orEmpty(),
            address = item?.address?.let {
                "${it.label} - ${it.street}, ${it.city}"
            }.orEmpty(),
            coordinate = item?.position?.let {
                Coordinate(
                    latitude = it.lat ?: 0.0,
                    longitude = it.lng ?: 0.0
                )
            } ?: Coordinate(),
            distance = item?.distance ?: -1
        )
    }
}