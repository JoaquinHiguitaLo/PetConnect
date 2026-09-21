package com.example.petconnect.ui.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petconnect.data.model.User
import com.example.petconnect.data.repository.AuthRepository
import com.example.petconnect.data.repository.AuthResult
import com.example.petconnect.data.repository.UserRepository
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
    // Repository encargado de Firebase Authentication.
    private val authRepository: AuthRepository = AuthRepository(),

    // Repository encargado de los datos adicionales del usuario en Firestore.
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    // _uiState es privado y mutable.
    // Solo el ViewModel puede modificarlo.
    private val _uiState = MutableStateFlow(RegisterUiState())

    // La UI solamente puede observar el estado.
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Se ejecuta cada vez que cambia el correo.
    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(
                email = value,
                emailError = null,
                generalError = null
            )
        }
    }

    // Se ejecuta cada vez que cambia la contraseña.
    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = null,
                generalError = null
            )
        }
    }

    // Se ejecuta cada vez que cambia la confirmación de contraseña.
    fun onConfirmPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                confirmPassword = value,
                confirmPasswordError = null,
                generalError = null
            )
        }
    }

    // Se ejecuta cuando el usuario pulsa "Crear cuenta".
    fun onRegisterClick() {

        // Primero validamos los campos localmente.
        // Si existe algún error, no hacemos ninguna petición a Firebase.
        if (!validateFields()) return

        _uiState.update {
            it.copy(
                isLoading = true,
                generalError = null,
                registrationSuccess = false
            )
        }

        viewModelScope.launch {

            // ---------------------------------------------------------
            // PASO 1: CREAR CUENTA EN FIREBASE AUTHENTICATION
            // ---------------------------------------------------------

            val result = authRepository.register(
                _uiState.value.email.trim(),
                _uiState.value.password
            )

            when (result) {

                // Firebase Authentication creó correctamente la cuenta.
                is AuthResult.Success -> {

                    val firebaseUser = result.user

                    // ---------------------------------------------------------
                    // PASO 2: CREAR PERFIL DEL USUARIO EN FIRESTORE
                    // ---------------------------------------------------------

                    val user = User(
                        id = firebaseUser.uid,
                        nombreCompleto = "",
                        correo = firebaseUser.email ?: "",
                        telefono = "",
                        foto = null
                    )

                    // Guardamos el perfil utilizando el mismo UID
                    // generado por Firebase Authentication.
                    val userResult = userRepository.crearUsuario(user)

                    when {

                        // Las dos operaciones fueron exitosas.
                        userResult.isSuccess -> {

                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    registrationSuccess = true,
                                    generalError = null
                                )
                            }
                        }

                        // Authentication funcionó, pero Firestore falló.
                        else -> {

                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    registrationSuccess = false,
                                    generalError =
                                        userResult.exceptionOrNull()?.message
                                            ?: "La cuenta fue creada, pero no se pudo guardar el perfil."
                                )
                            }
                        }
                    }
                }

                // Firebase Authentication devolvió un error.
                is AuthResult.Error -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            registrationSuccess = false,
                            generalError = result.message
                        )
                    }
                }
            }
        }
    }

    // Validación local de los campos del formulario.
    private fun validateFields(): Boolean {

        val state = _uiState.value
        var isValid = true

        val emailError = when {
            state.email.isBlank() ->
                "El correo electrónico es obligatorio."

            !Patterns.EMAIL_ADDRESS
                .matcher(state.email.trim())
                .matches() ->
                "Formato de correo inválido."

            else -> null
        }

        if (emailError != null) {
            isValid = false
        }

        val passwordError = when {
            state.password.isBlank() ->
                "La contraseña es obligatoria."

            state.password.length < 6 ->
                "La contraseña debe tener al menos 6 caracteres."

            else -> null
        }

        if (passwordError != null) {
            isValid = false
        }

        val confirmPasswordError = when {
            state.confirmPassword.isBlank() ->
                "Debes confirmar la contraseña."

            state.confirmPassword != state.password ->
                "Las contraseñas no coinciden."

            else -> null
        }

        if (confirmPasswordError != null) {
            isValid = false
        }

        // Actualizamos todos los errores encontrados.
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