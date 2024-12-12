package com.herehearteam.herehear.data.remote

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.herehearteam.herehear.R
import com.herehearteam.herehear.data.model.UserRequestDto
import com.herehearteam.herehear.data.remote.api.ApiConfig
import com.herehearteam.herehear.domain.model.SignInResult
import com.herehearteam.herehear.domain.model.User
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth
    private val apiService = ApiConfig.getApiService()

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val firebaseUser = auth.signInWithCredential(googleCredentials).await().user

            if(firebaseUser != null) {
                val currentUser = apiService.getUserById(firebaseUser.uid)
                if(!currentUser.status){
                val userRequest = UserRequestDto(
                    user_id = firebaseUser!!.uid, // Use Firebase UID
                    username = firebaseUser.displayName!!,
                    email = firebaseUser.email!!,
                    password = "Tidak ada Password"
                )
                    apiService.createUser(userRequest)
                }
            }

            SignInResult(
                data = firebaseUser?.run {
                    User(
                        userId = uid,
                        email = email ?: "",
                        displayName = displayName,
                        idToken = googleIdToken
                    )
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
            Log.d("AuthClient", "User signed out successfully")
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): User? = auth.currentUser?.run {
        User(
            userId = uid,
            email = email ?: "",
            displayName = displayName,
            idToken = null
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}
