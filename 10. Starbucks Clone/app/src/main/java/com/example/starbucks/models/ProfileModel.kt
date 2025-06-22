package com.example.starbucks.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucks.data.model.UserProfile
import com.example.starbucks.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var userProfile by mutableStateOf<UserProfile?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            userRepository.getUserProfile()
                .onSuccess { profile ->
                    userProfile = profile
                    isLoading = false
                }
                .onFailure { exception ->
                    errorMessage = exception.message ?: "Failed to fetch profile"
                    isLoading = false
                }
        }
    }
}
