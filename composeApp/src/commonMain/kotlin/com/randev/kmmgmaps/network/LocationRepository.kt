package com.randev.kmmgmaps.network

import com.randev.kmmgmaps.SecretConfig
import com.randev.kmmgmaps.base.BaseRepository
import com.randev.kmmgmaps.network.data.Coordinate
import com.randev.kmmgmaps.network.data.Place
import com.randev.kmmgmaps.network.response.PlaceResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author Raihan Arman
 * @date 01/10/24
 */
class LocationRepository: BaseRepository() {
    fun searchLocation(query: String, coordinate: Coordinate): Flow<State<List<Place>>> {
        val apikey = SecretConfig.HERE_API_KEY
        return suspend {
            getHttpResponse("https://discover.search.hereapi.com/v1/discover?at=$coordinate&q=$query&apiKey=$apikey")
        }.reduce<PlaceResponse, List<Place>> { response ->
            val data = PlaceMapper.mapResponseToPlaces(response)
            State.Success(data)
        }
    }

    fun reverseLocation(coordinate: Coordinate): Flow<State<List<Place>>> {
        val apikey = SecretConfig.HERE_API_KEY
        return suspend {
            getHttpResponse("https://revgeocode.search.hereapi.com/v1/revgeocode?at=$coordinate&limit=3&lang=en-US&apiKey=$apikey")
        }.reduce<PlaceResponse, List<Place>> { response ->
            val data = PlaceMapper.mapResponseToPlaces(response)
            State.Success(data)
        }
    }
}