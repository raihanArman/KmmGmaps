package com.randev.kmmgmaps.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.randev.kmmgmaps.network.data.Coordinate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * @author Raihan Arman
 * @date 04/10/24
 */


typealias GMSLocationService = LocationServices

class AndroidLocationService(
    private val context: Context
): LocationService {
    private val _myLocation: MutableStateFlow<Coordinate> = MutableStateFlow(Coordinate())
    override val myLocation: StateFlow<Coordinate>
        get() = _myLocation

    var isGranted = false

    private val permission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var launcher : ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>

    val fusedLocationProviderLocation = GMSLocationService.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getMyLocation() {
        println("Ampas kuda ->>>>>> getMyLocation")
        if (isGranted) {
            // get location
            getLastLocation()
        } else {
            // request permission
            startRequestPermission()
        }
    }

    fun requestPermission() {
        if (!isGranted) {
            startRequestPermission()
        }
    }

    private fun startRequestPermission() {
        launcher.launch(permission)
    }

    fun setLauncher(launcher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>) {
        this.launcher = launcher
    }

    fun setPermissionResult(granted: Boolean) {
        isGranted = granted
        getLastLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (isGranted) {
            fusedLocationProviderLocation.lastLocation
                .addOnCompleteListener { task ->
                    if (task.exception == null) {
                        val location = task.result
                        val coordinate = Coordinate(
                            latitude = location.latitude,
                            longitude = location.longitude
                        )

                        _myLocation.update { coordinate }
                    }
                }
        }
    }

}