package com.randev.kmmgmaps.feature.search

import androidx.lifecycle.viewModelScope
import com.randev.kmmgmaps.base.BaseViewModel
import com.randev.kmmgmaps.network.LocationRepository
import com.randev.kmmgmaps.network.State
import com.randev.kmmgmaps.network.data.Coordinate
import com.randev.kmmgmaps.network.data.Place
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @author Raihan Arman
 * @date 01/10/24
 */
data class SearchLocationModel(
    val query: String = "",
    val placeState: State<List<Place>> = State.Idle
)

sealed class SearchLocationIntent {
    data class Query(val query: String): SearchLocationIntent()
    data object Search: SearchLocationIntent()
}

class SearchLocationViewModel: BaseViewModel<SearchLocationModel, SearchLocationIntent>(
    SearchLocationModel()
) {
    private val locationRepository = LocationRepository()

    override fun handleIntent(appIntent: SearchLocationIntent) {
        when(appIntent) {
            is SearchLocationIntent.Query -> {
                sendQuery(appIntent.query)
            }
            SearchLocationIntent.Search -> {
                searchLocation()
            }
        }
    }

    private fun searchLocation() = viewModelScope.launch {
        val query = stateData.value.query
        locationRepository.searchLocation(
            query = query,
            coordinate = Coordinate(
                latitude = -5.156899,
                longitude = 119.449938
            )
        ).stateIn(this).collect {
            updateModel { model ->
                model.copy(
                    placeState = it
                )
            }
        }
    }

    private fun sendQuery(query: String) {
        updateModel { model ->
            model.copy(
                query = query
            )
        }
    }

}