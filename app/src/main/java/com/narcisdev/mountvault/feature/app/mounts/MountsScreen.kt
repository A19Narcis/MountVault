package com.narcisdev.mountvault.feature.app.mounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.narcisdev.mountvault.core.components.MountScreenExpansionCard

@Composable
fun MountsScreen(
    padding: PaddingValues,
    viewModel: MountsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.padding(padding)) {
        LazyColumn() {
            items(uiState.expansions, key = { it.id }) { expansion ->
                MountScreenExpansionCard(expansion)
            }
        }
    }
}