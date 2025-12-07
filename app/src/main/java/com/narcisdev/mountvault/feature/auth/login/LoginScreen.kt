package com.narcisdev.mountvault.feature.auth.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
        }
    }
    Scaffold { padding ->
        Column(Modifier.padding(padding)) {
            TextField(
                value = uiState.userEntry,
                onValueChange = { viewModel.onUserEntryChange(it.trim()) },
                label = { Text("Email o Username") },
            )
            Spacer(Modifier.height(16.dp))
            TextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChange(it.trim()) },
                label = { Text("Password") },
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { viewModel.onLoginClick() }
            ) {
                Text("Login")
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { onRegisterClicked() }
            ) {
                Text("Register")
            }
            Spacer(Modifier.height(16.dp))
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
