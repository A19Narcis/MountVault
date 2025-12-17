package com.narcisdev.mountvault.core.navigation

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.entity.UserEntity
import com.narcisdev.mountvault.feature.app.expansionMounts.ExpansionMountsScreen
import com.narcisdev.mountvault.feature.app.expansionMounts.ExpansionMountsViewModel
import com.narcisdev.mountvault.feature.app.main.MainScreen
import com.narcisdev.mountvault.feature.app.main.MainViewModel
import com.narcisdev.mountvault.feature.app.mounts.MountsScreen
import com.narcisdev.mountvault.feature.app.mounts.MountsViewModel
import com.narcisdev.mountvault.feature.app.navigationBar.MyNavigationBar
import com.narcisdev.mountvault.feature.app.profile.ProfileScreen
import com.narcisdev.mountvault.feature.app.profile.ProfileViewModel
import com.narcisdev.mountvault.feature.app.selectedMount.SelectedMountScreen
import com.narcisdev.mountvault.feature.app.selectedMount.SelectedMountViewModel
import com.narcisdev.mountvault.feature.auth.login.LoginScreen
import com.narcisdev.mountvault.feature.auth.login.LoginViewModel
import com.narcisdev.mountvault.feature.auth.register.RegisterScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MountVaultAppNav3(userPreferencesDataSource: UserPreferencesDataSource) {
    val backStack = remember { mutableStateListOf<Routes>() }
    val context = LocalContext.current
    var user by remember { mutableStateOf<UserEntity?>(null) }
    var logoutCounter by remember { mutableIntStateOf(0) }
    var profileCounter by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        userPreferencesDataSource.clearOnFirstRun(context, userPreferencesDataSource)

        user = userPreferencesDataSource.getUserLocalOnce()
        backStack.clear()
        backStack.add(if (user != null) Routes.Main else Routes.Login)
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


    var showBottomBar by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MyNavigationBar(
                    currentRoute = currentRoute,
                    backStack = backStack
                )
            }
        }
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
                    val loginViewModel: LoginViewModel = hiltViewModel(key = "login-$logoutCounter")

                    LoginScreen(
                        viewModel = loginViewModel,
                        onLoginSuccess = {
                            CoroutineScope(Dispatchers.Main).launch {
                                user = userPreferencesDataSource.getUserLocalOnce()
                                //Load data from Firebase to Room
                                loginViewModel.loadFromFirebaseIntoRoom {
                                    profileCounter++
                                    backStack.clear()
                                    backStack.add(Routes.Main)
                                }
                            }
                        },
                        onRegisterClicked = {
                            backStack.add(Routes.Register)
                        }
                    )
                }
                entry<Routes.Register> {
                    RegisterScreen(
                        backToLogin = { backStack.removeLastOrNull() },
                        navigateToMain = {
                            CoroutineScope(Dispatchers.Main).launch {
                                user = userPreferencesDataSource.getUserLocalOnce()
                            }
                            backStack.clear()
                            backStack.add(Routes.Main)
                        }
                    )
                }
                entry<Routes.Main> {
                    val mainViewModel: MainViewModel = hiltViewModel(key = "main-$logoutCounter")
                    MainScreen(
                        viewModel = mainViewModel,
                        padding = padding
                    )
                }
                entry<Routes.Mounts> {
                    val mountsViewModel: MountsViewModel = hiltViewModel(key = "mounts-$logoutCounter")
                    MountsScreen(
                        padding,
                        mountsViewModel
                    ) { mounts, expansion ->
                        backStack.add(Routes.ExpansionMounts(mounts = mounts, expansion = expansion))
                    }
                }
                entry<Routes.ExpansionMounts> { route ->
                    val expansionMountsViewModel: ExpansionMountsViewModel = hiltViewModel(key = "expansion-mounts-$logoutCounter")
                    ExpansionMountsScreen(
                        padding = padding,
                        viewModel = expansionMountsViewModel,
                        expansion = route.expansion,
                        mounts = route.mounts,
                        onMountClicked = { mount ->
                            backStack.add(Routes.SelectedMount(mount = mount))
                        },
                        navigateBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
                entry<Routes.SelectedMount> { route ->
                    val selectedMountViewModel: SelectedMountViewModel = hiltViewModel(key = "selected-mount-$logoutCounter")
                    SelectedMountScreen (
                        padding = padding,
                        mount = route.mount,
                        viewModel = selectedMountViewModel,
                        navigateBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
                entry<Routes.Profile> {
                    val profileViewModel: ProfileViewModel = hiltViewModel(
                        key = "profile-$profileCounter"
                    )

                    ProfileScreen(
                        viewModel = profileViewModel,
                        userLocal = user,
                        padding = padding,
                        onEditModeChange = { isEditing ->
                            showBottomBar = !isEditing
                        },
                        onLogoutSuccess = {
                            backStack.clear()
                            backStack.add(Routes.Login)
                            logoutCounter++
                        }
                    )
                }
            },
            transitionSpec = {
                slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(500)
                ) togetherWith slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(500)
                )
            },
            popTransitionSpec = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(500)
                ) togetherWith slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(500)
                )
            },
            predictivePopTransitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(600)
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(600)
                )
            }
        )
    }
}

