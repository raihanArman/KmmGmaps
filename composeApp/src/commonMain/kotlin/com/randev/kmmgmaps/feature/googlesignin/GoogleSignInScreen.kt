package com.randev.kmmgmaps.feature.googlesignin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.randev.kmmgmaps.authentication.rememberGoogleAuthentication

/**
 * @author Raihan Arman
 * @date 25/10/24
 */
@Composable
fun GoogleSignInScreen(modifier: Modifier = Modifier) {
    val googleAuthentication = rememberGoogleAuthentication()
    val user by googleAuthentication.user

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Selamat Datang : ${user?.name} | ${user?.email}")
    }
}