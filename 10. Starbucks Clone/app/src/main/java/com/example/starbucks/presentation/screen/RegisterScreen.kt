package com.example.starbucks.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.starbucks.presentation.navigation.Screen
import com.example.starbucks.presentation.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.isRegistrationSuccessful) {
        if (uiState.isRegistrationSuccessful) {
            navController.navigate(Screen.Home.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
            viewModel.clearSuccessState()
        }
    }

    if (uiState.showDatePickerDialog) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { viewModel.hideDatePicker() },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.onDateSelected(it)
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideDatePicker() }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Account") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = viewModel::updateFullName,
                label = { Text("Full Name") },
                isError = uiState.fieldErrors.containsKey("fullName"),
                supportingText = {
                    uiState.fieldErrors["fullName"]?.let { Text(it) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.birthDate,
                onValueChange = {},
                label = { Text("Birth Date") },
                readOnly = true,
                isError = uiState.fieldErrors.containsKey("birthDate"),
                supportingText = {
                    uiState.fieldErrors["birthDate"]?.let { Text(it) }
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Select Date",
                        modifier = Modifier.clickable { viewModel.showDatePicker() }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.showDatePicker() }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::updateEmail,
                label = { Text("Email") },
                isError = uiState.fieldErrors.containsKey("email"),
                supportingText = {
                    uiState.fieldErrors["email"]?.let { Text(it) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::updatePassword,
                label = { Text("Password") },
                isError = uiState.fieldErrors.containsKey("password"),
                supportingText = {
                    uiState.fieldErrors["password"]?.let { Text(it) }
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.register() },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Sign Up")
                }
            }
        }
    }
}