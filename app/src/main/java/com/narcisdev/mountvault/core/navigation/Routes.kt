package com.narcisdev.mountvault.core.navigation

import androidx.navigation3.runtime.NavKey
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity
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
    data class ExpansionMounts(val mounts: List<MountEntity>, val expansion: ExpansionEntity): Routes()

    @Serializable
    data class SelectedMount(val mount: MountEntity): Routes()
}
