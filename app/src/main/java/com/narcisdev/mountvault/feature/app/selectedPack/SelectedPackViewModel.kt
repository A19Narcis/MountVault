package com.narcisdev.mountvault.feature.app.selectedPack

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.usecase.GetExpansionsUseCase
import com.narcisdev.mountvault.domain.usecase.ObserveMountsUseCase
import com.narcisdev.mountvault.domain.usecase.ObserveUserMountsUseCase
import com.narcisdev.mountvault.domain.usecase.UpdateUserMountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectedPackViewModel @Inject constructor(
    observeMountsUseCase: ObserveMountsUseCase,
    observeUserMountsUseCase: ObserveUserMountsUseCase,
    private val userPrefs: UserPreferencesDataSource,
    private val updateUserMountsUseCase: UpdateUserMountsUseCase,
    private val getExpansionsUseCase: GetExpansionsUseCase,
    userPreferencesDataSource: UserPreferencesDataSource
): ViewModel() {

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

    private val _uiState = MutableStateFlow(MainUiState())

    val uiState: StateFlow<MainUiState> = _uiState

    fun onCardSeen(mount: MountEntity) {
        // analytics
        // sonido
        // guardar estado temporal
    }

    suspend fun updateUserCards(mounts: List<MountEntity>) {
        val ownedCards = userPrefs.userFlow.firstOrNull()?.ownedCards
        val openedCards = mounts.map { it.id }
        val newMounts = openedCards.filter { it !in ownedCards!! }

        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                userPrefs.updateNewMounts(newMounts)
                updateUserMountsUseCase.invoke(userPrefs.getUserLocalOnce()!!)
            }
        }

    }

    fun getIsNewCard(): Boolean {
        return _uiState.value.isNewCard
    }

    fun setIsNewCard(value: Boolean) {
        _uiState.value = _uiState.value.copy(isNewCard = value)
    }

}

data class MainUiState(
    val expansions: List<ExpansionEntity> = emptyList(),
    var isNewCard: Boolean = false
)