package com.narcisdev.mountvault.feature.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narcisdev.mountvault.domain.usecase.LoginUseCase
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import com.narcisdev.mountvault.domain.usecase.SyncAvatarsUseCase
import com.narcisdev.mountvault.domain.usecase.SyncExpansionsUseCase
import com.narcisdev.mountvault.domain.usecase.SyncMountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val userPrefs: UserPreferencesDataSource,
    private val syncMountsUseCase: SyncMountsUseCase,
    private val syncExpansionsUseCase: SyncExpansionsUseCase,
    private val syncAvatarsUseCase: SyncAvatarsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onUserEntryChange(value: String) {
        _uiState.update { state ->
            state.copy(userEntry = value)
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { state ->
            state.copy(password = value)
        }
    }

    fun onLoginClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val responseLogin = loginUseCase.invoke(
                identifier = _uiState.value.userEntry, password = _uiState.value.password
            )
            if (responseLogin != null) {
                // Guardar en DataStore
                userPrefs.saveUser(responseLogin)
                withContext(Dispatchers.Main) {
                    _uiState.update { it.copy(loginSuccess = true, error = null) }
                    Log.i(Constants.APP_NAME, "SUCCESS: $responseLogin")
                }
            } else {
                withContext(Dispatchers.Main) {
                    _uiState.update { it.copy(loginSuccess = false, error = Constants.LOGIN_FAILED_MSG) }
                    Log.i(Constants.APP_NAME, "ERROR: Login failed")
                }
            }

        }
    }
    
    fun loadFromFirebaseIntoRoom(onCompletion: () -> Unit){
        viewModelScope.launch (Dispatchers.Main) {
            syncMountsUseCase.invoke()
            syncExpansionsUseCase.invoke()
            syncAvatarsUseCase.invoke()
            withContext(Dispatchers.Main) {
                onCompletion()
            }
        }
    }
}

data class LoginUiState(
    val userEntry: String = "Neisis",
    val password: String = "123456",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val error: String? = null
)