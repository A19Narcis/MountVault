package com.narcisdev.mountvault.feature.app.selectedPack

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.core.components.MountVaultCardStackSwiper
import com.narcisdev.mountvault.core.components.MountVaultCardSwipeAnimated
import com.narcisdev.mountvault.core.components.MountVaultPackItem
import com.narcisdev.mountvault.core.components.WowButton
import com.narcisdev.mountvault.core.theme.WowFont
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.entity.PackEntity

@Composable
fun SelectedPackScreen(
    packEntity: PackEntity,
    viewModel: SelectedPackViewModel,
    padding: PaddingValues,
    navigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userMounts by viewModel.userMounts.collectAsStateWithLifecycle()
    userMounts.forEach { mountEntity ->
        Log.i(Constants.APP_NAME, "USER MOUNTS: ${mountEntity.name}")
    }
    val allMounts by viewModel.mounts.collectAsStateWithLifecycle()
    var opened by remember { mutableStateOf(false) }
    var swipedCount by remember { mutableIntStateOf(0) }

    var openedMounts by remember {
        mutableStateOf<List<MountEntity>>(emptyList())
    }
    val canShowButton = !opened || swipedCount >= openedMounts.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.back),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .padding(start = 20.dp, top = 5.dp)
                .align(Alignment.Start)
                .clickable { navigateBack() })

        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(24.dp) // altura fija igual al Text
        ) {
            if (uiState.isNewCard) {
                Text(
                    text = "* NEW *",
                    fontFamily = WowFont,
                    color = Color.Red,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (uiState.isNewCard) 1f else 0f)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxWidth(0.7f)
                .aspectRatio(0.7f),
            contentAlignment = Alignment.Center
        ) {
            if (!opened) {
                MountVaultPackItem(
                    pack = packEntity, scale = 1f, isSelected = true, onClick = {})
            } else {
                MountVaultCardSwipeAnimated(
                    mounts = openedMounts,
                    selectedPackViewModel = viewModel,
                    onSwipe = { swipedMount ->
                        swipedCount++
                        viewModel.onCardSeen(swipedMount)
                    },
                )
            }
        }
        Spacer(Modifier.weight(1f))
        WowButton(
            text = if (!opened) "Open" else "Close", fontSize = 16, onClick = {
                if (!opened) {
                    openedMounts = generateRandomMounts(packEntity, allMounts)
                    opened = true
                } else if (swipedCount >= openedMounts.size) {
                    opened = false
                    swipedCount = 0
                }
            }, modifier = Modifier
                .width(180.dp)
                .height(40.dp)
                .graphicsLayer {
                    alpha = if (canShowButton) 1f else 0f
                }, enabled = canShowButton
        )
        Spacer(Modifier.height(20.dp))
    }
}

fun generateRandomMounts(
    pack: PackEntity, allMounts: List<MountEntity>
): List<MountEntity> {

    if (allMounts.isEmpty()) return emptyList()

    val returnList = mutableListOf<MountEntity>()

    repeat(pack.numberOfCards) {

        val random = (0 until 100).random()

        val common = pack.commonDropChange.toInt()
        val rare = pack.rareDropChange.toInt()
        val epic = pack.epicDropChange.toInt()

        val rarity = when {
            random < common -> "common"
            random < common + rare -> "rare"
            random < common + rare + epic -> "epic"
            else -> "legendary"
        }

        val mount = allMounts.filter { it.rarity == rarity }.randomOrNull()

        mount?.let {
            returnList.add(it)
        }
    }

    return returnList.sortedWith(
        compareBy {
            when (it.rarity) {
                "common" -> 0
                "rare" -> 1
                "epic" -> 2
                "legendary" -> 3
                else -> 4
            }
        }
    )
}
