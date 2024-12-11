package com.herehearteam.herehear.data.remote.response

import com.herehearteam.herehear.data.model.DataUser

data class UserResponse(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: DataUser?
)

data class AllUsersResponse(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: List<DataUser>
)