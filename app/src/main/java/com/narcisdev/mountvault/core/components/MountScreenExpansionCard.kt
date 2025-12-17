package com.narcisdev.mountvault.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.theme.ExpansionColors
import com.narcisdev.mountvault.core.theme.WowFont
import com.narcisdev.mountvault.domain.entity.ExpansionEntity

@Composable
fun MountScreenExpansionCard(
    expansion: ExpansionEntity,
    modifier: Modifier = Modifier
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
            "Vanilla" -> R.drawable.classic_main_logo
            "Tbc" -> R.drawable.tbc_main_logo
            "Wotlk" -> R.drawable.wotlk_main_logo
            "Cataclysm" -> R.drawable.cataclysm_main_logo
            "Pandaria" -> R.drawable.pandaria_main_logo
            "Draenor" -> R.drawable.draenor_main_logo
            "Legion" -> R.drawable.legion_main_logo
            "Bfa" -> R.drawable.bfa_main_logo
            "Shadowlands" -> R.drawable.shadowlands_main_logo
            "Dragonflight" -> R.drawable.dragonflight_main_logo
            "Tww" -> R.drawable.tww_main_logo
            else -> R.drawable.classic_main_logo
        }
    }

    val miniLogo = remember(expansionId) {
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


    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(top = 15.dp, start = 20.dp, end = 20.dp)
            .background(
                brush = Brush.verticalGradient(colors), shape = MaterialTheme.shapes.extraLarge
            )
            .border(
                width = 4.dp,
                brush = Brush.horizontalGradient(colors),
                shape = MaterialTheme.shapes.extraLarge
            ),
        shape = MaterialTheme.shapes.extraLarge, colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
        ) {
            Text(
                text = expansion.name.replace("World of Warcraft: ", ""),
                fontFamily = WowFont,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp)
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.7f)
                        .offset(y = (-10).dp)
                )


                Image(
                    painter = painterResource(miniLogo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.BottomStart)
                        .padding(start = 15.dp, bottom = 15.dp)
                        .size(40.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 8.dp, bottom = 8.dp)
                ) {
                    Text(

                        text = "Mounts: ${expansion.mounts.size}", fontFamily = WowFont
                    )
                    Image(
                        painter = painterResource(R.drawable.dropdown),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(colors[0])
                    )
                }
            }
        }
    }
    Spacer(Modifier.height(8.dp))
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MountCardPreview() {

    val expansion: ExpansionEntity = ExpansionEntity(
        coverUrl = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/expansions/classic/classic.webp",
        id = "vanilla",
        name = "World of Warcraft",
        mounts = listOf("1","2"),
        year = "2004"
    )

    Column() {
        Spacer(Modifier.height(20.dp))
        MountScreenExpansionCard(expansion)
    }
}
