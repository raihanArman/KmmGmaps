package com.randev.kmmgmaps.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIRGoogleAuthProvider
import cocoapods.FirebaseAuth.FIRUser
import cocoapods.FirebaseCore.FIRApp
import cocoapods.GoogleSignIn.GIDConfiguration
import cocoapods.GoogleSignIn.GIDSignIn
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIViewController

/**
 * @author Raihan Arman
 * @date 25/10/24
 */

@OptIn(ExperimentalForeignApi::class)
class IosGoogleAuthentication(
    private val uiViewController: UIViewController
): GoogleAuthentication {
    private val _isSignIn = mutableStateOf(false)
    override val isSignIn: State<Boolean>
        get() = _isSignIn

    private val _user = mutableStateOf(getUser())
    override val user: State<User?>
        get() = _user

    init {
        val clientId = FIRApp.defaultApp()?.options()?.clientID
        if (clientId != null) {
            val config = GIDConfiguration(clientId)
            GIDSignIn.sharedInstance.configuration = config
        }
    }

    override fun signIn() {
        val user = FIRAuth.auth().currentUser()
        if (user == null) {
            GIDSignIn.sharedInstance.signInWithPresentingViewController(uiViewController) { result, error ->
                if (result != null) {
                    val googleUser = result.user
                    val idToken = googleUser.idToken?.tokenString

                    if (idToken != null) {
                        val accessToken = googleUser.accessToken.tokenString
                        val credentials = FIRGoogleAuthProvider.credentialWithIDToken(
                            idToken = idToken,
                            accessToken = accessToken
                        )

                        FIRAuth.auth().signInWithCredential(credentials) { firResult, error ->
                            _isSignIn.value = firResult != null && error == null
                        }

                    } else {
                        _isSignIn.value = false
                    }
                } else {
                    _isSignIn.value = false
                }
            }
        } else {
            _isSignIn.value = true
        }
    }

    override fun signOut() {
        FIRAuth.auth().signOut(null)
        GIDSignIn.sharedInstance.signOut()
        _isSignIn.value = false
    }

    private fun getUser(): User? {
        val user = FIRAuth.auth().currentUser()
        return if (user != null) {
            User(
                name = user.displayName().orEmpty(),
                email = user.email().orEmpty(),
                photoUrl = user.photoURL()?.absoluteString
            )
        }else {
            null
        }
    }
}

@Composable
actual fun rememberGoogleAuthentication(): GoogleAuthentication {
    val uiViewController = LocalUIViewController.current
    return remember { IosGoogleAuthentication(uiViewController) }
}