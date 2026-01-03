package com.narcisdev.mountvault.feature.app.expansionMounts

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.overscroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.components.MountVaultCard
import com.narcisdev.mountvault.core.components.MountVaultRankBadge
import com.narcisdev.mountvault.core.navigation.Routes
import com.narcisdev.mountvault.core.theme.WowFont
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.feature.app.navigationBar.MyNavigationBar

@Composable
fun ExpansionMountsScreen(
    padding: PaddingValues,
    expansion: ExpansionEntity,
    viewModel: ExpansionMountsViewModel,
    expansionMounts: List<MountEntity>,
    onMountClicked: (MountEntity) -> Unit,
    navigateBack: () -> Unit
) {
    //val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userMounts by viewModel.userMounts.collectAsStateWithLifecycle()
    val rarities = listOf("legendary", "epic", "rare", "common")
    var selectedRarities by remember { mutableStateOf(setOf<String>()) }

    val image = remember(expansion.id) {
        when (expansion.id.replaceFirstChar { it.uppercase() }) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f)
        ) {
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.TopStart)
                    .padding(start = 20.dp, top = 5.dp)
                    .clickable {
                        navigateBack()
                    })

            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .align(Alignment.Center)
            )

            Text(
                text = "Mounts: ${expansionMounts.filter { it.expansionId == expansion.id }.size}",
                fontFamily = WowFont,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 5.dp)
            )
        }

        val filteredMounts = remember(selectedRarities, expansionMounts) {
            expansionMounts.filter { it.expansionId == expansion.id }
                .filter { selectedRarities.isEmpty() || selectedRarities.contains(it.rarity) }
                .sortedWith(
                    compareBy {
                        when (it.rarity) {
                            "legendary" -> 0
                            "epic" -> 1
                            "rare" -> 2
                            "common" -> 3
                            else -> 4
                        }
                    })
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            rarities.forEach { rarity ->
                MountVaultRankBadge(
                    rank = rarity,
                    isSelected = selectedRarities.contains(rarity),
                    modifier = Modifier
                        .size(50.dp)
                        .clickable(
                            indication = null,
                            interactionSource = androidx.compose.foundation.interaction.MutableInteractionSource()
                        ) {
                            selectedRarities = if (selectedRarities.contains(rarity)) {
                                selectedRarities - rarity
                            } else {
                                selectedRarities + rarity
                            }
                        })
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .overscroll(null)
        ) {
            items(filteredMounts) { mount ->
                val obtained: Boolean = userMounts.any { it.id == mount.id }
                MountVaultCard(mount = mount, obtained = obtained) {
                    onMountClicked(mount)
                }
            }
        }
    }
}
