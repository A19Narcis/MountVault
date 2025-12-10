package com.narcisdev.mountvault.feature.app.settings

import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.integrity.t
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import com.narcisdev.mountvault.domain.entity.AvatarEntity
import com.narcisdev.mountvault.domain.entity.UserEntity
import com.narcisdev.mountvault.domain.usecase.GetAvatarsUseCase
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
class SettingsViewModel @Inject constructor(
    private val settingsUseCase: SettingsUseCase,
    private val getAvatarsUseCase: GetAvatarsUseCase,
    private val userPrefs: UserPreferencesDataSource
): ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    fun onLogoutClicked(){
        viewModelScope.launch(Dispatchers.IO) {
            settingsUseCase.invoke()
            withContext(Dispatchers.Main) {
                _uiState.update { state ->
                    state.copy(isLoggedOut = true)
                }
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val avatars = getAvatarsUseCase.invoke()
            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(avatars = avatars)
                }
            }
        }
    }

    fun resetLogoutFlag() {
        _uiState.update { it.copy(isLoggedOut = false) }
    }

    fun onProfileBackClicked(backToProfile: () -> Unit) {
        _uiState.update { state ->
            state.copy(clickableEnabled = false)
        }

        backToProfile()

        viewModelScope.launch {
            delay(1000)
            _uiState.update { state ->
                state.copy(clickableEnabled = true)
            }
        }
    }

    suspend fun updateUser(user: UserEntity) {
        userPrefs.updateUser { currentUser ->
            currentUser.copy(
                username = user.username,
                age = user.age,
                userUrl = user.userUrl
            )
        }
    }

}

data class SettingsUiState(
    val username: String = "",
    val selectedAvatarUrl: String = "",
    val age: Int = 0,
    val isLoggedOut: Boolean = false,
    val clickableEnabled: Boolean = true,
    val avatars: List<AvatarEntity> = emptyList()
)