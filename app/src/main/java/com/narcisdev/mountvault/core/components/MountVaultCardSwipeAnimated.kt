package com.narcisdev.mountvault.core.components

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.narcisdev.mountvault.core.theme.WowFont
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.feature.app.selectedPack.SelectedPackViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.any
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun MountVaultCardSwipeAnimated(
    mounts: List<MountEntity>,
    selectedPackViewModel: SelectedPackViewModel,
    modifier: Modifier = Modifier,
    onSwipe: (MountEntity) -> Unit
) {
    var cards by remember { mutableStateOf(mounts) }
    val scope = rememberCoroutineScope()
    val swipeThreshold = 50.dp
    val userMounts by selectedPackViewModel.userMounts.collectAsStateWithLifecycle()

    if (cards.isEmpty()) return

    val topCard = cards.first()
    val behindCards = cards.drop(1)

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val behindOffsetY = remember { Animatable(0f) } // para animar las cartas detrás
    var isSwiping by remember { mutableStateOf(false) }


    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Cartas detrás
        behindCards.reversed().forEachIndexed { index, mount ->
            val behindTranslation = if (isSwiping) behindOffsetY.value else 0f
            Box(
                modifier = Modifier
                    .graphicsLayer(
                        translationY = behindTranslation,
                        scaleX = 1f,
                        scaleY = 1f,
                        alpha = 1f
                    )
            ) {
                MountVaultCard(
                    mount = mount,
                    obtained = true,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Carta superior draggable
        Box(
            modifier = Modifier
                .zIndex(if (isSwiping) 10f else 0f)
                .graphicsLayer(
                    translationX = offsetX.value,
                    translationY = offsetY.value,
                    rotationZ = rotation.value,
                    scaleX = 1f,
                    scaleY = 1f,
                    alpha = 1f,
                )
                .pointerInput(cards) {
                    detectDragGestures(
                        onDragEnd = {
                            scope.launch {
                                val shouldSwipe =
                                    abs(offsetX.value) > swipeThreshold.toPx() ||
                                            abs(offsetY.value) > swipeThreshold.toPx()

                                if (shouldSwipe) {
                                    isSwiping = true

                                    // Animación de salida de la carta superior
                                    val targetX = if (offsetX.value >= 0) 1000f else -1000f
                                    val targetY = offsetY.value + 300f
                                    launch { offsetX.animateTo(targetX, tween(300)) }
                                    launch { offsetY.animateTo(targetY, tween(300)) }
                                    launch { rotation.animateTo(offsetX.value / 20f, tween(300)) }


                                    // Esperar que termine la animación
                                    delay(300)

                                    // Reordenar lista: la carta superior va al final

                                    val movedCard = cards.first()
                                    onSwipe(movedCard)
                                    cards = cards.drop(1) + movedCard

                                    // Reset offsets
                                    offsetX.snapTo(0f)
                                    offsetY.snapTo(0f)
                                    rotation.snapTo(0f)
                                    behindOffsetY.snapTo(0f)
                                    isSwiping = false

                                } else {
                                    // Vuelve al centro si no supera threshold
                                    launch { offsetX.animateTo(0f, tween(300)) }
                                    launch { offsetY.animateTo(0f, tween(300)) }
                                    launch { rotation.animateTo(0f, tween(300)) }
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            scope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount.x)
                                offsetY.snapTo(offsetY.value + dragAmount.y)
                                rotation.snapTo(offsetX.value / 20f)
                            }
                        }
                    )
                }
        ) {
            // Ver si la carta que se muestra la tiene ya el usuario para ensñar el texto de NEW
            Log.i(Constants.APP_NAME, "MOUNT ACTUAL: ${topCard.name}")
            val repeatedCard = userMounts.any { it.id == topCard.id }
            if (repeatedCard) {
                selectedPackViewModel.setIsNewCard(false)
            } else {
                selectedPackViewModel.setIsNewCard(true)
            }
            Log.i(Constants.APP_NAME, "NEW MOUNT? ${selectedPackViewModel.getIsNewCard()}")
            MountVaultCard(
                mount = topCard,
                obtained = true,
                modifier = Modifier.fillMaxSize()
            )
        }
    }


}

//@Composable
//fun MountVaultCardSwipeAnimated(
//    mounts: List<MountEntity>,
//    modifier: Modifier = Modifier
//) {
//    var cards by remember { mutableStateOf(mounts) }
//    val scope = rememberCoroutineScope()
//    val swipeThreshold = 50.dp
//
//    if (cards.isEmpty()) return
//
//    val topCard = cards.first()
//    val behindCards = cards.drop(1)
//
//    val offsetX = remember { Animatable(0f) }
//    val offsetY = remember { Animatable(0f) }
//
//    Box(
//        modifier = modifier,
//        contentAlignment = Alignment.Center
//    ) {
//        // Cartas detrás
//        behindCards.reversed().forEach { mount ->
//            Box(
//                modifier = Modifier
//                    .graphicsLayer(
//                        scaleX = 1f,
//                        scaleY = 1f,
//                        alpha = 1f
//                    )
//            ) {
//                MountVaultCard(
//                    mount = mount,
//                    obtained = true,
//                    modifier = Modifier.fillMaxSize()
//                )
//            }
//        }
//
//        // Carta superior draggable
//        Box(
//            modifier = Modifier
//                .graphicsLayer(
//                    translationX = offsetX.value,
//                    translationY = offsetY.value,
//                    scaleX = 1f,
//                    scaleY = 1f,
//                    alpha = 1f
//                )
//                .pointerInput(cards) {
//                    detectDragGestures(
//                        onDragEnd = {
//                            scope.launch {
//                                if (abs(offsetX.value) > swipeThreshold.toPx() ||
//                                    abs(offsetY.value) > swipeThreshold.toPx()
//                                ) {
//                                    // Mover la carta superior al final
//                                    val movedCard = cards.first()
//                                    cards = cards.drop(1) + movedCard
//                                }
//                                offsetX.snapTo(0f)
//                                offsetY.snapTo(0f)
//                            }
//                        },
//                        onDrag = { change, dragAmount ->
//                            change.consume()
//                            scope.launch {
//                                offsetX.snapTo(offsetX.value + dragAmount.x)
//                                offsetY.snapTo(offsetY.value + dragAmount.y)
//                            }
//                        }
//                    )
//                }
//        ) {
//            MountVaultCard(
//                mount = topCard,
//                obtained = true,
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//    }
//}