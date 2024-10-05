package com.randev.kmmgmaps.maps

import android.Manifest
import androidx.activity.compose.ManagedActivityResultLauncher
import com.randev.kmmgmaps.network.data.Coordinate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Raihan Arman
 * @date 04/10/24
 */
class AndroidLocationService: LocationService {
    private val _myLocation: MutableStateFlow<Coordinate> = MutableStateFlow(Coordinate())
    override val myLocation: StateFlow<Coordinate>
        get() = _myLocation

    var isGranted = false

    private val permission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var launcher : ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>

    override suspend fun getMyLocation() {
        if (isGranted) {
            // get location
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
    }

}