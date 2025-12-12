package com.narcisdev.mountvault.feature.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.components.MountVaultErrorText
import com.narcisdev.mountvault.core.components.MountVaultTextField
import com.narcisdev.mountvault.core.components.WowButton

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    backToLogin: () -> Unit,
    navigateToMain: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    var passwordHidden by remember { mutableStateOf(true) }

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            navigateToMain()
        }
    }

    Scaffold { padding ->
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            focusManager.clearFocus()
                        }
                    }
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 20.dp)
                        .size(30.dp)
                        .clickable { backToLogin() },
                    painter = painterResource(R.drawable.back),
                    contentDescription = null
                )

                Spacer(Modifier.height(80.dp))

                Text(
                    text = "Welcome to MountVault!",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )

                Spacer(Modifier.height(80.dp))

                MountVaultTextField(
                    value = uiState.age.trim(),
                    onValueChange = { viewModel.onAgeChange(it.trim()) },
                    label = "Age",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth(0.45f)
                )

                Spacer(Modifier.height(16.dp))

                MountVaultTextField(
                    value = uiState.email.trim(),
                    onValueChange = { viewModel.onEmailChange(it.trim()) },
                    label = "Email"
                )

                Spacer(Modifier.height(16.dp))

                MountVaultTextField(
                    value = uiState.username.trim(),
                    onValueChange = { viewModel.onUsernameChange(it.trim()) },
                    label = "Username"
                )

                Spacer(Modifier.height(16.dp))

                MountVaultTextField(
                    value = uiState.password.trim(),
                    onValueChange = { viewModel.onPasswordChange(it.trim()) },
                    label = "Password",
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        Image(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    passwordHidden = !passwordHidden
                                },
                            painter = painterResource(
                                if (passwordHidden) R.drawable.lock_icon else R.drawable.eye_icon
                            ),
                            contentDescription = "Password Toggle"
                        )
                    }
                )

                Spacer(Modifier.height(16.dp))

                WowButton(
                    text = "Register",
                    fontSize = 16,
                    onClick = { viewModel.onRegisterClicked() },
                    modifier = Modifier
                        .width(180.dp)
                        .height(40.dp)
                )

                Text(
                    text = "* After registering, you can not modify your credentials. *",
                    fontSize = 10.sp
                )

                Spacer(Modifier.weight(1f))

                uiState.errorRegister?.let { MountVaultErrorText(error = it) }
                uiState.emailError?.let { MountVaultErrorText(error = it) }
                uiState.passwordError?.let { MountVaultErrorText(error = it) }
                uiState.ageError?.let { MountVaultErrorText(error = it) }
                uiState.usernameError?.let { MountVaultErrorText(error = it) }

                Spacer(Modifier.height(16.dp))
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.2f))
                        .clickable(enabled = true) { }
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}