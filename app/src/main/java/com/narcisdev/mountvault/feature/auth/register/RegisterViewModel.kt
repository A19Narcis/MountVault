package com.narcisdev.mountvault.feature.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narcisdev.mountvault.core.components.Constants
import com.narcisdev.mountvault.data.local.UserPreferencesDataSource
import com.narcisdev.mountvault.domain.entity.UserEntity
import com.narcisdev.mountvault.domain.usecase.RegisterUseCase
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
            state.copy(email = value, emailError = null)
        }

    }

    private fun validateEmail() {
        val email = _uiState.value.email
        val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.length in 9..Integer.MAX_VALUE
        _uiState.update { state ->
            state.copy(emailError = if (isValid) null else "Invalid email")
        }
    }

    fun onUsernameChange(value: String) {
        _uiState.update { state ->
            state.copy(username = value, usernameError = null)
        }
    }

    private fun validateUsername() {
        val username = _uiState.value.username
        val isValid = username.length in 2..20
        _uiState.update { state ->
            state.copy(usernameError = if (isValid) null else "Invalid username")
        }
    }

    fun onAgeChange(value: String) {
        if (!value.contains(".") && !value.contains("-")) {
            _uiState.update { state ->
                state.copy(age = value, ageError = null)
            }
        }

    }

    private fun validateAge() {
        val age = _uiState.value.age
        val isValid = age.isNotEmpty() && age.toInt() in 1..120
        _uiState.update { state ->
            state.copy(ageError = if (isValid) null else "Invalid age")
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { state ->
            state.copy(password = value, passwordError = null)
        }
    }

    private fun validatePassword() {
        val password = _uiState.value.password
        val isValid = password.length in 6..20
        _uiState.update { state ->
            state.copy(passwordError = if (isValid) null else "Invalid password. Must be between 6 and 20 characters.")
        }
    }

    fun onRegisterClicked() {
        validateEmail()
        validateAge()
        validateUsername()
        validatePassword()
        if (_uiState.value.emailError != null || _uiState.value.ageError != null || _uiState.value.usernameError != null || _uiState.value.passwordError != null) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val user = UserEntity(
                username = _uiState.value.username,
                email = _uiState.value.email,
                age = _uiState.value.age.toInt()
            )
            val responseRegister = registerUseCase.invoke(
                user,
                password = _uiState.value.password,
            )

            if (responseRegister != null) {
                // Guardar en DataStore
                userPrefs.saveUser(responseRegister)
                withContext(Dispatchers.Main) {
                    _uiState.update { it.copy(registerSuccess = true, errorRegister = null, emailError = null, ageError = null, usernameError = null, passwordError = null) }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _uiState.update { it.copy(registerSuccess = false, errorRegister = Constants.REGISTER_FAILED_MSG) }
                    Log.i(Constants.APP_NAME, "ERROR: Register failed")
                }
            }
        }
    }

}

data class RegisterUiState(
    val age: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,
    val errorRegister: String? = null,
    val emailError: String? = null,
    val ageError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
)