package com.narcisdev.mountvault.feature.app.profile

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.components.Constants.expansionColorMap
import com.narcisdev.mountvault.core.components.Constants.getExpansionColorsFromUrl
import com.narcisdev.mountvault.core.components.ExpansionCard
import com.narcisdev.mountvault.core.components.ShakingImage
import com.narcisdev.mountvault.core.components.SnackBarMountVault
import com.narcisdev.mountvault.domain.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    padding: PaddingValues,
    userLocal: UserEntity?,
    onEditModeChange: (Boolean) -> Unit,
    onLogoutSuccess: () -> Unit,
) {
    val mounts by viewModel.mounts.collectAsStateWithLifecycle()
    val userMounts by viewModel.userMounts.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var imagesSheetOpen by remember { mutableStateOf(false) }

    var selectedAvatarImage by remember { mutableStateOf(userLocal?.userUrl) }
    var selectedAvatarBorders by remember { mutableStateOf(getExpansionColorsFromUrl(selectedAvatarImage, expansionColorMap)) }

    var originalUser by remember { mutableStateOf(userLocal!!) }
    var currentUser by remember { mutableStateOf(userLocal!!) }

    var showEditUi by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    var showSnackBar by remember { mutableStateOf(false) }

    var showDetailedImage by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onLogoutSuccess()
            viewModel.resetLogoutFlag()
        }
    }

    LaunchedEffect(uiState.showToastUpdate) {
        if (uiState.showToastUpdate) {
            showSnackBar = true
            viewModel.resetToastFlag()
        }
    }

    LaunchedEffect(showEditUi) {
        onEditModeChange(showEditUi)
    }

    BackHandler(enabled = showEditUi) {
        showExitDialog = true
    }

    if (uiState.isLoading && !isRefreshing) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro que quieres cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutConfirm = false
                    viewModel.onLogoutClicked()
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) {
                    Text("No")
                }
            }
        )
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Salir del modo edición") },
            text = { Text("Vas a salir del modo edición") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    showEditUi = false
                    viewModel.changeAvatarUrl(userLocal!!.userUrl)
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        showEditUi = true
                    }
                ) {
                    Text("Volver")
                }
            }
        )
    }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        state = rememberPullToRefreshState(),
        isRefreshing = isRefreshing,
        onRefresh = {
            if (!showEditUi){
                scope.launch(Dispatchers.Main) {
                    isRefreshing = true
                    viewModel.refreshProfile()
                    delay(1000)
                    isRefreshing = false
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (showEditUi) {
                    ShakingImage(
                        painter = painterResource(R.drawable.check_icon),
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .size(35.dp),
                        onClick = {
                            if (originalUser.userUrl != currentUser.userUrl) {
                                viewModel.changeAvatarUrl(uiState.selectedAvatarUrl)
                                viewModel.updateUser()
                            }
                            showEditUi = !showEditUi
                        }
                    )
                    Spacer(Modifier.weight(1f))
                    ShakingImage(
                        painter = painterResource(R.drawable.cross_icon),
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .size(35.dp),
                        onClick = {
                            viewModel.changeAvatarUrl(userLocal!!.userUrl)
                            showEditUi = !showEditUi
                        }
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .clickable {
                                showLogoutConfirm = true
                            }
                            .size(35.dp),
                        painter = painterResource(R.drawable.logout_icon),
                        contentDescription = null
                    )
                    Spacer(Modifier.weight(1f))
                    Image(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .clickable {
                                showEditUi = !showEditUi
                            }
                            .size(35.dp),
                        painter = painterResource(R.drawable.edit_icon),
                        contentDescription = null
                    )
                }
            }

            selectedAvatarBorders = getExpansionColorsFromUrl(uiState.selectedAvatarUrl, expansionColorMap)

            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { imagesSheetOpen = true },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(110.dp)
                        .border(
                            brush = Brush.horizontalGradient(selectedAvatarBorders),
                            width = 4.dp,
                            shape = RoundedCornerShape(50)
                        )
                        .clip(RoundedCornerShape(50))
                        .clickable { showDetailedImage = true },
                    model = uiState.selectedAvatarUrl,
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop
                )
                if (showDetailedImage) {
                    Dialog(onDismissRequest = { showDetailedImage = false }) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable (
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { showDetailedImage = false },
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = uiState.selectedAvatarUrl,
                                contentDescription = "Profile picture zoom",
                                contentScale = ContentScale.Crop, // mantiene la proporción y llena el espacio
                                modifier = Modifier
                                    .size(280.dp)
                                    .border(
                                        brush = Brush.horizontalGradient(selectedAvatarBorders),
                                        width = 8.dp,
                                        shape = RoundedCornerShape(50)
                                    )
                                    .clip(RoundedCornerShape(50))
                            )
                        }
                    }
                }
                if (showEditUi) {
                    Image(
                        painter = painterResource(id = R.drawable.edit_icon),
                        contentDescription = "Edit image",
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.BottomEnd)
                            .background(Color.LightGray, shape = CircleShape)
                            .padding(4.dp)
                    )
                }
            }

            if (imagesSheetOpen && showEditUi) {
                ModalBottomSheet(
                    onDismissRequest = { imagesSheetOpen = false }
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.avatars.size) { index ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50)),
                                contentAlignment = Alignment.Center
                            ) {
                                val borderImageColorSelectAvatar: List<Color> = getExpansionColorsFromUrl(uiState.avatars[index].url, expansionColorMap)
                                AsyncImage(
                                    modifier = Modifier
                                        .size(110.dp)
                                        .border(
                                            brush = Brush.horizontalGradient(
                                                borderImageColorSelectAvatar
                                            ),
                                            width = 4.dp,
                                            shape = RoundedCornerShape(50)
                                        )
                                        .clip(RoundedCornerShape(50))
                                        .clickable {
                                            viewModel.changeAvatarUrl(uiState.avatars[index].url)
                                            imagesSheetOpen = false
                                            currentUser =
                                                currentUser.copy(userUrl = uiState.avatars[index].url)
                                        },
                                    model = uiState.avatars[index].url,
                                    contentDescription = "Profile picture",
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
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
                    val expansionMounts = remember(expansion.id, mounts) {
                        mounts.filter { it.expansionId == expansion.id }
                    }
                    val userMountsForExpansion = remember(expansion.id, userMounts) {
                        userMounts.filter { it.expansionId == expansion.id }
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
    if (showSnackBar) {
        SnackBarMountVault(
            visible = showSnackBar,
            time = 1500,
            text = "Cambios guardados",
            onDismiss = { showSnackBar = !showSnackBar }
        )
    }
}




