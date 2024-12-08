package com.herehearteam.herehear.data.local.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.herehearteam.herehear.data.local.datastore.UserPreferencesDataStore
import com.herehearteam.herehear.domain.model.User
import com.herehearteam.herehear.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val preferencesDataStore: UserPreferencesDataStore
) : UserRepository {
    override val user: Flow<User?> = preferencesDataStore.userPreferencesFlow

    override suspend fun saveUser(user: User) {
        preferencesDataStore.saveUser(user)
    }

    override suspend fun clearUser() {
        preferencesDataStore.clearUser()
    }

    override suspend fun isLoggedIn(): Boolean {
        return user.first() != null
    }

}