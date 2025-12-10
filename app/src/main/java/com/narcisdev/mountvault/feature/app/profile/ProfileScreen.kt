package com.narcisdev.mountvault.feature.app.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.core.components.Constants.Expansion
import com.narcisdev.mountvault.core.components.Constants.expansionColorMap
import com.narcisdev.mountvault.core.components.Constants.getExpansionColorsFromUrl
import com.narcisdev.mountvault.core.components.ExpansionCard
import com.narcisdev.mountvault.core.theme.ExpansionColors
import com.narcisdev.mountvault.domain.entity.UserEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    padding: PaddingValues,
    userLocal: UserEntity?,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var imagesSheetOpen by remember { mutableStateOf(false) }
    var selectedAvatarImage by remember { mutableStateOf(userLocal?.userUrl) }
    var originalUser by remember { mutableStateOf(userLocal!!) }
    var currentUser by remember { mutableStateOf(userLocal!!) }
    var showEditUi by remember { mutableStateOf(false) }

    if (uiState.isLoading && !isRefreshing) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        state = rememberPullToRefreshState(),
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                viewModel.refreshProfile()
                delay(1000)
                isRefreshing = false
            }
        }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Image(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 20.dp)
                    .clickable { showEditUi = true }
                    .size(30.dp),
                painter = painterResource(R.drawable.edit_icon),
                contentDescription = null
            )

            val borderImageColor: List<Color> = getExpansionColorsFromUrl(userLocal?.userUrl, expansionColorMap)

            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clickable (
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { imagesSheetOpen = true },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(110.dp)
                        .border(
                            brush = Brush.horizontalGradient(borderImageColor),
                            width = 4.dp,
                            shape = RoundedCornerShape(50)
                        )
                        .clip(RoundedCornerShape(50)),
                    model = selectedAvatarImage,
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop
                )
                if (showEditUi) {
                    Image(
                        painter = painterResource(id = R.drawable.edit_icon),
                        contentDescription = "Edit image",
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.BottomEnd)
                            .background(Color.LightGray, shape = CircleShape)
                            .padding(4.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = userLocal?.username ?: "Username", fontSize = 20.sp
            )
            Spacer(Modifier.height(30.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.expansions, key = { it.id }) { expansion ->
                    val expansionMounts = remember(expansion.id, uiState.mounts) {
                        uiState.mounts.filter { it.expansionId == expansion.id }
                    }
                    val userMountsForExpansion = remember(expansion.id, uiState.userMounts) {
                        uiState.userMounts.filter { it.expansionId == expansion.id }
                    }

                    ExpansionCard(
                        expansion = expansion,
                        expansionMounts = expansionMounts,
                        userMountsForExpansion = userMountsForExpansion
                    )
                }
            }
        }
    }
}


