package com.narcisdev.mountvault.feature.app.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import com.narcisdev.mountvault.domain.entity.AvatarEntity
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.usecase.GetAvatarsUseCase
import com.narcisdev.mountvault.domain.usecase.GetExpansionsUseCase
import com.narcisdev.mountvault.domain.usecase.LogoutUseCase
import com.narcisdev.mountvault.domain.usecase.ObserveMountsUseCase
import com.narcisdev.mountvault.domain.usecase.ObserveUserMountsUseCase
import com.narcisdev.mountvault.domain.usecase.UpdateUserUseCase
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getExpansionsUseCase: GetExpansionsUseCase,
    private val getAvatarsUseCase: GetAvatarsUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    observeMountsUseCase: ObserveMountsUseCase,
    observeUserMountsUseCase: ObserveUserMountsUseCase,
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

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            val currentAvatarUrl = userFlow.firstOrNull()?.userUrl.orEmpty()
            val expansions = getExpansionsUseCase()
            val avatars = getAvatarsUseCase()

            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(
                        selectedAvatarUrl = currentAvatarUrl,
                        avatars = avatars,
                        expansions = expansions,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateUser() {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                userPreferencesDataSource.editUserUrl(uiState.value.selectedAvatarUrl)
                updateUserUseCase.invoke(userPreferencesDataSource.getUserLocalOnce()!!)
            }
            _uiState.update { it.copy(showToastUpdate = true) }
        }
    }

    fun changeAvatarUrl(url: String) {
        _uiState.update { it.copy(selectedAvatarUrl = url) }
    }

    fun refreshProfile() {
        loadProfile()
    }

    fun onLogoutClicked(){
        viewModelScope.launch(Dispatchers.IO) {
            logoutUseCase.invoke()
            withContext(Dispatchers.Main) {
                _uiState.update { state ->
                    state.copy(isLoggedOut = true)
                }
            }
        }
    }

    fun resetLogoutFlag() {
        _uiState.update { it.copy(isLoggedOut = false) }
    }

    fun resetToastFlag() {
        _uiState.update { it.copy(showToastUpdate = false) }
    }
}

data class ProfileUiState(
    val avatars: List<AvatarEntity> = emptyList(),
    val selectedAvatarUrl: String = "",
    val expansions: List<ExpansionEntity> = emptyList(),
    val isLoading: Boolean = true,
    val showToastUpdate: Boolean = false,
    val isLoggedOut: Boolean = false
)