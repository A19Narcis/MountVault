package com.narcisdev.mountvault.feature.app.navigationBar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.navigation.Routes

@Composable
fun MyNavigationBar(
    currentRoute: Routes?,
    backStack: MutableList<Routes>
) {
    val bottomBarRoutes = listOf(Routes.Main, Routes.Mounts, Routes.Profile)

    if (currentRoute in bottomBarRoutes) {
        NavigationBar {
            bottomBarRoutes.forEach { route ->
                NavigationBarItem(
                    selected = currentRoute == route,
                    onClick = {
                        if (currentRoute == route) return@NavigationBarItem

                        if (route == Routes.Main) {
                            // Mantener Main como raíz única
                            backStack.removeAll { it in bottomBarRoutes && it != Routes.Main }
                            if (backStack.lastOrNull() != Routes.Main) backStack.add(Routes.Main)
                        } else {
                            // Reemplazar la top route si es de BottomBar
                            if (backStack.lastOrNull() in bottomBarRoutes) backStack.removeLastOrNull()
                            backStack.add(route)
                        }
                    },
                    icon = {
                        when (route) {
                            Routes.Main -> Image(painter = painterResource(R.drawable.toniki), contentDescription = null)
                            Routes.Mounts -> Image(painter = painterResource(R.drawable.toniki), contentDescription = null)
                            Routes.Profile -> Image(painter = painterResource(R.drawable.toniki), contentDescription = null)
                            else -> {}
                        }
                    },
                )
            }
        }
    }
}