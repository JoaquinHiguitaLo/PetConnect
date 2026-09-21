package com.example.petconnect.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petconnect.data.model.User
import com.example.petconnect.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado de la pantalla de perfil.
 *
 * Contiene:
 * - Los datos actuales del usuario.
 * - Estado de carga.
 * - Mensaje de error.
 * - Resultado de una actualización.
 */
data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val operationSuccess: Boolean = false
)

/**
 * ViewModel encargado de gestionar la información del perfil.
 *
 * Responsabilidades:
 * - Obtener el usuario autenticado.
 * - Cargar sus datos desde Firestore.
 * - Actualizar sus datos.
 *
 * El ViewModel NO accede directamente a Firestore.
 * Para eso utiliza UserRepository.
 */
class ProfileViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    // Firebase Authentication nos permite conocer
    // qué usuario está actualmente autenticado.
    private val firebaseAuth = FirebaseAuth.getInstance()

    // Estado privado y mutable.
    private val _uiState = MutableStateFlow(ProfileUiState())

    // Estado público de solo lectura para Compose.
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    /**
     * Carga los datos del usuario actualmente autenticado.
     *
     * Flujo:
     *
     * FirebaseAuth
     *      ↓
     * currentUser.uid
     *      ↓
     * UserRepository
     *      ↓
     * Firestore usuarios/{uid}
     */
    fun cargarUsuario() {

        val firebaseUser = firebaseAuth.currentUser

        // Si no existe usuario autenticado,
        // no podemos consultar su perfil.
        if (firebaseUser == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "No hay un usuario autenticado."
                )
            }
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            // Obtenemos el UID de Firebase Authentication.
            val uid = firebaseUser.uid

            // Buscamos el documento usuarios/{uid}.
            val result = userRepository.obtenerUsuario(uid)

            result.onSuccess { user ->

                if (user != null) {

                    _uiState.update {
                        it.copy(
                            user = user,
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                } else {

                    _uiState.update {
                        it.copy(
                            user = null,
                            isLoading = false,
                            errorMessage = "No se encontró el perfil del usuario."
                        )
                    }
                }

            }.onFailure { error ->

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            error.message
                                ?: "No se pudo cargar la información del usuario."
                    )
                }
            }
        }
    }

    /**
     * Actualiza los datos del usuario en Firestore.
     */
    fun actualizarUsuario(user: User) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    operationSuccess = false
                )
            }

            val result = userRepository.actualizarUsuario(user)

            result.onSuccess {

                _uiState.update {
                    it.copy(
                        user = user,
                        isLoading = false,
                        operationSuccess = true,
                        errorMessage = null
                    )
                }

            }.onFailure { error ->

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        operationSuccess = false,
                        errorMessage =
                            error.message
                                ?: "No se pudo actualizar el perfil."
                    )
                }
            }
        }
    }

    /**
     * Limpia el mensaje de éxito después de que la UI
     * haya terminado de procesar la actualización.
     */
    fun limpiarEstadoOperacion() {

        _uiState.update {
            it.copy(
                operationSuccess = false
            )
        }
    }

    /**
     * Limpia cualquier mensaje de error mostrado.
     */
    fun limpiarError() {

        _uiState.update {
            it.copy(
                errorMessage = null
            )
        }
    }
}