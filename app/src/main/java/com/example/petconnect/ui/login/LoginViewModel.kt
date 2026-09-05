package com.example.petconnect.ui.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petconnect.data.AuthRepository
import com.example.petconnect.data.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false
)

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null, generalError = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, generalError = null) }
    }

    fun onLoginClick() {
        if (!validateFields()) return

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            val result = authRepository.login(
                _uiState.value.email.trim(),
                _uiState.value.password
            )
            when (result) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, generalError = result.message) }
                }
            }
        }
    }

    // Nota: aquí validamos MENOS cosas que en el registro.
    // No tiene sentido validar "la contraseña debe tener mínimo 6 caracteres"
    // en el login: si el usuario ya tiene una cuenta, su contraseña YA pasó esa regla
    // cuando se registró. Aquí solo nos importa que no vengan campos vacíos
    // y que el correo tenga formato válido.
    private fun validateFields(): Boolean {
        val state = _uiState.value
        var isValid = true

        val emailError = when {
            state.email.isBlank() -> "El correo electrónico es obligatorio."
            !Patterns.EMAIL_ADDRESS.matcher(state.email.trim()).matches() -> "Formato de correo inválido."
            else -> null
        }
        if (emailError != null) isValid = false

        val passwordError = when {
            state.password.isBlank() -> "La contraseña es obligatoria."
            else -> null
        }
        if (passwordError != null) isValid = false

        _uiState.update {
            it.copy(emailError = emailError, passwordError = passwordError)
        }
        return isValid
    }
}