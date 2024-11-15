package com.randev.kmmgmaps.feature.maps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.randev.kmmgmaps.BackPressed
import com.randev.kmmgmaps.authentication.rememberGoogleAuthentication
import com.randev.kmmgmaps.feature.maps.component.CarouselHeight
import com.randev.kmmgmaps.feature.maps.component.CarouselPlaces
import com.randev.kmmgmaps.feature.maps.component.ItemPlace
import com.randev.kmmgmaps.feature.maps.component.SearchBarPlace
import com.randev.kmmgmaps.isAndroid
import com.randev.kmmgmaps.isKeyboardOpen
import com.randev.kmmgmaps.maps.CameraCoordinate
import com.randev.kmmgmaps.maps.DefaultMapsPadding
import com.randev.kmmgmaps.maps.GoogleMapsCompose
import com.randev.kmmgmaps.maps.GoogleMapsMarker
import com.randev.kmmgmaps.maps.LocationService
import com.randev.kmmgmaps.maps.MapSettings
import com.randev.kmmgmaps.maps.rememberLocationService
import com.randev.kmmgmaps.maps.state.rememberGoogleMapsState
import com.randev.kmmgmaps.navigation.appyx.LocalNavigator
import com.randev.kmmgmaps.network.State
import com.randev.kmmgmaps.network.data.Place
import com.randev.kmmgmaps.onError
import com.randev.kmmgmaps.onLoading
import com.randev.kmmgmaps.onSuccess
import kmmgooglemaps.composeapp.generated.resources.Res
import kmmgooglemaps.composeapp.generated.resources.ic_pin_marker
import kmmgooglemaps.composeapp.generated.resources.ic_search
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

/**
 * @author Raihan Arman
 * @date 14/10/24
 */
@Composable
fun MapsScreen(
    viewModel: MapsViewModel = viewModel { MapsViewModel() }
) {
    val mapsState = rememberGoogleMapsState(
        initialCameraCoordinate = CameraCoordinate(
            zoom = 16f
        )
    )

    val locationService = rememberLocationService()

    val isMapHasLoaded by mapsState.mapLoaded.collectAsState()
    val myLocation by locationService.myLocation.collectAsState()
    val model by viewModel.stateData.collectAsState()
    val navigator = LocalNavigator.current

    val googleAuthentication = rememberGoogleAuthentication()
    val user by googleAuthentication.user

    LaunchedEffect(Unit) {
        locationService.getMyLocation()
    }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(MapsIntent.ObserverQuery)
    }

    LaunchedEffect(myLocation) {
        viewModel.handleIntent(MapsIntent.SetMyCoordinate(myLocation))
    }

    LaunchedEffect(myLocation, isMapHasLoaded) {
        if (isMapHasLoaded) {
            mapsState.animatedCamera(
                cameraCoordinate = CameraCoordinate(
                    coordinate = myLocation
                )
            )
        }
    }

    LaunchedEffect(model.selectedPlace) {
        if (model.selectedPlace != Place.Empty) {
            println("Ampas kuda -> title ${model.selectedPlace.name}")
            mapsState.animatedCamera(
                cameraCoordinate = CameraCoordinate(
                    coordinate = model.selectedPlace.coordinate, zoom = 17f
                )
            )

            mapsState.setSelectedMarkerByCoordinate(model.selectedPlace.coordinate)
        }
    }

    val places by remember(model.placeState) {
        derivedStateOf {
            val placeState = model.placeState
            if (placeState is State.Success) {
                placeState.data
            } else {
                emptyList()
            }
        }
    }

    BackPressed(true) {
        when {
            model.isShowSearch -> {
                viewModel.handleIntent(
                    MapsIntent.SetIsShowSearch(false)
                )
            }
            places.isNotEmpty() -> {
                viewModel.handleIntent(
                    MapsIntent.SetPlacesClear
                )
                mapsState.removeAllMarker()
            }
            else -> {
                navigator.back()
            }

        }
    }

    val imePadding by rememberUpdatedState(WindowInsets.ime.asPaddingValues().calculateBottomPadding())
    val systemGesture by rememberUpdatedState(WindowInsets.systemGestures.asPaddingValues().calculateBottomPadding())
    val isKeyboardOpen by isKeyboardOpen()
    LaunchedEffect(places, imePadding, systemGesture, isKeyboardOpen) {
        val platformSystemGesture = if (imePadding > 0.dp) {
            systemGesture
        } else {
            if (isAndroid) {
                0.dp
            } else {
                systemGesture
            }
        }

        val bottomMapsPadding = when {
            isKeyboardOpen -> imePadding - platformSystemGesture
            places.isNotEmpty() -> (CarouselHeight + 16.dp) - platformSystemGesture
            else -> 0.dp
        }

        viewModel.handleIntent(
            MapsIntent.SetBottomMapPadding(bottomMapsPadding)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        GoogleMapsCompose(
            modifier = Modifier
                .fillMaxSize(),
            googleMapsState = mapsState,
            mapSettings = MapSettings(
                myLocationEnabled = myLocation.latitude != 0.0,
                composeEnabled = true,
                padding = PaddingValues(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    bottom = model.mapBottomPadding
                )
            ),
            onMarkerClick = { marker ->
                viewModel.handleIntent(MapsIntent.SetSelectedMarker(marker))
            }
        )

        CarouselPlaces(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            places = places,
            selectedPlace = model.selectedPlace,
            onPageChange = { place ->
                viewModel.handleIntent(
                    MapsIntent.SetSelectedPlace(place)
                )
            }
        )

        Box(
            modifier = Modifier
        ) {
            SearchBarPlace(
                value = model.query,
                photoUrl = user?.photoUrl,
                onEditValue = {
                    viewModel.handleIntent(
                        MapsIntent.SetQuery(it)
                    )
                    viewModel.handleIntent(
                        MapsIntent.SetIsShowSearch(true)
                    )
                },
                isShowSearch = model.isShowSearch,
                onDoneEdit = {
                    viewModel.handleIntent(
                        MapsIntent.SetIsShowSearch(false)
                    )
                },
                isPlaceNotEmpty = places.isNotEmpty(),
                onBackButtonClick = {
                    if (!model.isShowSearch) {
                        // clear state
                        viewModel.handleIntent(
                            MapsIntent.SetPlacesClear
                        )
                        mapsState.removeAllMarker()
                    } else {
                        viewModel.handleIntent(
                            MapsIntent.SetIsShowSearch(false)
                        )
                    }
                },
                content = { keyboardController ->
                    with(model.placeState) {
                        onLoading {
                            Spacer(Modifier
                                .height(10.dp))
                            CircularProgressIndicator()
                        }
                        onError {
                            Text(text = it.message.orEmpty(), color = Color.Red)
                        }
                        onSuccess { places ->
                            LaunchedEffect(places) {
                                for (place in places) {
                                    mapsState.addMarker(
                                        marker = GoogleMapsMarker(
                                            coordinate = place.coordinate,
                                            title = place.name
                                        )
                                    )
                                }
                            }
                            for (place in places) {
                                ItemPlace(place) {
                                    keyboardController?.hide()
                                    viewModel.handleIntent(
                                        MapsIntent.SetIsShowSearch(false)
                                    )
                                    viewModel.handleIntent(
                                        MapsIntent.SetSelectedPlace(place)
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}