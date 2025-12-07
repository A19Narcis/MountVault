package com.narcisdev.mountvault.core.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import com.narcisdev.mountvault.domain.entity.UserEntity
import com.narcisdev.mountvault.feature.app.main.MainScreen
import com.narcisdev.mountvault.feature.app.mounts.MountsScreen
import com.narcisdev.mountvault.feature.app.navigationBar.MyNavigationBar
import com.narcisdev.mountvault.feature.app.profile.ProfileScreen
import com.narcisdev.mountvault.feature.app.settings.SettingsScreen
import com.narcisdev.mountvault.feature.auth.login.LoginScreen
import com.narcisdev.mountvault.feature.auth.register.RegisterScreen
import kotlinx.coroutines.delay

@Composable
fun MountVaultAppNav3(userPreferencesDataSource: UserPreferencesDataSource) {
    val backStack = remember { mutableStateListOf<Routes>() }
    val context = LocalContext.current
    var user by remember { mutableStateOf<UserEntity?>(null) }
    var logoutCounter by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        user = userPreferencesDataSource.getUserLocalOnce()
        if (user != null) {
            backStack.clear()
            backStack.add(Routes.Main)
        } else {
            backStack.clear()
            backStack.add(Routes.Login)
        }
    }

    if (user == null && backStack.isEmpty()) {
        // Se puede hacer una pantalla de carga aquí
        return
    }

    val bottomBarRoutes = listOf(Routes.Main, Routes.Mounts, Routes.Profile)
    val currentRoute: Routes? = backStack.lastOrNull()

    var showExitConfirm by remember { mutableStateOf(false) }

    BackHandler(enabled = backStack.isNotEmpty()) {
        val current = backStack.lastOrNull()

        when {
            // BottomBar que no sea Main → volver a Main
            current != Routes.Main && current in bottomBarRoutes -> {
                backStack.removeAll { it in bottomBarRoutes && it != Routes.Main }
                if (backStack.lastOrNull() != Routes.Main) backStack.add(Routes.Main)
            }

            // Main → mostrar AlertDialog de confirmación
            current == Routes.Main -> {
                showExitConfirm = true
            }

            // Register → volver a Login
            current == Routes.Register -> {
                backStack.removeLastOrNull() // queda Login arriba
            }

            // Login → back normal, dejamos que Android maneje la salida
            current == Routes.Login -> {
                (context as? Activity)?.moveTaskToBack(true)
            }
        }
    }


    Scaffold(
        bottomBar = { MyNavigationBar(
            currentRoute = currentRoute,
            backStack = backStack
        ) }
    ) { padding ->

        if (showExitConfirm) {
            AlertDialog(
                onDismissRequest = { showExitConfirm = false },
                title = { Text("Salir de la app") },
                text = { Text("¿Estás seguro que quieres salir?") },
                confirmButton = {
                    TextButton(onClick = { (context as? Activity)?.finish() }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitConfirm = false }) {
                        Text("No")
                    }
                }
            )
        }

        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.size == 1) {
                    (context as? Activity)?.finish()
                } else {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider = entryProvider {
                entry<Routes.Login> {
                    LoginScreen(
                        viewModel = hiltViewModel(key = "login-$logoutCounter"),
                        onLoginSuccess = {
                            backStack.clear()
                            backStack.add(Routes.Main)
                        },
                        onRegisterClicked = {
                            backStack.add(Routes.Register)
                        }
                    )
                }
                entry<Routes.Register> {
                    RegisterScreen(
                        viewModel = hiltViewModel(key = "register-$logoutCounter"),
                        backToLogin = { backStack.removeLastOrNull() },
                        navigateToMain = {
                            backStack.clear()
                            backStack.add(Routes.Main)
                        }
                    )
                }
                entry<Routes.Main> {
                    MainScreen(padding)
                }
                entry<Routes.Mounts> {
                    MountsScreen(padding)
                }
                entry<Routes.Profile> {
                    ProfileScreen(padding = padding, goToSettings = {
                        backStack.add(Routes.Settings)
                    })
                }
                entry<Routes.Settings> {
                    SettingsScreen(
                        viewModel = hiltViewModel(key = "settings-$logoutCounter"),
                        backToProfile = { backStack.removeLastOrNull() },
                        onLogoutSuccess = {
                            backStack.clear()
                            backStack.add(Routes.Login)
                            logoutCounter++
                        }
                    )
                }
            }
        )
    }
}

