package com.example.shire.domain.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
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
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val database = db(context)
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("auth_session", Context.MODE_PRIVATE)

    private object Keys {
        const val loggedInUserId = "logged_in_user_id"
    }

    init {
        ensureDefaultUser()
    }

    override val loggedInUserFlow: Flow<LoggedInUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(resolveLoggedInUser(auth.currentUser))
        }
        firebaseAuth.addAuthStateListener(listener)
        trySend(resolveLoggedInUser(firebaseAuth.currentUser))
        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }.distinctUntilChanged()

    override suspend fun login(email: String, password: String): Result<LoggedInUser> {
        val normalizedEmail = email.trim().lowercase()
        val normalizedPassword = password.trim()

        if (!isValidEmail(normalizedEmail)) return Result.failure(IllegalArgumentException("Email invalido"))
        if (normalizedPassword.isBlank()) return Result.failure(IllegalArgumentException("Password requerido"))

        return runCatching {
            firebaseAuth
                .signInWithEmailAndPassword(normalizedEmail, normalizedPassword)
                .awaitCompletion()

            resolveLoggedInUser(firebaseAuth.currentUser)
                ?: throw IllegalStateException("No se pudo recuperar el usuario autenticado")
        }.recoverCatching { error ->
            throw mapFirebaseAuthError(error)
        }
    }

    override suspend fun register(email: String, password: String, name: String): Result<LoggedInUser> {
        val normalizedEmail = email.trim().lowercase()
        val normalizedPassword = password.trim()
        val normalizedName = name.trim()

        if (normalizedName.isBlank()) return Result.failure(IllegalArgumentException("Nombre requerido"))
        if (!isValidEmail(normalizedEmail)) return Result.failure(IllegalArgumentException("Email invalido"))
        if (normalizedPassword.isBlank()) return Result.failure(IllegalArgumentException("Password requerido"))
        if (normalizedPassword.length < 4) return Result.failure(IllegalArgumentException("La contraseña debe tener al menos 4 caracteres"))

        return runCatching {
            firebaseAuth
                .createUserWithEmailAndPassword(normalizedEmail, normalizedPassword)
                .awaitCompletion()

            firebaseAuth.currentUser?.let { user ->
                user.updateProfile(
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(normalizedName)
                        .build()
                ).awaitCompletion()
            }

            resolveLoggedInUser(firebaseAuth.currentUser)
                ?: throw IllegalStateException("No se pudo crear el usuario")
        }.recoverCatching { error ->
            throw mapFirebaseAuthError(error)
        }
    }

    override suspend fun recoverPassword(email: String): Result<Unit> {
        val normalizedEmail = email.trim().lowercase()

        if (!isValidEmail(normalizedEmail)) return Result.failure(IllegalArgumentException("Email invalido"))

        return try {
            firebaseAuth.sendPasswordResetEmail(normalizedEmail).awaitCompletion()
            Result.success(Unit)
        } catch (error: Throwable) {
            // To avoid account enumeration, treat "user not found" like a successful request.
            if (isUserNotFoundError(error)) {
                Result.success(Unit)
            } else {
                Result.failure(mapRecoverPasswordError(error))
            }
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
        sharedPrefs.edit().remove(Keys.loggedInUserId).apply()
    }

    override fun getLoggedInUser(): LoggedInUser? {
        return resolveLoggedInUser(firebaseAuth.currentUser)
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

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun resolveLoggedInUser(firebaseUser: FirebaseUser?): LoggedInUser? {
        if (firebaseUser == null) {
            sharedPrefs.edit().remove(Keys.loggedInUserId).apply()
            return null
        }

        val normalizedEmail = firebaseUser.email?.trim()?.lowercase() ?: return null
        val nameFromFirebase = firebaseUser.displayName?.trim().orEmpty()

        val localUser = database.getUserByEmail(normalizedEmail) ?: run {
            database.upsertUser(
                DbUser(
                    name = nameFromFirebase.ifBlank {
                        normalizedEmail.substringBefore('@').ifBlank { "User" }
                    },
                    email = normalizedEmail,
                    passwordHash = "",
                    createdAt = System.currentTimeMillis()
                )
            )
            database.getUserByEmail(normalizedEmail)
        } ?: return null

        setLoggedInUserId(localUser.id)
        return localUser.toLoggedInUser()
    }

    private fun mapFirebaseAuthError(error: Throwable): Throwable {
        val message = error.message?.lowercase().orEmpty()
        return when {
            message.contains("password is invalid") ||
                message.contains("no user record") ||
                message.contains("invalid-credential") -> IllegalArgumentException("Credenciales invalidas")
            message.contains("email address is already") ||
                message.contains("email-already-in-use") -> IllegalArgumentException("El email ya existe")
            message.contains("badly formatted") ||
                message.contains("invalid-email") -> IllegalArgumentException("Email invalido")
            else -> error
        }
    }

    private fun mapRecoverPasswordError(error: Throwable): Throwable {
        val message = error.message?.lowercase().orEmpty()
        return when {
            message.contains("invalid-email") ||
                message.contains("badly formatted") -> IllegalArgumentException("Email invalido")
            message.contains("too-many-requests") -> IllegalStateException("Demasiados intentos. Intentalo mas tarde")
            else -> error
        }
    }

    private fun isUserNotFoundError(error: Throwable): Boolean {
        val message = error.message?.lowercase().orEmpty()
        return message.contains("user-not-found") || message.contains("no user record")
    }

    private suspend fun Task<*>.awaitCompletion() {
        suspendCancellableCoroutine<Unit> { continuation ->
            addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(task.exception ?: IllegalStateException("Operacion Firebase fallida"))
                }
            }
        }
    }

    private fun DbUser.toLoggedInUser(): LoggedInUser = LoggedInUser(
        id = id,
        name = name,
        email = email
    )
}
