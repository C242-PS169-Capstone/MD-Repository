package com.herehearteam.herehear.di

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.herehearteam.herehear.data.local.datastore.UserPreferencesDataStore
import com.herehearteam.herehear.data.local.repository.UserRepositoryImpl
import com.herehearteam.herehear.data.remote.GoogleAuthUiClient
import com.herehearteam.herehear.domain.repository.UserRepository

class AppDependencies(private val context: Context) {
    private val signInClient: SignInClient by lazy {
        Identity.getSignInClient(context)
    }

    val googleAuthUiClient: GoogleAuthUiClient by lazy {
        GoogleAuthUiClient(context, signInClient)
    }

    private val userPreferencesDataStore: UserPreferencesDataStore by lazy {
        UserPreferencesDataStore.getInstance(context)
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(userPreferencesDataStore)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: AppDependencies? = null

        fun getInstance(context: Context): AppDependencies {
            if (instance == null) {
                instance = AppDependencies(context.applicationContext)
            }
            return instance!!
        }
    }
}
