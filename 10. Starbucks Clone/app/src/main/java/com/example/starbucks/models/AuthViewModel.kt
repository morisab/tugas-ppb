package com.example.starbucks.models

import androidx.lifecycle.ViewModel
import com.example.starbucks.data.repository.AuthRepository
import com.example.starbucks.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val currentUser get() = authRepository.currentUser

    fun isUserLoggedIn(): Boolean {
        return authRepository.currentUser != null
    }

    suspend fun signIn(email: String, password: String): Result<User> {
        return authRepository.signIn(email, password)
    }

    suspend fun createAccount(email: String, password: String, fullName: String, birthDate: String): Result<User> {
        return authRepository.createAccount(email, password, fullName, birthDate)
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return authRepository.sendPasswordResetEmail(email)
    }

    fun signOut() {
        authRepository.signOut()
    }
}
