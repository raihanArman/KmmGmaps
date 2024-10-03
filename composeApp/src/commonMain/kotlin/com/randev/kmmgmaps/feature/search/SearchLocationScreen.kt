package com.randev.kmmgmaps.feature.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.randev.kmmgmaps.network.data.Place
import com.randev.kmmgmaps.onError
import com.randev.kmmgmaps.onLoading
import com.randev.kmmgmaps.onSuccess

/**
 * @author Raihan Arman
 * @date 01/10/24
 */
@Composable
fun SearchLocation(
    viewModel: SearchLocationViewModel = androidx.lifecycle.viewmodel.compose.viewModel { SearchLocationViewModel() }
) {
    val model by viewModel.stateData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row {
            TextField(
                value = model.query,
                onValueChange = {
                    viewModel.handleIntent(SearchLocationIntent.Query(it))
                },
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier
                .width(12.dp))
            Button(
                onClick = {
                    viewModel.handleIntent(SearchLocationIntent.Search)
                },
                enabled = model.query.isNotEmpty()
            ) {
                Text(text = "Search")
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

@Composable
fun PlaceContent(place: Place) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = place.name,
                style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = place.address,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = place.coordinate.toString()
            )
        }
    }
}