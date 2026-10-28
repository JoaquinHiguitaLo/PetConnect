
package com.example.petconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petconnect.data.model.UserAddress
import com.example.petconnect.data.repository.AddressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/**
 * Estado de la pantalla de direcciones.
 */
data class AddressUiState(
    val direcciones: List<UserAddress> = emptyList(),

    // Identifica la dirección seleccionada por el usuario.
    val direccionActivaId: String? = null,

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val operationSuccess: Boolean = false
)

class AddressViewModel(
    private val repository: AddressRepository = AddressRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressUiState())

    val uiState: StateFlow<AddressUiState> =
        _uiState.asStateFlow()

    /**
     * Consulta las direcciones del usuario autenticado.
     */
    fun cargarDirecciones() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val resultado = repository.obtenerMisDirecciones()

            resultado.onSuccess { direcciones ->

                _uiState.value = _uiState.value.copy(
                    direcciones = direcciones,
                    isLoading = false,
                    errorMessage = null
                )

            }.onFailure { error ->

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message
                        ?: "No se pudieron cargar las direcciones."
                )
            }
        }
    }

    /**
     * Consulta cuál dirección tiene seleccionada
     * el usuario autenticado.
     */
    fun cargarDireccionActiva() {

        viewModelScope.launch {

            val resultado = repository.obtenerDireccionActivaId()

            resultado.onSuccess { direccionId ->

                _uiState.value = _uiState.value.copy(
                    direccionActivaId = direccionId,
                    errorMessage = null
                )

            }.onFailure { error ->

                _uiState.value = _uiState.value.copy(
                    errorMessage = error.message
                        ?: "No se pudo consultar la dirección activa."
                )
            }
        }
    }

    /**
     * Selecciona una dirección guardada
     * como ubicación activa.
     */
    fun seleccionarDireccion(direccionId: String) {

        if (_uiState.value.isSaving) return

        _uiState.value = _uiState.value.copy(
            isSaving = true,
            errorMessage = null,
            operationSuccess = false
        )

        viewModelScope.launch {

            val resultado = repository
                .seleccionarDireccionActiva(direccionId)

            resultado.onSuccess {

                _uiState.value = _uiState.value.copy(
                    direccionActivaId = direccionId,
                    isSaving = false,
                    operationSuccess = true,
                    errorMessage = null
                )

            }.onFailure { error ->

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    operationSuccess = false,
                    errorMessage = error.message
                        ?: "No se pudo seleccionar la dirección."
                )
            }
        }
    }

    /**
     * Registra una nueva dirección en Firestore.
     */
    fun crearDireccion(direccion: UserAddress) {

        if (_uiState.value.isSaving) return

        _uiState.value = _uiState.value.copy(
            isSaving = true,
            errorMessage = null,
            operationSuccess = false
        )

        viewModelScope.launch {

            val resultado = repository.crearDireccion(direccion)

            resultado.onSuccess {

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    operationSuccess = true,
                    errorMessage = null
                )

                // Actualizamos el listado al terminar.
                cargarDirecciones()

            }.onFailure { error ->

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    operationSuccess = false,
                    errorMessage = error.message
                        ?: "No se pudo guardar la dirección."
                )
            }
        }
    }

    /**
     * Elimina una dirección guardada.
     */
    fun eliminarDireccion(direccionId: String) {

        if (_uiState.value.isSaving) return

        _uiState.value = _uiState.value.copy(
            isSaving = true,
            errorMessage = null,
            operationSuccess = false
        )

        viewModelScope.launch {

            val resultado = repository.eliminarDireccion(direccionId)

            resultado.onSuccess {

                _uiState.value = _uiState.value.copy(
                    direcciones = _uiState.value.direcciones.filter {
                        it.id != direccionId
                    },
                    isSaving = false,
                    operationSuccess = true,
                    errorMessage = null
                )

            }.onFailure { error ->

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    operationSuccess = false,
                    errorMessage = error.message
                        ?: "No se pudo eliminar la dirección."
                )
            }
        }
    }

    /**
     * Limpia los mensajes de operaciones anteriores.
     */
    fun limpiarResultadoOperacion() {

        _uiState.value = _uiState.value.copy(
            operationSuccess = false,
            errorMessage = null
        )
    }
}
