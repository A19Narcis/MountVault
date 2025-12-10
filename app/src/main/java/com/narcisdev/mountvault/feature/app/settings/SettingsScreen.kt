package com.narcisdev.mountvault.feature.app.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.narcisdev.mountvault.R
import com.narcisdev.mountvault.core.components.Constants.expansionColorMap
import com.narcisdev.mountvault.core.components.Constants.getExpansionColorsFromUrl
import com.narcisdev.mountvault.domain.entity.UserEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    userLocal: UserEntity?,
    onLogoutSuccess: () -> Unit,
    backToProfile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLogoutConfirm by remember { mutableStateOf(false) }
    var imagesSheetOpen by remember { mutableStateOf(false) }
    var selectedAvatarImage by remember { mutableStateOf(userLocal?.userUrl) }
    var originalUser by remember { mutableStateOf(userLocal!!) }
    var currentUser by remember { mutableStateOf(userLocal!!) }

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onLogoutSuccess()
            viewModel.resetLogoutFlag()
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 20.dp)
                    .size(30.dp)
                    .clickable(enabled = uiState.clickableEnabled) {
                        viewModel.onProfileBackClicked {
                            backToProfile()
                        }
                    },
                painter = painterResource(R.drawable.back), contentDescription = null
            )

            val borderImageColor: List<Color> = getExpansionColorsFromUrl(selectedAvatarImage, expansionColorMap)

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

                Image(
                    painter = painterResource(id = R.drawable.edit_icon), // tu PNG
                    contentDescription = "Edit image",
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 0.dp, y = 0.dp)
                        .background(Color.LightGray, shape = CircleShape)
                        .padding(4.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            TextField(
                value = userLocal?.username ?: "",
                onValueChange = { currentUser = currentUser.copy(username = it) },
                label = { Text("Username") },
            )
            Spacer(Modifier.height(16.dp))
            TextField(
                value = userLocal?.age.toString(),
                onValueChange = { currentUser = currentUser.copy(age = it.toInt()) },
                label = { Text("Age") },
            )
            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    if (currentUser == originalUser) {
                        viewModel.onProfileBackClicked {
                            backToProfile()
                        }
                    } else {

                    }
                }
            ) {
                Text("Save")
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    showLogoutConfirm = true
                }
            ) {
                Text("Logout")
            }

            Spacer(Modifier.height(16.dp))


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
                    })
            }

            if (imagesSheetOpen) {
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
                                            brush = Brush.horizontalGradient(borderImageColorSelectAvatar),
                                            width = 4.dp,
                                            shape = RoundedCornerShape(50)
                                        )
                                        .clip(RoundedCornerShape(50))
                                        .clickable {
                                            selectedAvatarImage = uiState.avatars[index].url
                                            imagesSheetOpen = false
                                            currentUser = currentUser.copy(userUrl = uiState.avatars[index].url)
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

        }
    }
}


@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(onLogoutSuccess = {}, backToProfile = {}, userLocal = null)
}

