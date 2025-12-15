package com.narcisdev.mountvault.feature.app.navigationBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.navigation.Routes

@Composable
fun MyNavigationBar(
    currentRoute: Routes?, backStack: MutableList<Routes>
) {
    val bottomBarRoutes = listOf(Routes.Main, Routes.Mounts, Routes.Profile)

    if (currentRoute in bottomBarRoutes) {
        NavigationBar(
            modifier = Modifier.height(70.dp),
        ) {
            bottomBarRoutes.forEach { route ->
                NavigationBarItem(
                    modifier = Modifier.height(35.dp),
                    selected = currentRoute == route,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    ),
                    onClick = {
                        if (currentRoute == route) return@NavigationBarItem

                        if (route == Routes.Main) {
                            backStack.removeAll { it in bottomBarRoutes && it != Routes.Main }
                            if (backStack.lastOrNull() != Routes.Main) backStack.add(Routes.Main)
                        } else {
                            if (backStack.lastOrNull() in bottomBarRoutes) backStack.removeLastOrNull()
                            backStack.add(route)
                        }
                    },
                    icon = {
                        when (route) {
                            Routes.Main -> BadgedBox(badge = {}) {
                                Image(
                                    painter = painterResource(R.drawable.homestone),
                                    contentDescription = ""
                                )
                            }

                            Routes.Mounts -> BadgedBox(badge = {}) {
                                Image(
                                    modifier = Modifier.clip(RoundedCornerShape(50)),
                                    painter = painterResource(R.drawable.random_mount_icon),
                                    contentDescription = ""
                                )
                            }

                            Routes.Profile -> BadgedBox(badge = {}) {
                                Image(
                                    painter = painterResource(R.drawable.hearthstone_icon),
                                    contentDescription = ""
                                )
                            }

                            else -> {}
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun MyBadge(modifier: Modifier = Modifier) {
    Badge(
        modifier = modifier,
        contentColor = Color.Green,
        containerColor = Color.Blue,
    ) {

    }
}