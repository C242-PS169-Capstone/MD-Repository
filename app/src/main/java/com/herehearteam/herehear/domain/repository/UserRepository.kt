package com.herehearteam.herehear.domain.repository

import com.herehearteam.herehear.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val user: Flow<User?>
    suspend fun saveUser(user: User)
    suspend fun clearUser()
    suspend fun isLoggedIn(): Boolean
}