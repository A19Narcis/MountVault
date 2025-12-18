package com.narcisdev.mountvault.core.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.narcisdev.mountvault.core.navigation.Routes
import com.narcisdev.mountvault.domain.entity.PackEntity
import com.narcisdev.mountvault.feature.app.navigationBar.MyNavigationBar
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@SuppressLint("FrequentlyChangingValue")
@Composable
fun MountVaultPackCarousel(
    packs: List<PackEntity>,
    onPackSelected: (PackEntity) -> Unit,
    onPackClick: (PackEntity) -> Unit
) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { packs.size }
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        onPackSelected(packs[pagerState.currentPage])
    }

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 50.dp),
        pageSpacing = 8.dp,
        modifier = Modifier.fillMaxWidth()

    ) { page ->

        val pageOffset =
            ((pagerState.currentPage - page) +
                    pagerState.currentPageOffsetFraction).absoluteValue

        val scale = lerp(
            start = 0.85f,
            stop = 1f,
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        )

        MountVaultPackItem(
            pack = packs[page],
            scale = scale,
            isSelected = page == pagerState.currentPage,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(page)
                }
                onPackClick(packs[page])
            }
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MountVaultPackCarouselPreview() {
    Scaffold(
        bottomBar = { MyNavigationBar(currentRoute = Routes.Main, backStack = mutableListOf()) }
    ) {
        MountVaultPackCarousel(Constants.getAllPacks(), onPackSelected = {}, onPackClick = {})
    }
}