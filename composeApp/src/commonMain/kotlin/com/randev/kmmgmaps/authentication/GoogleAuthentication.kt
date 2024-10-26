package com.randev.kmmgmaps.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

/**
 * @author Raihan Arman
 * @date 25/10/24
 */
interface GoogleAuthentication {
    val isSignIn: State<Boolean>
    val user: State<User?>

    fun signIn()
    fun signOut()
}

@Composable
expect fun rememberGoogleAuthentication(): GoogleAuthentication