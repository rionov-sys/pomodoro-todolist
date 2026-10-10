package com.grinding.app.data.repository

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

interface AuthRepository {
    val sessionStatus: Flow<SessionStatus>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String, fullName: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    fun getCurrentUserId(): String?
    fun getCurrentUserEmail(): String?
    fun getCurrentUserName(): String?
}

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth
) : AuthRepository {

    override val sessionStatus: Flow<SessionStatus> = auth.sessionStatus

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return runCatching {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }
    }

    override suspend fun signUp(email: String, password: String, fullName: String): Result<Unit> {
        return runCatching {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
                this.data = buildJsonObject {
                    put("full_name", fullName)
                }
            }
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return runCatching {
            auth.signOut()
        }
    }

    override fun getCurrentUserId(): String? {
        return auth.currentSessionOrNull()?.user?.id
    }

    override fun getCurrentUserEmail(): String? {
        return auth.currentSessionOrNull()?.user?.email
    }

    override fun getCurrentUserName(): String? {
        val user = auth.currentSessionOrNull()?.user
        val meta = user?.userMetadata
        return meta?.get("full_name")?.toString()?.replace("\"", "")
            ?: user?.email?.substringBefore("@")
            ?: "Pengguna"
    }
}
