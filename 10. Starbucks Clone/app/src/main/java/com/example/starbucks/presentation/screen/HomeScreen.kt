package com.example.starbucks.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.starbucks.presentation.navigation.Screen
import com.example.starbucks.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSignedOut) {
        if (uiState.isSignedOut) {
            navController.navigate(Screen.Login.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
            viewModel.clearSignOutState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome!",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        uiState.userEmail?.let { email ->
            Text(
                text = "You are logged in as: $email",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate(Screen.Profile.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Profile",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("View My Profile")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { viewModel.signOut() }
        ) {
            Text("Logout")
        }
    }
}
