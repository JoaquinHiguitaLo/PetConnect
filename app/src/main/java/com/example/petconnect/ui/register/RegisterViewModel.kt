package com.example.petconnect.ui.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petconnect.data.repository.AuthRepository
import com.example.petconnect.data.repository.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Este es el "estado" completo de la pantalla de registro en un momento dado.
data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false,
    val registrationSuccess: Boolean = false
)

class RegisterViewModel(
    // Le "inyectamos" el repository. Por ahora lo creamos aquí directamente;
    // más adelante,  inyección de dependencias (Hilt), esto se hace de otra forma.
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    // _uiState es privado y "mutable" (se puede cambiar) — solo el ViewModel puede tocarlo.
    private val _uiState = MutableStateFlow(RegisterUiState())

    // uiState es público pero "de solo lectura" — la pantalla (Compose) solo puede leerlo,
    // nunca cambiarlo directamente. Esto evita bugs donde la UI "hace trampa" y cambia
    // datos que no le corresponden.
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Estas 3 funciones se llaman cada vez que el usuario escribe en un campo.
    // Actualizamos el texto Y limpiamos el error de ese campo (para que no se quede
    // pegado un error viejo mientras el usuario está corrigiendo).
    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null, generalError = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, generalError = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null, generalError = null) }
    }

    // Esto se llama cuando el usuario hace clic en "Registrarse"
    fun onRegisterClick() {
        // Primero validamos localmente. Si algo está mal, ni siquiera llamamos a Firebase.
        if (!validateFields()) return

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        // viewModelScope.launch nos permite ejecutar código "suspend" (que espera cosas)
        // de forma segura, ligado al ciclo de vida del ViewModel.
        viewModelScope.launch {
            val result = authRepository.register(
                _uiState.value.email.trim(),   // .trim() quita espacios accidentales al inicio/final
                _uiState.value.password
            )
            when (result) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, registrationSuccess = true) }
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, generalError = result.message) }
                }
            }
        }
    }

    // Aquí viven TODAS las validaciones de los criterios de aceptación:
    // - campos vacíos
    // - formato de correo
    // - contraseñas coinciden
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
            state.password.length < 6 -> "La contraseña debe tener al menos 6 caracteres."
            else -> null
        }
        if (passwordError != null) isValid = false

        val confirmPasswordError = when {
            state.confirmPassword.isBlank() -> "Debes confirmar la contraseña."
            state.confirmPassword != state.password -> "Las contraseñas no coinciden."
            else -> null
        }
        if (confirmPasswordError != null) isValid = false

        // Actualizamos el estado con todos los errores encontrados (o null si no hay)
        _uiState.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
        }
        return isValid
    }
}