package com.example.starbucks.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucks.domain.usecase.SignInUseCase
import com.example.starbucks.domain.usecase.SendPasswordResetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false,
    val showForgotPasswordDialog: Boolean = false,
    val emailForReset: String = "",
    val resetEmailSent: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val sendPasswordResetUseCase: SendPasswordResetUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun updateEmailForReset(email: String) {
        _uiState.value = _uiState.value.copy(emailForReset = email)
    }

    fun showForgotPasswordDialog() {
        _uiState.value = _uiState.value.copy(showForgotPasswordDialog = true)
    }

    fun hideForgotPasswordDialog() {
        _uiState.value = _uiState.value.copy(
            showForgotPasswordDialog = false,
            emailForReset = "",
            resetEmailSent = false
        )
    }

    fun signIn() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            signInUseCase(_uiState.value.email, _uiState.value.password)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoginSuccessful = true
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Login failed"
                    )
                }
        }
    }

    fun sendPasswordResetEmail() {
        viewModelScope.launch {
            sendPasswordResetUseCase(_uiState.value.emailForReset)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(resetEmailSent = true)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = exception.message ?: "Failed to send reset email"
                    )
                }
        }
    }

    fun clearSuccessState() {
        _uiState.value = _uiState.value.copy(isLoginSuccessful = false)
    }
}