package com.randev.kmmgmaps.maps

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.FusedLocationProviderClient

/**
 * @author Raihan Arman
 * @date 04/10/24
 */
@Composable
actual fun rememberLocationService(): LocationService {
    val androidContext = LocalContext.current
    val androidLocationService = remember {
        AndroidLocationService(androidContext)
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val isGranted = !result.map { it.value }.contains(false)
        println("Ampas kuda -> isGranted $isGranted")
        androidLocationService.setPermissionResult(isGranted)
        androidLocationService.isGranted = isGranted
    }

    LaunchedEffect(launcher) {
        println("Ampas kuda -> launcher")
        androidLocationService.setLauncher(launcher)
    }

    LaunchedEffect(Unit) {
        println("Ampas kuda -> request permission")
        androidLocationService.requestPermission()
    }

    return androidLocationService
}