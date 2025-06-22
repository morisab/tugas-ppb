package com.example.starbucks.data.repository

import com.example.starbucks.data.local.LocalUserStorage
import com.example.starbucks.data.model.User
import com.example.starbucks.data.model.UserProfile
import com.example.starbucks.data.util.PasswordHelper
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val localUserStorage: LocalUserStorage,
    private val passwordHelper: PasswordHelper
) {

    val currentUser: User? get() = localUserStorage.getCurrentUser()

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val user = localUserStorage.getUserByEmail(email)
            if (user != null && passwordHelper.verifyPassword(password, user.passwordHash)) {
                localUserStorage.setCurrentUserId(user.id)
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createAccount(email: String, password: String, fullName: String, birthDate: String): Result<User> {
        return try {
            if (localUserStorage.getUserByEmail(email) != null) {
                return Result.failure(Exception("User with this email already exists"))
            }

            val userId = UUID.randomUUID().toString()
            val hashedPassword = passwordHelper.hashPassword(password)

            val profile = UserProfile(
                id = userId,
                fullName = fullName,
                email = email,
                birthDate = birthDate,
                passwordHash = hashedPassword
            )

            val user = User(
                id = userId,
                email = email,
                passwordHash = hashedPassword,
                profile = profile
            )

            localUserStorage.saveUser(user)
            localUserStorage.setCurrentUserId(userId)

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            val user = localUserStorage.getUserByEmail(email)
            if (user != null) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("No user found with this email"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        localUserStorage.clearCurrentUser()
    }
}