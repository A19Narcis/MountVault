package com.narcisdev.mountvault.feature.app.mounts

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
class MountsViewModel @Inject constructor(
    observeMountsUseCase: ObserveMountsUseCase,
    observeUserMountsUseCase: ObserveUserMountsUseCase,
    private val getExpansionsUseCase: GetExpansionsUseCase,
    private val userPreferencesDataSource: UserPreferencesDataSource
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

    private val _uiState = MutableStateFlow(MountsUiState())
    val uiState: StateFlow<MountsUiState> = _uiState

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

data class MountsUiState(
    val selectedExpansions: List<ExpansionEntity> = emptyList(),
    val selectedRarities: List<String> = emptyList(),
    val expansions: List<ExpansionEntity> = emptyList(),
    val isLoading: Boolean = true,
)