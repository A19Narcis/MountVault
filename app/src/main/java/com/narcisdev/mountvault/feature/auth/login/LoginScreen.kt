package com.narcisdev.mountvault.feature.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.components.MountVaultErrorText
import com.narcisdev.mountvault.core.components.MountVaultTextField
import com.narcisdev.mountvault.core.components.WowButton

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    var passwordHidden by remember { mutableStateOf(true) }

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
        }
    }

    Scaffold { padding ->
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
            Spacer(Modifier.height(80.dp))
            Image(modifier = Modifier.size(190.dp), painter = painterResource(R.drawable.main_warcraft_icon), contentDescription = "Login APP LOGO")
            Spacer(Modifier.height(80.dp))
            MountVaultTextField(
                value = uiState.userEntry,
                onValueChange = { viewModel.onUserEntryChange(it.trim()) },
                label = "Email or Username",
                keyboardType = KeyboardType.Email
            )
            Spacer(Modifier.height(16.dp))
            MountVaultTextField(
                value = uiState.password,
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
            Spacer(Modifier.weight(1f))
            WowButton(
                text = "Login",
                fontSize = 16,
                onClick = { viewModel.onLoginClick() },
                modifier = Modifier
                    .width(180.dp)
                    .height(40.dp)
            )
            Spacer(Modifier.height(8.dp))
            WowButton(
                text = "Register",
                fontSize = 16,
                onClick = { onRegisterClicked() },
                modifier = Modifier
                    .width(180.dp)
                    .height(40.dp)
            )
            Spacer(Modifier.weight(1f))
            uiState.error?.let { error ->
                MountVaultErrorText(error = error)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
