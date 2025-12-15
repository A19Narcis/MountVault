package com.narcisdev.mountvault.feature.app.main

import androidx.lifecycle.ViewModel
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.usecase.GetExpansionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getExpansionsUseCase: GetExpansionsUseCase,
    private val userPreferencesDataSource: UserPreferencesDataSource
): ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())

    val uiState: StateFlow<MainUiState> = _uiState


}

data class MainUiState(
    val mounts: List<MountEntity> = emptyList(),
    val expansions: List<ExpansionEntity> = emptyList()
)