package com.example.shire.domain.repository

import com.example.shire.domain.model.LoggedInUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val loggedInUserFlow: Flow<LoggedInUser?>
    suspend fun login(email: String, password: String): Result<LoggedInUser>
    suspend fun register(email: String, password: String, name: String): Result<LoggedInUser>
    suspend fun recoverPassword(email: String): Result<Unit>
    suspend fun logout()
    fun getLoggedInUser(): LoggedInUser?
}
