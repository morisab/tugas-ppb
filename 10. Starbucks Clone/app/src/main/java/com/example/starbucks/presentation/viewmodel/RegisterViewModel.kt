package com.example.starbucks.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starbucks.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val birthDate: String = "",
    val isLoading: Boolean = false,
    val showDatePickerDialog: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
    val isRegistrationSuccessful: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    fun updateFullName(fullName: String) {
        _uiState.value = _uiState.value.copy(
            fullName = fullName,
            fieldErrors = _uiState.value.fieldErrors - "fullName"
        )
    }

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            fieldErrors = _uiState.value.fieldErrors - "email"
        )
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            fieldErrors = _uiState.value.fieldErrors - "password"
        )
    }

    fun showDatePicker() {
        _uiState.value = _uiState.value.copy(showDatePickerDialog = true)
    }

    fun hideDatePicker() {
        _uiState.value = _uiState.value.copy(showDatePickerDialog = false)
    }

    fun onDateSelected(dateMillis: Long) {
        val date = Date(dateMillis)
        val formattedDate = dateFormatter.format(date)
        _uiState.value = _uiState.value.copy(
            birthDate = formattedDate,
            showDatePickerDialog = false,
            fieldErrors = _uiState.value.fieldErrors - "birthDate"
        )
    }

    fun register() {
        if (!validateFields()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            registerUseCase(
                _uiState.value.fullName,
                _uiState.value.email,
                _uiState.value.password,
                _uiState.value.birthDate
            )
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRegistrationSuccessful = true
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Registration failed"
                    )
                }
        }
    }

    private fun validateFields(): Boolean {
        val errors = mutableMapOf<String, String>()

        if (_uiState.value.fullName.isBlank()) {
            errors["fullName"] = "Full name is required"
        }

        if (_uiState.value.email.isBlank()) {
            errors["email"] = "Email is required"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()) {
            errors["email"] = "Invalid email format"
        }

        if (_uiState.value.password.length < 6) {
            errors["password"] = "Password must be at least 6 characters"
        }

        if (_uiState.value.birthDate.isBlank()) {
            errors["birthDate"] = "Birth date is required"
        }

        _uiState.value = _uiState.value.copy(fieldErrors = errors)
        return errors.isEmpty()
    }

    fun clearSuccessState() {
        _uiState.value = _uiState.value.copy(isRegistrationSuccessful = false)
    }
}