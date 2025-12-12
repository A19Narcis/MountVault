package com.narcisdev.mountvault.core.components

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.theme.ExpansionColors
import com.narcisdev.mountvault.core.theme.WowFont
import com.narcisdev.mountvault.domain.entity.MountEntity

@Composable
fun MountVaultCard(mount: MountEntity) {

    val expansionId = mount.expansionId

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

    Box(
        modifier = Modifier
            .fillMaxWidth(0.33f)
            .height(160.dp)
            .padding(start = 5.dp, end = 5.dp)
    ) {

        // --- CARD ---
        Card(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(colors),
                    shape = RoundedCornerShape(10)
                )
                .border(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(colors),
                    shape = RoundedCornerShape(10)
                ),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = mount.name,
                    fontFamily = WowFont,
                    maxLines = 1,
                    autoSize = TextAutoSize.StepBased(
                        minFontSize = 3.sp,
                        maxFontSize = 10.sp,
                    ),
                    modifier = Modifier.padding(start = 18.dp)
                )
                Spacer(Modifier.height(3.dp))
                Image(
                    painter = painterResource(R.drawable.swift_zulian_tiger),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, brush = Brush.horizontalGradient(colors), RoundedCornerShape(4.dp))
                )
            }
        }

        MountVaultRankBadge(
            rank = mount.rarity,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.TopStart)
                .padding(start = 4.dp, top = 4.dp)
                .zIndex(100f)
        )

        Image(
            painter = painterResource(R.drawable.classic),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.BottomStart)
                .zIndex(100f)
                .padding(start = 10.dp, bottom = 10.dp)
        )
    }
}







@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MountVaultCardPreview() {
    val mounts = listOf(
        MountEntity(
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
        ),
        MountEntity(
            cost = "0",
            dropRate = 0.9,
            id = "1",
            name = "Swift Zulian Tiger",
            faction = "Neutral",
            imageUrl = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/mounts/classic/swift-zulian-tiger.webp",
            rarity = "epic",
            expansionId = "tbc",
            source = "Zul'Gurub - High Priest Thekal",
            type = "ground"
        ),
        MountEntity(
            cost = "0",
            dropRate = 0.9,
            id = "1",
            name = "Swift Zulian Tiger",
            faction = "Neutral",
            imageUrl = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/mounts/classic/swift-zulian-tiger.webp",
            rarity = "legendary",
            expansionId = "wotlk",
            source = "Zul'Gurub - High Priest Thekal",
            type = "ground"
        ),
        MountEntity(
            cost = "0",
            dropRate = 0.9,
            id = "1",
            name = "Swift Zulian Tiger",
            faction = "Neutral",
            imageUrl = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/mounts/classic/swift-zulian-tiger.webp",
            rarity = "legendary",
            expansionId = "cataclysm",
            source = "Zul'Gurub - High Priest Thekal",
            type = "ground"
        ),
        MountEntity(
            cost = "0",
            dropRate = 0.9,
            id = "1",
            name = "Swift Zulian Tiger",
            faction = "Neutral",
            imageUrl = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/mounts/classic/swift-zulian-tiger.webp",
            rarity = "rare",
            expansionId = "pandaria",
            source = "Zul'Gurub - High Priest Thekal",
            type = "ground"
        ),
        MountEntity(
            cost = "0",
            dropRate = 0.9,
            id = "1",
            name = "Swift Zulian Tiger",
            faction = "Neutral",
            imageUrl = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/mounts/classic/swift-zulian-tiger.webp",
            rarity = "legendary",
            expansionId = "draenor",
            source = "Zul'Gurub - High Priest Thekal",
            type = "ground"
        ),
        MountEntity(
            cost = "0",
            dropRate = 0.9,
            id = "1",
            name = "Swift Zulian Tiger",
            faction = "Neutral",
            imageUrl = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/mounts/classic/swift-zulian-tiger.webp",
            rarity = "legendary",
            expansionId = "legion",
            source = "Zul'Gurub - High Priest Thekal",
            type = "ground"
        ),
        MountEntity(
            cost = "0",
            dropRate = 0.9,
            id = "1",
            name = "Swift Zulian Tiger",
            faction = "Neutral",
            imageUrl = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/mounts/classic/swift-zulian-tiger.webp",
            rarity = "legendary",
            expansionId = "bfa",
            source = "Zul'Gurub - High Priest Thekal",
            type = "ground"
        ),
        MountEntity(
            cost = "0",
            dropRate = 0.9,
            id = "1",
            name = "Swift Zulian Tiger",
            faction = "Neutral",
            imageUrl = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/mounts/classic/swift-zulian-tiger.webp",
            rarity = "legendary",
            expansionId = "shadowlands",
            source = "Zul'Gurub - High Priest Thekal",
            type = "ground"
        ),
        MountEntity(
            cost = "0",
            dropRate = 0.9,
            id = "1",
            name = "Swift Zulian Tiger",
            faction = "Neutral",
            imageUrl = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/mounts/classic/swift-zulian-tiger.webp",
            rarity = "legendary",
            expansionId = "dragonflight",
            source = "Zul'Gurub - High Priest Thekal",
            type = "ground"
        ),
        MountEntity(
            cost = "0",
            dropRate = 0.9,
            id = "1",
            name = "Swift Zulian Tiger",
            faction = "Neutral",
            imageUrl = "https://ebmwaaoknfipdeingrue.supabase.co/storage/v1/object/public/mountVault/mounts/classic/swift-zulian-tiger.webp",
            rarity = "legendary",
            expansionId = "tww",
            source = "Zul'Gurub - High Priest Thekal",
            type = "ground"
        )
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(top = 50.dp)
    ) {

        items(mounts.size) { index ->
            MountVaultCard(mounts[index])
        }
    }

}