package com.randev.kmmgmaps.feature.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.randev.kmmgmaps.authentication.rememberGoogleAuthentication
import com.randev.kmmgmaps.navigation.appyx.LocalNavigator
import com.randev.kmmgmaps.navigation.appyx.NavTarget

/**
 * @author Raihan Arman
 * @date 01/10/24
 */
@Composable
fun MainScreen() {
    val navigator = LocalNavigator.current

    val googleAuthentication = rememberGoogleAuthentication()
    val result by googleAuthentication.isSignIn

    LaunchedEffect(result) {
        if (result) {
            navigator.navigate(NavTarget.FeatureGoogleSignIn)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                navigator.navigate(NavTarget.GoogleMaps)
            },
        ) {
            Text("Maps")
        }

        Button(
            onClick = {
                navigator.navigate(NavTarget.FeatureMaps)
            },
        ) {
            Text("Feature Maps")
        }

        Button(
            onClick = {
                googleAuthentication.signIn()
            },
        ) {
            Text("Google Sign In")
        }
        Button(
            onClick = {
                googleAuthentication.signOut()
            },
        ) {
            Text("Google Sign Out")
        }
    }

}