package com.narcisdev.mountvault.core.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(
                width = 2.dp,
                brush = Brush.horizontalGradient(colors),
                shape = MaterialTheme.shapes.extraLarge
            ),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
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
                var name = expansion.name
                if (expansion.name.contains(":")){
                    name = expansion.name.replace("World of Warcraft: ", "")
                }
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Mounts: " + userMountsForExpansion.size + "/" + expansionMounts.size,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )

                ExpansionMountsProgressBar(
                    progress,
                    colors = colors
                )
            }
        }
    }
    Spacer(Modifier.height(16.dp))
}

private fun calcularProgress(entity: ExpansionEntity, userMountsForExpansion: List<MountEntity>): Float {
    val userMounts = userMountsForExpansion.size
    if (userMounts == 0) return 0f
    return userMounts / entity.mounts.size.toFloat()
}