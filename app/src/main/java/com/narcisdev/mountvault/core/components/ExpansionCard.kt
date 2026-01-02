package com.narcisdev.mountvault.core.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.theme.ExpansionColors
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity

@Composable
fun ExpansionCard(
    expansion: ExpansionEntity,
    expansionMounts: List<MountEntity>,
    userMountsForExpansion: List<MountEntity>
) {
    val expansionId = expansion.id
    val shape = MaterialTheme.shapes.extraLarge

    val colors = remember(expansionId) {
        when (expansionId.replaceFirstChar { it.uppercase() }) {
            "Vanilla" -> ExpansionColors.Vanilla
            "Tbc" -> ExpansionColors.BurningCrusade
            "Wotlk" -> ExpansionColors.Wrath
            "Cataclysm" -> ExpansionColors.Cataclysm
            "Pandaria" -> ExpansionColors.Pandaria
            "Draenor" -> ExpansionColors.Draenor
            "Legion" -> ExpansionColors.Legion
            "Bfa" -> ExpansionColors.BFA
            "Shadowlands" -> ExpansionColors.Shadowlands
            "Dragonflight" -> ExpansionColors.Dragonflight
            "Tww" -> ExpansionColors.TWW
            else -> ExpansionColors.Vanilla
        }
    }

    val image = remember(expansionId) {
        when (expansionId.replaceFirstChar { it.uppercase() }) {
            "Vanilla" -> R.drawable.classic
            "Tbc" -> R.drawable.tbc
            "Wotlk" -> R.drawable.wotlk
            "Cataclysm" -> R.drawable.cata
            "Pandaria" -> R.drawable.panda
            "Draenor" -> R.drawable.draenor
            "Legion" -> R.drawable.legion
            "Bfa" -> R.drawable.bfa
            "Shadowlands" -> R.drawable.shadow
            "Dragonflight" -> R.drawable.dragon
            "Tww" -> R.drawable.tww
            else -> R.drawable.classic
        }
    }

    val progress = remember(expansionId, userMountsForExpansion) {
        calcularProgress(expansion, userMountsForExpansion)
    }

    val backgroundBrush: Brush? = remember(expansionId, expansionMounts.size == userMountsForExpansion.size) {
        if (expansionMounts.size == userMountsForExpansion.size) {
            Brush.horizontalGradient(
                colors.map { it.copy(alpha = 0.55f) }
            )
        } else {
            null
        }
    }

    Card(
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(
                width = 2.dp,
                brush = Brush.horizontalGradient(colors),
                shape = shape
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .then(
                    if (backgroundBrush != null) {
                        Modifier.background(backgroundBrush)
                    } else {
                        Modifier
                    }
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 20.dp)
            ) {
                Image(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(50.dp),
                    painter = painterResource(image),
                    contentDescription = null
                )

                Column {
                    val name = if (expansion.name.contains(":")) {
                        expansion.name.replace("World of Warcraft: ", "")
                    } else {
                        expansion.name
                    }

                    Text(
                        text = name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Mounts: ${userMountsForExpansion.size}/${expansionMounts.size}",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic
                    )

                    ExpansionMountsProgressBar(
                        progress = progress,
                        colors = colors
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(16.dp))
}
private fun calcularProgress(
    entity: ExpansionEntity, userMountsForExpansion: List<MountEntity>
): Float {
    val userMounts = userMountsForExpansion.size
    if (userMounts == 0) return 0f
    return userMounts / entity.mounts.size.toFloat()
}

@Preview
@Composable
fun ExpansionCardPreview() {
    val expansion = ExpansionEntity(
        coverUrl = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/expansions/classic/classic.webp",
        id = "vanilla",
        name = "World of Warcraft",
        mounts = listOf("1", "2"),
        year = "2004"
    )
    ExpansionCard(
        expansion = expansion, expansionMounts = listOf(), userMountsForExpansion = listOf()
    )
}