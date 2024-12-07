package com.herehearteam.herehear.data.local.datastore

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.herehearteam.herehear.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesDataStore private constructor(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

    val userPreferencesFlow: Flow<User?> = context.dataStore.data.map { preferences ->
        val userId = preferences[PreferencesKey.USER_ID]
        if (userId != null) {
            User(
                userId = userId,
                email = preferences[PreferencesKey.EMAIL] ?: "",
                displayName = preferences[PreferencesKey.DISPLAY_NAME],
                idToken = preferences[PreferencesKey.ID_TOKEN]
            )
        } else null
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.USER_ID] = user.userId
            preferences[PreferencesKey.EMAIL] = user.email
            user.displayName?.let { preferences[PreferencesKey.DISPLAY_NAME] = it }
            user.idToken?.let { preferences[PreferencesKey.ID_TOKEN] = it }
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { preferences ->
            preferences.clear()
            Log.d("UPD", "User data cleared")
        }
    }

    private object PreferencesKey {
        val USER_ID = stringPreferencesKey("user_id")
        val EMAIL = stringPreferencesKey("email")
        val DISPLAY_NAME = stringPreferencesKey("display_name")
        val ID_TOKEN = stringPreferencesKey("id_token")
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: UserPreferencesDataStore? = null

        fun getInstance(context: Context): UserPreferencesDataStore {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreferencesDataStore(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}