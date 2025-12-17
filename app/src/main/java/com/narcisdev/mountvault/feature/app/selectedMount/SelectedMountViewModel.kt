package com.narcisdev.mountvault.feature.app.selectedMount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.usecase.GetExpansionsUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectedMountViewModel @Inject constructor(
    private val getExpansionsUseCase: GetExpansionsUseCase,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExpansionMountsUiState())
    val uiState: StateFlow<ExpansionMountsUiState> = _uiState

    init {
        loadMountsData()
    }

    private fun loadMountsData() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {

            val responseGetExpansions = getExpansionsUseCase.invoke()
            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(

                        expansions = responseGetExpansions,
                        isLoading = false,
                    )
                }
            }
        }
    }


}

data class ExpansionMountsUiState(
    val selectedExpansions: List<ExpansionEntity> = emptyList(),
    val selectedRarities: List<String> = emptyList(),
    val userMounts: List<MountEntity> = emptyList(),
    val mounts: List<MountEntity> = emptyList(),
    val expansions: List<ExpansionEntity> = emptyList(),
    val isLoading: Boolean = true,
)