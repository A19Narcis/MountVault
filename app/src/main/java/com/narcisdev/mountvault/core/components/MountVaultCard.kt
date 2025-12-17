package com.narcisdev.mountvault.core.components

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.theme.ExpansionColors
import com.narcisdev.mountvault.core.theme.WowFont
import com.narcisdev.mountvault.domain.entity.MountEntity

@Composable
fun MountVaultCard(
    mount: MountEntity,
    obtained: Boolean,
    modifier: Modifier = Modifier,
    selectMount: () -> Unit = {}
) {

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

    val expansionIcon = remember(expansionId) {
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

    val grayscaleColorFilter = if (!obtained) {
        ColorFilter.colorMatrix(
            ColorMatrix().apply {
                setToSaturation(0f)
            }
        )
    } else {
        null
    }

    Box(
        modifier = modifier
            .fillMaxWidth(0.50f)
            .height(300.dp)
            .padding(start = 2.dp, end = 2.dp)
    ) {

        Card(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    this.colorFilter = grayscaleColorFilter
                    if (!obtained) alpha = 0.7f
                }
                .background(
                    brush = Brush.verticalGradient(colors),
                    shape = RoundedCornerShape(6)
                )
                .border(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(colors),
                    shape = RoundedCornerShape(6)
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    selectMount()
                },
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MountVaultRankBadge(
                        rank = mount.rarity,
                        modifier = Modifier
                            .size(25.dp)
                            .zIndex(100f)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = mount.name,
                        fontFamily = WowFont,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 12.sp,
                            maxFontSize = 12.sp,
                        ),
                    )
                }
                Spacer(Modifier.height(4.dp))
                AsyncImage(
                    model = mount.imageUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Mount picture",
                            modifier = Modifier
                            .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, brush = Brush.horizontalGradient(colors), RoundedCornerShape(4.dp))
                )
            }
        }

        Image(
            painter = painterResource(expansionIcon),
            contentDescription = null,
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.BottomStart)
                .zIndex(100f)
                .padding(start = 12.dp, bottom = 12.dp)
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
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(top = 50.dp)
    ) {

        items(mounts.size) { index ->
            MountVaultCard(mounts[index], true)
        }
    }

}