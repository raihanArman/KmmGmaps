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
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInApi
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.randev.kmmgmaps.R
import com.randev.kmmgmaps.SecretConfig

/**
 * @author Raihan Arman
 * @date 25/10/24
 */

class AndroidGoogleAuthentication(
    private val context: Context
): GoogleAuthentication {
    private val _isSignIn = mutableStateOf(false)
    override val isSignIn: State<Boolean>
        get() = _isSignIn

    private val _user = mutableStateOf(getUser())
    override val user: State<User?>
        get() = _user

    private lateinit var signInClient: GoogleSignInClient
    private lateinit var signInIntent: Intent
    private lateinit var signInLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>

    fun initialize() {
        val webClientId = SecretConfig.WEB_CLIENT_ID
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .build()

        signInClient = GoogleSignIn.getClient(context, gso)
        signInIntent = signInClient.signInIntent
    }

    override fun signIn() {
        if (getUser() == null) {
            signInLauncher.launch(signInIntent)
        } else {
            _isSignIn.value = true
        }
    }

    override fun signOut() {
        signInClient.signOut()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Firebase.auth.signOut()
                    _isSignIn.value = false
                    _user.value = null
                }
            }
    }

    fun observerLauncher(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            if (task.isSuccessful) {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                if (idToken != null) {
                    val credentials = GoogleAuthProvider.getCredential(idToken, null)
                    signInWIthCredentials(credentials)
                }
            }
        } catch (e: ApiException) {
            _isSignIn.value = false
        }
    }

    private fun signInWIthCredentials(credential: AuthCredential) {
        Firebase.auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val firebaseUser = task.result.user
                _isSignIn.value = firebaseUser != null
                _user.value = getUser()
            } else {
                _isSignIn.value = false
            }
        }
    }

    private fun getUser(): User? {
        val user = Firebase.auth.currentUser
        return if (user != null) {
            User(
                name = user.displayName.orEmpty(),
                email = user.email.orEmpty()
            )
        }else {
            null
        }
    }

    fun bindLauncher(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
        signInLauncher = launcher
    }

}

@Composable
actual fun rememberGoogleAuthentication(): GoogleAuthentication {
    val context = LocalContext.current
    val androidGoogleAuthentication = remember { AndroidGoogleAuthentication(context) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        androidGoogleAuthentication.observerLauncher(it.data)
    }

    LaunchedEffect(Unit) {
        androidGoogleAuthentication.bindLauncher(launcher)
        androidGoogleAuthentication.initialize()
    }

    return androidGoogleAuthentication
}