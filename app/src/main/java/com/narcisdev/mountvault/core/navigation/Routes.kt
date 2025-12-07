package com.narcisdev.mountvault.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes : NavKey {
    @Serializable
    data object Login : Routes()

    @Serializable
    data object Register: Routes()

    @Serializable
    data object Main : Routes()

    @Serializable
    data object Mounts: Routes()

    @Serializable
    data object Profile: Routes()

    @Serializable
    data object Settings: Routes()
}
