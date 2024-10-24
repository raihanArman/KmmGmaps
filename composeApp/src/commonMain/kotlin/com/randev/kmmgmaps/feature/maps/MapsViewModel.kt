package com.randev.kmmgmaps.feature.maps

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.randev.kmmgmaps.base.BaseViewModel
import com.randev.kmmgmaps.maps.GoogleMapsMarker
import com.randev.kmmgmaps.network.LocationRepository
import com.randev.kmmgmaps.network.State
import com.randev.kmmgmaps.network.data.Coordinate
import com.randev.kmmgmaps.network.data.Place
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @author Raihan Arman
 * @date 14/10/24
 */
data class MapsState(
    val query: String = "",
    val placeState: State<List<Place>> = State.Idle,
    val selectedPlace: Place = Place.Empty,
    val isShowSearch: Boolean = false,
    val myCoordinate: Coordinate = Coordinate(),
    val mapBottomPadding: Dp = 0.dp
)

sealed class MapsIntent {
    data class SetQuery(val query: String): MapsIntent()
    data class SetSelectedPlace(val place: Place): MapsIntent()
    data class SetIsShowSearch(val isShowSearch: Boolean): MapsIntent()
    data object ObserverQuery: MapsIntent()
    data class SetMyCoordinate(val coordinate: Coordinate): MapsIntent()
    data class SetSelectedMarker(val marker: GoogleMapsMarker): MapsIntent()
    data object SetPlacesClear: MapsIntent()
    data class SetBottomMapPadding(val padding: Dp): MapsIntent()
}

class MapsViewModel: BaseViewModel<MapsState, MapsIntent>(
    MapsState()
) {

    private val locationRepository = LocationRepository()

    override fun handleIntent(appIntent: MapsIntent) {
        when (appIntent) {
            is MapsIntent.SetIsShowSearch -> {
                setIsShowSearch(appIntent.isShowSearch)
            }
            is MapsIntent.SetQuery -> {
                setQuerySearch(appIntent.query)
            }
            is MapsIntent.SetSelectedPlace -> {
                setSelectedPlace(appIntent.place)
            }

            is MapsIntent.SetMyCoordinate -> {
                setMyCoordinate(appIntent.coordinate)
            }

            MapsIntent.ObserverQuery -> {
                observerQuery()
            }

            is MapsIntent.SetSelectedMarker -> {
                setSelectedMarker(appIntent.marker)
            }

            MapsIntent.SetPlacesClear ->  {
                restartPlaceState()
            }

            is MapsIntent.SetBottomMapPadding -> {
                setSelectedMapPadding(appIntent.padding)
            }
        }
    }

    private fun setSelectedMapPadding(padding: Dp) {
        updateModel {
            it.copy(
                mapBottomPadding = padding
            )
        }
    }

    private fun setSelectedMarker(marker: GoogleMapsMarker) {
        val selectedPlace = stateData.value.placeState
        if (selectedPlace is State.Success) {
            val places = selectedPlace.data
            val place = places.find { marker.coordinate.toString() == it.coordinate.toString() }

            if (place != null) {
                setSelectedPlace(place)
            }
        }
    }

    private fun observerQuery() = viewModelScope.launch {
        stateData
            .map { it.query }
            .debounce(2000)
            .stateIn(this)
            .collectLatest {
                if (it.length > 2) {
                    searchPlace()
                }

                if (it.isEmpty()) {
                    restartPlaceState()
                }
            }
    }

    private fun setMyCoordinate(coordinate: Coordinate) {
        updateModel {
            it.copy(
                myCoordinate = coordinate
            )
        }
    }

    private fun setIsShowSearch(isShowSearch: Boolean) {
        updateModel {
            it.copy(
                isShowSearch = isShowSearch
            )
        }
    }

    private fun setSelectedPlace(place: Place) {
        updateModel {
            it.copy(
                selectedPlace = place
            )
        }
    }

    private fun setQuerySearch(query: String) {
        updateModel {
            it.copy(
                query = query
            )
        }
    }

    private fun searchPlace() = viewModelScope.launch {
        val query = stateData.value.query
        val coordinate = stateData.value.myCoordinate
        locationRepository
            .searchLocation(query, coordinate)
            .stateIn(this)
            .collectLatest { placeState ->
                updateModel {
                    it.copy(
                        placeState = placeState
                    )
                }
            }
    }

    private fun restartPlaceState() {
        updateModel {
            MapsState().copy(
                myCoordinate = it.myCoordinate
            )
        }
    }

}