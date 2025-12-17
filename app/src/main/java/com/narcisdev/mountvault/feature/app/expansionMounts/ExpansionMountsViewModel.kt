package com.narcisdev.mountvault.feature.app.expansionMounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.usecase.GetExpansionsUseCase
import com.narcisdev.mountvault.domain.usecase.ObserveMountsUseCase
import com.narcisdev.mountvault.domain.usecase.ObserveUserMountsUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExpansionMountsViewModel @Inject constructor(
    private val getExpansionsUseCase: GetExpansionsUseCase,
    observeMountsUseCase: ObserveMountsUseCase,
    observeUserMountsUseCase: ObserveUserMountsUseCase,
    userPreferencesDataSource: UserPreferencesDataSource
) : ViewModel() {

    private val userFlow = userPreferencesDataSource.userFlow

    val mounts: StateFlow<List<MountEntity>> =
        observeMountsUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val userMounts: StateFlow<List<MountEntity>> = userFlow
        .filterNotNull()
        .flatMapLatest { user ->
            observeUserMountsUseCase(user.ownedCards)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

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
    val expansions: List<ExpansionEntity> = emptyList(),
    val isLoading: Boolean = true,
)