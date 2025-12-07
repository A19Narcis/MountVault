package com.narcisdev.mountvault.feature.auth.register

import android.util.Log
import androidx.credentials.PasswordCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import com.narcisdev.mountvault.domain.entity.UserEntity
import com.narcisdev.mountvault.domain.usecase.LoginUseCase
import com.narcisdev.mountvault.domain.usecase.RegisterUseCase
import com.narcisdev.mountvault.feature.auth.login.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val userPrefs: UserPreferencesDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())

    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onEmailChange(value: String) {
        _uiState.update { state ->
            state.copy(email = value)
        }
    }

    fun onUsernameChange(value: String) {
        _uiState.update { state ->
            state.copy(username = value)
        }
    }

    fun onAgeChange(value: String) {
        _uiState.update { state ->
            state.copy(age = value.toInt())
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { state ->
            state.copy(password = value)
        }
    }

    fun onRegisterClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = UserEntity(
                username = _uiState.value.username,
                email = _uiState.value.email,
                age = _uiState.value.age
            )
            val responseRegister = registerUseCase.invoke(
                user,
                password = _uiState.value.password,
            )

            if (responseRegister != null) {
                // Guardar en DataStore
                userPrefs.saveUser(responseRegister)
                withContext(Dispatchers.Main) {
                    _uiState.update { it.copy(registerSuccess = true, error = null) }
                    Log.i(Constants.APP_NAME, "SUCCESS: $responseRegister")
                }
            } else {
                withContext(Dispatchers.Main) {
                    _uiState.update { it.copy(registerSuccess = false, error = Constants.REGISTER_FAILED_MSG) }
                    Log.i(Constants.APP_NAME, "ERROR: Register failed")
                }
            }
        }
    }

}

data class RegisterUiState(
    val age: Int = 40,
    val email: String = "narcis@gmail.com",
    val username: String = "Rulkkan",
    val password: String = "123qwerty",
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,
    val error: String? = null
)