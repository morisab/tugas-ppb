package com.example.starbucks.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.starbucks.R
import com.example.starbucks.presentation.navigation.Screen
import com.example.starbucks.presentation.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
            navController.navigate(Screen.Home.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
            viewModel.clearSuccessState()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
        }
    }

    if (uiState.showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideForgotPasswordDialog() },
            title = { Text("Reset Password") },
            text = {
                Column {
                    Text("Enter your email address to receive a password reset link.")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = uiState.emailForReset,
                        onValueChange = viewModel::updateEmailForReset,
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    if (uiState.resetEmailSent) {
                        Text(
                            text = "Password reset link sent!",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.sendPasswordResetEmail() },
                    enabled = uiState.emailForReset.isNotBlank()
                ) {
                    Text("Send Link")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideForgotPasswordDialog() }) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Starbucks Logo",
                modifier = Modifier.size(120.dp).padding(bottom = 24.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Welcome Back!",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Login to your account",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::updateEmail,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorMessage != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::updatePassword,
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorMessage != null
            )

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            TextButton(
                onClick = { viewModel.showForgotPasswordDialog() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Forgot Password?")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.signIn() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = !uiState.isLoading && uiState.email.isNotBlank() && uiState.password.isNotBlank()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Login")
                }
            }

            TextButton(
                onClick = { navController.navigate(Screen.Register.route) },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Don't have an account? Sign Up")
            }
        }
    }
}
