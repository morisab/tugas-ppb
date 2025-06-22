package com.example.starbucks.data.repository

import com.example.starbucks.data.local.LocalUserStorage
import com.example.starbucks.data.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val localUserStorage: LocalUserStorage,
    private val authRepository: AuthRepository
) {
    suspend fun saveUserProfile(userProfile: UserProfile): Result<Unit> {
        return try {
            val currentUser = authRepository.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            val updatedUser = currentUser.copy(profile = userProfile)
            localUserStorage.saveUser(updatedUser)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(): Result<UserProfile> {
        return try {
            val currentUser = authRepository.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            Result.success(currentUser.profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}