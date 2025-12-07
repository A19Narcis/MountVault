package com.narcisdev.mountvault.feature.app.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.narcisdev.mountvault.R

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onLogoutSuccess: () -> Unit,
    backToProfile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLogoutConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onLogoutSuccess()
        }
    }

    Scaffold { padding ->
        Column(
            Modifier.padding(padding)
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.Start)
                    .clickable(enabled = uiState.clickableEnabled) {
                        viewModel.onProfileBackClicked {
                            backToProfile()
                        }
                    },
                painter = painterResource(R.drawable.toniki), contentDescription = null
            )
            Text(text = "SETTINGS")
            Button(onClick = {
                showLogoutConfirm = true

            }) {
                Text("Logout")
            }

            if (showLogoutConfirm) {
                AlertDialog(
                    onDismissRequest = { showLogoutConfirm = false },
                    title = { Text("Cerrar sesión") },
                    text = { Text("¿Estás seguro que quieres cerrar sesión?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showLogoutConfirm = false
                            viewModel.onLogoutClicked()
                        }) {
                            Text("Sí")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutConfirm = false }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }


}

