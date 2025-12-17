package com.narcisdev.mountvault.feature.app.mounts

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.core.components.MountScreenExpansionCard
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity

@Composable
fun MountsScreen(
    padding: PaddingValues,
    viewModel: MountsViewModel,
    onExpansionClicked: (List<MountEntity>, ExpansionEntity) -> Unit
) {
    val mounts by viewModel.mounts.collectAsStateWithLifecycle()
    val userMounts by viewModel.userMounts.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.padding(padding)) {
        LazyColumn() {
            items(uiState.expansions, key = { it.id }) { expansion ->
                MountScreenExpansionCard(
                    expansion = expansion,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        Log.i(Constants.APP_NAME, "Expansion clicked: $expansion\nMounts $mounts")
                        onExpansionClicked(mounts, expansion)
                    }
                )
            }
        }
    }
}