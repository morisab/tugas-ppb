package com.example.starbucks.domain.usecase

import com.example.starbucks.data.repository.AuthRepository
import com.example.starbucks.data.repository.UserRepository
import com.example.starbucks.data.model.UserProfile
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Email and password cannot be empty"))
        }

        return authRepository.signIn(email, password).map { Unit }
    }
}

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        fullName: String,
        email: String,
        password: String,
        birthDate: String
    ): Result<Unit> {
        // Validation
        when {
            fullName.isBlank() -> return Result.failure(Exception("Full name is required"))
            email.isBlank() -> return Result.failure(Exception("Email is required"))
            !isValidEmail(email) -> return Result.failure(Exception("Invalid email format"))
            password.length < 6 -> return Result.failure(Exception("Password must be at least 6 characters"))
            birthDate.isBlank() -> return Result.failure(Exception("Birth date is required"))
        }

        return authRepository.createAccount(email, password, fullName, birthDate).map { Unit }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

class SendPasswordResetUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank()) {
            return Result.failure(Exception("Email cannot be empty"))
        }
        return authRepository.sendPasswordResetEmail(email)
    }
}

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        authRepository.signOut()
    }
}

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<UserProfile> {
        return userRepository.getUserProfile()
    }
}