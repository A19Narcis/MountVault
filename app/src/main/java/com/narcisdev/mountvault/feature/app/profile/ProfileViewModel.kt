package com.narcisdev.mountvault.feature.app.profile

import android.util.Log
import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.play.core.integrity.t
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import com.narcisdev.mountvault.domain.entity.ExpansionEntity
import com.narcisdev.mountvault.domain.entity.MountEntity
import com.narcisdev.mountvault.domain.usecase.GetExpansionsUseCase
import com.narcisdev.mountvault.domain.usecase.GetMountsUseCase
import com.narcisdev.mountvault.domain.usecase.GetUserMountsUseCase
import com.narcisdev.mountvault.domain.usecase.SettingsUseCase
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
            val responseGetExpansions = getExpansionsUseCase.invoke()
            Log.d(Constants.APP_NAME, "${userPreferencesDataSource.getUserLocalOnce()?.username ?: "NO_NAME"} ${responseGetUserMounts.size} User Mounts: $responseGetUserMounts")
            Log.d(Constants.APP_NAME, "------------------------------------------------------------------")
            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(
                        mounts = responseGetMounts,
                        userMounts = responseGetUserMounts,
                        expansions = responseGetExpansions,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun refreshProfile() {
        loadProfile()
    }
}

data class ProfileUiState(
    val userMounts: List<MountEntity> = emptyList(),
    val mounts: List<MountEntity> = emptyList(),
    val expansions: List<ExpansionEntity> = emptyList(),
    val isLoading: Boolean = true
)