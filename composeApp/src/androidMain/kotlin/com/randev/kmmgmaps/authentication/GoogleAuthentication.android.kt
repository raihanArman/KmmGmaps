package com.randev.kmmgmaps.authentication

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInApi
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.randev.kmmgmaps.R
import com.randev.kmmgmaps.SecretConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @author Raihan Arman
 * @date 25/10/24
 */

class AndroidGoogleAuthentication(
    private val context: Context,
    private val coroutineScope: CoroutineScope
): GoogleAuthentication {
    private val _isSignIn = mutableStateOf(false)
    override val isSignIn: State<Boolean>
        get() = _isSignIn

    private val _user = mutableStateOf(getUser())
    override val user: State<User?>
        get() = _user

    private val credentialManager = CredentialManager.create(context)

    override fun signIn() {
        if (getUser() == null) {
            coroutineScope.launch {
                signInWithIdentity()
            }
        } else {
            _isSignIn.value = true
        }
    }

    override fun signOut() {
        coroutineScope.launch {
            val clearCredentials = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearCredentials)

            Firebase.auth.signOut()
            _isSignIn.value = false
            _user.value = null
        }
    }

    private fun signInWIthCredentials(idToken: String) {
        val credentials = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credentials).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val firebaseUser = task.result.user
                _isSignIn.value = firebaseUser != null
                _user.value = getUser()
            } else {
                _isSignIn.value = false
            }
        }
    }

    private suspend fun signInWithIdentity() {
        val weClientId = SecretConfig.WEB_CLIENT_ID

        val googleOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(weClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val resultCredentials = result.credential
            val googleCredentialType = GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            if (resultCredentials is CustomCredential && resultCredentials.type == googleCredentialType) {
                try {
                    val tokenCredential = GoogleIdTokenCredential.createFrom(resultCredentials.data)

                    val idToken = tokenCredential.idToken
                    signInWIthCredentials(idToken)
                } catch (e: GoogleIdTokenParsingException) {
                    _isSignIn.value = false
                }
            }

        } catch (e: GetCredentialException) {
            _isSignIn.value = false
        }
    }

    private fun getUser(): User? {
        val user = Firebase.auth.currentUser
        return if (user != null) {
            User(
                name = user.displayName.orEmpty(),
                email = user.email.orEmpty(),
                photoUrl = user.photoUrl.toString()
            )
        }else {
            null
        }
    }
}

@Composable
actual fun rememberGoogleAuthentication(): GoogleAuthentication {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val androidGoogleAuthentication = remember { AndroidGoogleAuthentication(context, coroutineScope) }

    return androidGoogleAuthentication
}