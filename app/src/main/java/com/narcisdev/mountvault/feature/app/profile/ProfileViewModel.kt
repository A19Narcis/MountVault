package com.narcisdev.mountvault.feature.app.profile

import android.util.Log
import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.play.core.integrity.t
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import com.narcisdev.mountvault.domain.entity.AvatarEntity
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.usecase.GetAvatarsUseCase
import com.narcisdev.mountvault.domain.usecase.GetExpansionsUseCase
import com.narcisdev.mountvault.domain.usecase.GetMountsUseCase
import com.narcisdev.mountvault.domain.usecase.GetUserMountsUseCase
import com.narcisdev.mountvault.domain.usecase.LogoutUseCase
import com.narcisdev.mountvault.domain.usecase.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMountsUseCase: GetMountsUseCase,
    private val getUserMountsUseCase: GetUserMountsUseCase,
    private val getExpansionsUseCase: GetExpansionsUseCase,
    private val getAvatarsUseCase: GetAvatarsUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            val responseGetMounts = getMountsUseCase.invoke()
            val responseGetUserMounts = getUserMountsUseCase.invoke(
                userPreferencesDataSource.getUserLocalOnce()?.ownedCards ?: emptyList()
            )
            val currentAvatarUrl = userPreferencesDataSource.getUserLocalOnce()?.userUrl ?: ""
            val responseGetExpansions = getExpansionsUseCase.invoke()
            val avatars = getAvatarsUseCase.invoke()
            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(
                        selectedAvatarUrl = currentAvatarUrl,
                        avatars = avatars,
                        mounts = responseGetMounts,
                        userMounts = responseGetUserMounts,
                        expansions = responseGetExpansions,
                        isLoading = false,
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
    val userMounts: List<MountEntity> = emptyList(),
    val mounts: List<MountEntity> = emptyList(),
    val expansions: List<ExpansionEntity> = emptyList(),
    val isLoading: Boolean = true,
    val showToastUpdate: Boolean = false,
    val isLoggedOut: Boolean = false
)