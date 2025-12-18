package com.narcisdev.mountvault.core.components

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.narcisdev.mountvault.domain.entity.MountEntity
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun MountVaultCardStackSwiper(
    mounts: List<MountEntity>,
    onSwipe: (MountEntity) -> Unit
) {
    var cards by remember { mutableStateOf(mounts) }
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val visibleCards = cards.take(4)
        val topCard = visibleCards.lastOrNull()

        visibleCards.forEachIndexed { index, mount ->
            val isTop = mount == topCard

            MountVaultCard(
                mount = mount,
                obtained = true,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(index.toFloat())
                    .graphicsLayer {
                        scaleX = 1f - (visibleCards.size - 1 - index) * 0.015f
                        scaleY = 1f - (visibleCards.size - 1 - index) * 0.015f
                        translationY = (visibleCards.size - 1 - index) * 6f
                        if (isTop) translationX = offsetX.value
                    }
                    .then(
                        if (isTop)
                            Modifier.pointerInput(Unit) {
                                detectDragGestures(
                                    onDrag = { _, dragAmount ->
                                        scope.launch {
                                            offsetX.snapTo(offsetX.value + dragAmount.x)
                                        }
                                    },
                                    onDragEnd = {
                                        scope.launch {
                                            if (offsetX.value.absoluteValue > 120f) {
                                                val swipedCard = topCard ?: return@launch

                                                offsetX.animateTo(
                                                    targetValue = if (offsetX.value > 0) 1000f else -1000f,
                                                    animationSpec = tween(
                                                        durationMillis = 300,
                                                        easing = FastOutSlowInEasing
                                                    )
                                                )

                                                // Mueve la carta arrastrada al final del stack
                                                cards = cards.filter { it != swipedCard } + swipedCard
                                                onSwipe(swipedCard)
                                            }
                                            offsetX.snapTo(0f)
                                        }
                                    }
                                )
                            } else Modifier
                    )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MountVaultCardStackSwiperPreview() {
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
        ), MountEntity(
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
        ), MountEntity(
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
        ), MountEntity(
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
        ), MountEntity(
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
        ), MountEntity(
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
        ), MountEntity(
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
        ), MountEntity(
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
        ), MountEntity(
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
        ), MountEntity(
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
        ), MountEntity(
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
    MountVaultCardStackSwiper(mounts = mounts, onSwipe = {})
}