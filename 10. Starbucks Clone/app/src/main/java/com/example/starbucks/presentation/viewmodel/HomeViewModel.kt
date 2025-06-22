package com.example.starbucks.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.starbucks.data.repository.AuthRepository
import com.example.starbucks.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class HomeUiState(
    val userEmail: String? = null,
    val isSignedOut: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            userEmail = authRepository.currentUser?.email
        )
    }

    fun signOut() {
        signOutUseCase()
        _uiState.value = _uiState.value.copy(isSignedOut = true)
    }

    fun clearSignOutState() {
        _uiState.value = _uiState.value.copy(isSignedOut = false)
    }
}