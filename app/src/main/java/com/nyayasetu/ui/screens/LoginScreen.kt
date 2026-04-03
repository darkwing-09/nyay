package com.nyayasetu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nyayasetu.data.models.LoginRequest
import com.nyayasetu.ui.viewmodel.AuthViewModel
import com.nyayasetu.utils.Resource

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is Resource.Success) {
            viewModel.resetState()
            onNavigateToDashboard()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                emailError = false 
            },
            label = { Text("Email") },
            isError = emailError,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (emailError) {
            Text("Email cannot be empty", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        var passwordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                passwordError = false
            },
            label = { Text("Password") },
            isError = passwordError,
            visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) androidx.compose.material.icons.Icons.Filled.Visibility else androidx.compose.material.icons.Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (passwordError) {
            Text("Password cannot be empty", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState is Resource.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    val trimmedEmail = email.trim()
                    val trimmedPassword = password.trim()
                    
                    emailError = trimmedEmail.isEmpty()
                    passwordError = trimmedPassword.isEmpty()

                    if (!emailError && !passwordError) {
                        viewModel.login(LoginRequest(trimmedEmail, trimmedPassword))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }

        if (uiState is Resource.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = (uiState as Resource.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Google Login Flow safely mocked */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Login with Google")
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onNavigateToRegister) {
            Text("Don't have an account? Register")
        }
    }
}
