package com.randev.kmmgmaps.feature.reverse

import androidx.lifecycle.viewModelScope
import com.randev.kmmgmaps.base.BaseViewModel
import com.randev.kmmgmaps.network.LocationRepository
import com.randev.kmmgmaps.network.State
import com.randev.kmmgmaps.network.data.Coordinate
import com.randev.kmmgmaps.network.data.Place
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @author Raihan Arman
 * @date 01/10/24
 */

data class ReversedLocationModel(
    val coordinate: Coordinate = Coordinate(),
    val placeState: State<List<Place>> = State.Idle,
    val isReservedButtonEnabled: Boolean = false
)

sealed class ReversedLocationIntent {
    data class Latitude(val lat: Double): ReversedLocationIntent()
    data class Longitude(val long: Double): ReversedLocationIntent()
    data object GetPlaces: ReversedLocationIntent()
}

class ReversedLocationViewModel: BaseViewModel<ReversedLocationModel, ReversedLocationIntent>(
    ReversedLocationModel()
) {
    private val locationRepository = LocationRepository()

    override fun handleIntent(appIntent: ReversedLocationIntent) {
        when(appIntent) {
            ReversedLocationIntent.GetPlaces -> {
                reserved()
            }
            is ReversedLocationIntent.Latitude -> {
                setLatitude(appIntent.lat)
            }
            is ReversedLocationIntent.Longitude -> {
                setLongitude(appIntent.long)
            }
        }
    }

    private fun reserved() = viewModelScope.launch {
        val coordinate = stateData.value.coordinate
        locationRepository.reverseLocation(coordinate)
            .stateIn(this)
            .collectLatest {
                updateModel { model ->
                    model.copy(
                        placeState = it
                    )
                }
            }
    }

    private fun setLongitude(long: Double) {
        updateModel { model ->
            val coordinate = model.coordinate
            model.copy(
                coordinate = coordinate.copy(
                    longitude = long
                ),
                isReservedButtonEnabled = setButtonEnabled()
            )
        }
    }

    private fun setLatitude(lat: Double) {
        updateModel { model ->
            val coordinate = model.coordinate
            model.copy(
                coordinate = coordinate.copy(
                    latitude = lat
                ),
                isReservedButtonEnabled = setButtonEnabled()
            )
        }
    }

    private fun setButtonEnabled(): Boolean {
        val isLatitudeValid = stateData.value.coordinate.latitude.toString().isNotEmpty()
        val isLongitudeValid = stateData.value.coordinate.longitude.toString().isNotEmpty()

        return isLatitudeValid && isLongitudeValid
    }
}