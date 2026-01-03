package com.narcisdev.mountvault.feature.app.main

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.core.components.MountVaultPackCarousel
import com.narcisdev.mountvault.core.navigation.Routes
import com.narcisdev.mountvault.core.theme.WowFont
import com.narcisdev.mountvault.core.theme.rankALight
import com.narcisdev.mountvault.core.theme.rankBLight
import com.narcisdev.mountvault.core.theme.rankCLight
import com.narcisdev.mountvault.core.theme.rankSLight
import com.narcisdev.mountvault.core.theme.wow_darkBlue
import com.narcisdev.mountvault.domain.entity.PackEntity
import com.narcisdev.mountvault.feature.app.navigationBar.MyNavigationBar

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    padding: PaddingValues,
    navigateToSelectedPack: (PackEntity) -> Unit
) {
    var selectedPack by remember {
        mutableStateOf(Constants.getAllPacks().first())
    }

    Column(
        Modifier
            .padding(padding)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.15f)
        ) {
            Image(
                painter = painterResource(R.drawable.main_warcraft_icon),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .fillMaxWidth()
        ) {
            MountVaultPackCarousel(
                packs = Constants.getAllPacks(),
                onPackSelected = { pack ->
                    selectedPack = pack
                },
                onPackClick = {
                    navigateToSelectedPack(selectedPack)
                },
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = wow_darkBlue.copy(alpha = 1f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(
                        text = selectedPack.numberOfCards.toString() + " Cards",
                        fontFamily = WowFont,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    ChanceRow("Common", selectedPack.commonDropChange, rankCLight)
                    ChanceRow("Rare", selectedPack.rareDropChange, rankBLight)
                    ChanceRow("Epic", selectedPack.epicDropChange, rankALight)
                    ChanceRow("Legendary", selectedPack.legendaryDropChange, rankSLight)
                }
            }
        }
    }
}

@Composable
fun ChanceRow(label: String, chance: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = color, fontFamily = WowFont)
        Text("${chance}%", color = color, fontFamily = WowFont)
    }
}

@Preview(
    name = "Light",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    device = Devices.PIXEL_9_PRO
)
@Composable
fun PreviewLoginLight() {
    Scaffold (
        bottomBar = { MyNavigationBar(currentRoute = Routes.Main, backStack = mutableListOf()) }
    ){ paddingValues ->
        MainScreen(
            viewModel = hiltViewModel(key = "main-$1"),
            padding = paddingValues
        ) {}
    }
}