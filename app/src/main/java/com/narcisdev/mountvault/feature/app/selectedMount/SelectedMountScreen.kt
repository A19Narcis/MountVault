package com.narcisdev.mountvault.feature.app.selectedMount

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.components.MountVaultCard
import com.narcisdev.mountvault.core.theme.ExpansionColors
import com.narcisdev.mountvault.core.theme.WowFont
import com.narcisdev.mountvault.domain.entity.MountEntity

@Composable
fun SelectedMountScreen(
    padding: PaddingValues,
    mount: MountEntity,
    viewModel: SelectedMountViewModel,
    navigateBack: () -> Unit
) {

    val colors = remember(mount.expansionId) {
        when (mount.expansionId.replaceFirstChar { it.uppercase() }) {
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

    val backgroundImage = remember(mount.expansionId) {
        when (mount.expansionId.replaceFirstChar { it.uppercase() }) {
            "Vanilla" -> R.drawable.vanilla_background
            "Tbc" -> R.drawable.tbc_background
            "Wotlk" -> R.drawable.wotlk_background
            "Cataclysm" -> R.drawable.cata_background
            "Pandaria" -> R.drawable.panda_background
            "Draenor" -> R.drawable.draenor_background
            "Legion" -> R.drawable.legion_background
            "Bfa" -> R.drawable.bfa_background
            "Shadowlands" -> R.drawable.shadowlands_background
            "Dragonflight" -> R.drawable.dragonflight_background
            "Tww" -> R.drawable.tww_background
            else -> R.drawable.vanilla_background
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(backgroundImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(6.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.06f)
            ) {
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.TopStart)
                        .padding(start = 20.dp, top = 5.dp)
                        .clickable { navigateBack() }
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
            ) {
                MountVaultCard(
                    mount = mount,
                    obtained = true,
                    selectMount = {},
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.8f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        Brush.verticalGradient(colors),
                        shape = RoundedCornerShape(20.dp),
                        alpha = 0.65f
                    )
                    .border(
                        1.dp,
                        brush = Brush.horizontalGradient(colors),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoRow("Source", mount.source)
                InfoRow("Drop rate", "${mount.dropRate}%")
                InfoRow("Rarity", mount.rarity)
                InfoRow("Type", mount.type)
                InfoRow("Cost", mount.cost)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = WowFont,
            fontSize = 14.sp,
            color = Color.White
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = WowFont,
            fontSize = 14.sp,
            color = Color.White
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SelectedMountPreview() {
    val mount = MountEntity(
        cost = "0",
        dropRate = 0.9,
        id = "1",
        name = "Swift Zulian Tiger",
        faction = "Neutral",
        imageUrl = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/mounts/classic/swift-zulian-tiger.webp",
        rarity = "common",
        expansionId = "vanilla",
        source = "Zul'Gurub - High Priest Thekal",
        type = "ground"
    )
    Scaffold { paddingValues ->
        SelectedMountScreen(
            padding = paddingValues,
            mount = mount,
            viewModel = hiltViewModel(key = "selected-mount-$1"),
            navigateBack = {}
        )
    }
}