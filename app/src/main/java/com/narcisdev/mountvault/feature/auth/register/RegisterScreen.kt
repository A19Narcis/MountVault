package com.narcisdev.mountvault.feature.auth.register

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.narcisdev.mountvault.R

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    backToLogin: () -> Unit,
    navigateToMain: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            navigateToMain()
        }
    }

    Scaffold { padding ->
        Column(Modifier.padding(padding)) {
            Image(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 20.dp)
                    .size(30.dp)
                    .clickable {
                        backToLogin()
                    },
                painter = painterResource(R.drawable.back), contentDescription = null
            )
            Spacer(Modifier.height(16.dp))
            TextField(
                value = uiState.age.toString().trim(),
                onValueChange = { viewModel.onAgeChange(it.trim()) },
                label = { Text("Age") })
            Spacer(Modifier.height(16.dp))
            TextField(
                value = uiState.email.trim(),
                onValueChange = { viewModel.onEmailChange(it.trim()) },
                label = { Text("Email") })
            Spacer(Modifier.height(16.dp))
            TextField(
                value = uiState.username.trim(),
                onValueChange = { viewModel.onUsernameChange(it.trim()) },
                label = { Text("Username") })
            Spacer(Modifier.height(16.dp))
            TextField(
                value = uiState.password.trim(),
                onValueChange = { viewModel.onPasswordChange(it.trim()) },
                label = { Text("Password") })
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { viewModel.onRegisterClicked() }) {
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