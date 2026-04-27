package com.example.shire.domain.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.shire.db.User as DbUser
import com.example.shire.db.db
import com.example.shire.domain.model.LoggedInUser
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val database = db(context)
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("auth_session", Context.MODE_PRIVATE)

    private object Keys {
        const val loggedInUserId = "logged_in_user_id"
    }

    init {
        ensureDefaultUser()
    }

    override val loggedInUserFlow: Flow<LoggedInUser?> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            trySend(getLoggedInUser())
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(getLoggedInUser())
        awaitClose {
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.distinctUntilChanged()

    override suspend fun login(email: String, password: String): Result<LoggedInUser> {
        val normalizedEmail = email.trim().lowercase()
        val normalizedPassword = password.trim()

        if (normalizedEmail.isBlank()) return Result.failure(IllegalArgumentException("Email requerido"))
        if (normalizedPassword.isBlank()) return Result.failure(IllegalArgumentException("Password requerido"))

        val existingUser = database.getUserByEmail(normalizedEmail)
            ?: return Result.failure(IllegalArgumentException("Usuario no encontrado"))

        if (existingUser.passwordHash != normalizedPassword) {
            return Result.failure(IllegalArgumentException("Credenciales invalidas"))
        }

        setLoggedInUserId(existingUser.id)
        return Result.success(existingUser.toLoggedInUser())
    }

    override suspend fun register(email: String, password: String, name: String): Result<LoggedInUser> {
        val normalizedEmail = email.trim().lowercase()
        val normalizedPassword = password.trim()
        val normalizedName = name.trim()

        if (normalizedName.isBlank()) return Result.failure(IllegalArgumentException("Nombre requerido"))
        if (normalizedEmail.isBlank()) return Result.failure(IllegalArgumentException("Email requerido"))
        if (normalizedPassword.isBlank()) return Result.failure(IllegalArgumentException("Password requerido"))
        if (database.getUserByEmail(normalizedEmail) != null) {
            return Result.failure(IllegalArgumentException("El email ya existe"))
        }

        database.upsertUser(
            DbUser(
                name = normalizedName,
                email = normalizedEmail,
                passwordHash = normalizedPassword,
                createdAt = System.currentTimeMillis()
            )
        )

        val createdUser = database.getUserByEmail(normalizedEmail)
            ?: return Result.failure(IllegalStateException("No se pudo crear el usuario"))

        setLoggedInUserId(createdUser.id)
        return Result.success(createdUser.toLoggedInUser())
    }

    override suspend fun logout() {
        sharedPrefs.edit().remove(Keys.loggedInUserId).apply()
    }

    override fun getLoggedInUser(): LoggedInUser? {
        val userId = getLoggedInUserId() ?: return null
        return database.getUserById(userId)?.toLoggedInUser()
    }

    private fun ensureDefaultUser() {
        if (database.getUserById(1) != null) return
        database.upsertUser(
            DbUser(
                id = 1,
                name = "Demo User",
                email = "demo@shire.local",
                passwordHash = "1234",
                createdAt = System.currentTimeMillis()
            )
        )
    }

    private fun setLoggedInUserId(userId: Int) {
        sharedPrefs.edit().putInt(Keys.loggedInUserId, userId).apply()
    }

    private fun getLoggedInUserId(): Int? {
        if (!sharedPrefs.contains(Keys.loggedInUserId)) return null
        return sharedPrefs.getInt(Keys.loggedInUserId, -1).takeIf { it > 0 }
    }

    private fun DbUser.toLoggedInUser(): LoggedInUser = LoggedInUser(
        id = id,
        name = name,
        email = email
    )
}
