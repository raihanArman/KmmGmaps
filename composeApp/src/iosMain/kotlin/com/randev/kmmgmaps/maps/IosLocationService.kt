package com.randev.kmmgmaps.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.randev.kmmgmaps.network.data.Coordinate
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.darwin.NSObject

/**
 * @author Raihan Arman
 * @date 04/10/24
 */
class IosLocationService : LocationService {
    private val _myLocation: MutableStateFlow<Coordinate> = MutableStateFlow(Coordinate())
    override val myLocation: StateFlow<Coordinate>
        get() = _myLocation

    @OptIn(ExperimentalForeignApi::class)
    private val iosLocationDelegate = object: NSObject(), CLLocationManagerDelegateProtocol {
        override fun locationManager(
            manager: CLLocationManager,
            didChangeAuthorizationStatus: CLAuthorizationStatus
        ) {
            when (didChangeAuthorizationStatus) {
                kCLAuthorizationStatusNotDetermined, kCLAuthorizationStatusDenied -> {
                    // not granted
                    println("permission not granted...")
                    manager.requestWhenInUseAuthorization()
                }
                kCLAuthorizationStatusAuthorizedAlways, kCLAuthorizationStatusAuthorizedWhenInUse, kCLAuthorizationStatusRestricted -> {
                    // granted
                    println("permission granted...")
                    manager.startUpdatingLocation()
                }
            }
        }

        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            val coordinate = (didUpdateLocations as? List<CLLocation>)
                ?.firstOrNull()
                ?.coordinate
                ?.useContents {
                    Coordinate(latitude, longitude)
                }

            if (coordinate != null) {
                _myLocation.update { coordinate }
            }
        }
    }

    override suspend fun getMyLocation() {
        println("get my location")
        val locationManager = CLLocationManager()
        locationManager.delegate = iosLocationDelegate
        locationManager.requestWhenInUseAuthorization()
    }

}