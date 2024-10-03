package com.randev.kmmgmaps.feature.reverse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.randev.kmmgmaps.feature.search.PlaceContent
import com.randev.kmmgmaps.feature.search.SearchLocationIntent
import com.randev.kmmgmaps.onError
import com.randev.kmmgmaps.onLoading
import com.randev.kmmgmaps.onSuccess

/**
 * @author Raihan Arman
 * @date 01/10/24
 */
@Composable
fun ReversedLocationScreen(
    viewModel: ReversedLocationViewModel = androidx.lifecycle.viewmodel.compose.viewModel { ReversedLocationViewModel() },
) {
    val model by viewModel.stateData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row {
            TextField(
                value = model.coordinate.latitude.toString(),
                onValueChange = {
                    viewModel.handleIntent(ReversedLocationIntent.Latitude(it.toDoubleOrNull() ?: 0.0))
                },
                modifier = Modifier
                    .weight(1f),
                label = {
                    Text("Latitude")
                }
            )
            Spacer(modifier = Modifier
                .width(12.dp))

            TextField(
                value = model.coordinate.longitude.toString(),
                onValueChange = {
                    viewModel.handleIntent(ReversedLocationIntent.Longitude(it.toDoubleOrNull() ?: 0.0))
                },
                modifier = Modifier
                    .weight(1f),
                label = {
                    Text("Longitude")
                }
            )
            Spacer(modifier = Modifier
                .width(12.dp))
            Button(
                onClick = {
                    viewModel.handleIntent(ReversedLocationIntent.GetPlaces)
                },
                enabled = model.isReservedButtonEnabled
            ) {
                Text(text = "Get Place")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        with(model.placeState) {
            onLoading {

            }

            onSuccess { places ->
                LazyColumn {
                    items(places) {
                        PlaceContent(it)
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

            onError {
                Text(it.message.orEmpty())
            }
        }
    }
}