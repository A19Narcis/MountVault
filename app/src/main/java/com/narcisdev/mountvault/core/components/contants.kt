package com.narcisdev.mountvault.core.components

import androidx.compose.ui.graphics.Color
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.theme.ExpansionColors
import com.narcisdev.mountvault.domain.entity.PackEntity

object Constants {
    const val APP_NAME = "MountVault_APP_NAME"
    const val NORMAL_USER = "NORMAL"
    const val ADMIN_USER = "ADMIN"
    const val DEFAULT_PROFILE_IMG = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/avatars/default_medivh.webp"
    const val LOGIN_FAILED_MSG: String = "Usuario o contraseña incorrecto"
    const val NOT_FOUND_USER: String = "Usuario no encontrado"
    const val REGISTER_FAILED_MSG: String = "Nombre de usuario o Email ya registrado"

    const val NAV_LOGIN_SCREEN_NAME = "login"
    const val NAV_HOME_SCREEN_NAME = "home"

    enum class Expansion(val key: String) {
        Vanilla("vanilla"),
        BurningCrusade("tbc"),
        Wrath("wotlk"),
        Cataclysm("cata"),
        Pandaria("pandaria"),
        Draenor("draenor"),
        Legion("legion"),
        BFA("bfa"),
        Shadowlands("shadowlands"),
        Dragonflight("dragonflight"),
        TWW("tww")
    }

    val expansionColorMap = mapOf(
        Expansion.Vanilla to ExpansionColors.Vanilla,
        Expansion.BurningCrusade to ExpansionColors.BurningCrusade,
        Expansion.Wrath to ExpansionColors.Wrath,
        Expansion.Cataclysm to ExpansionColors.Cataclysm,
        Expansion.Pandaria to ExpansionColors.Pandaria,
        Expansion.Draenor to ExpansionColors.Draenor,
        Expansion.Legion to ExpansionColors.Legion,
        Expansion.BFA to ExpansionColors.BFA,
        Expansion.Shadowlands to ExpansionColors.Shadowlands,
        Expansion.Dragonflight to ExpansionColors.Dragonflight,
        Expansion.TWW to ExpansionColors.TWW
    )

    val drawableListExpansionMiniLogo = listOf(
        R.drawable.classic,
        R.drawable.tbc,
        R.drawable.wotlk,
        R.drawable.cata,
        R.drawable.panda,
        R.drawable.draenor,
        R.drawable.legion,
        R.drawable.bfa,
        R.drawable.shadow,
        R.drawable.dragon,
        R.drawable.tww
    )

    fun getExpansionColorsFromUrl(url: String?, colorMap: Map<Expansion, List<Color>>): List<Color> {
        val expansion = detectExpansionFromUrl(url)
        return expansion?.let { colorMap[it] } ?: ExpansionColors.Vanilla
    }

    fun detectExpansionFromUrl(url: String?): Expansion? {
        if (url.isNullOrEmpty()) return null
        val normalized = url.lowercase()
        return Expansion.entries.firstOrNull { expansion ->
            normalized.contains(expansion.key)
        }
    }

    fun getAllPacks(): List<PackEntity> {
        return listOf(
            PackEntity(
                id = 1,
                name = "Normal",
                numberOfCards = 5,
                commonDropChange = "50",
                rareDropChange = "30",
                epicDropChange = "15",
                legendaryDropChange = "5",
            ),
            PackEntity(
                id = 2,
                name = "Epic",
                numberOfCards = 7,
                commonDropChange = "40",
                rareDropChange = "30",
                epicDropChange = "15",
                legendaryDropChange = "10",
            ),
            PackEntity(
                id = 3,
                name = "Mythic",
                numberOfCards = 10,
                commonDropChange = "40",
                rareDropChange = "25",
                epicDropChange = "20",
                legendaryDropChange = "15",
            )
        )
    }

}
