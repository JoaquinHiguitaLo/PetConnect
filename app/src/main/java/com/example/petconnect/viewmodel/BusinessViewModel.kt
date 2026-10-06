package com.example.petconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petconnect.data.model.Business
import com.example.petconnect.data.repository.BusinessRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BusinessUiState(
    val businesses: List<Business> = emptyList(),
    val selectedBusiness: Business? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val operationSuccess: Boolean = false
)

class BusinessViewModel(
    private val businessRepository: BusinessRepository = BusinessRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BusinessUiState())

    val uiState: StateFlow<BusinessUiState> =
        _uiState.asStateFlow()


/**
 * Registra un nuevo negocio en Firestore.
 *
 * La operación se ejecuta dentro de viewModelScope para que
 * la corrutina esté asociada al ciclo de vida del ViewModel.
 */
fun crearNegocio(business: Business) {
    viewModelScope.launch {

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null,
            operationSuccess = false
        )

        val result = businessRepository.crearNegocio(business)

        result.onSuccess {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                operationSuccess = true,
                errorMessage = null
            )
        }.onFailure { error ->
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                operationSuccess = false,
                errorMessage = error.message
                    ?: "No se pudo registrar el negocio."
            )
        }
    }
}
    /**
     * Carga todos los negocios asociados a un prestador.
     *
     * Utiliza el ID del usuario autenticado para consultar
     * únicamente sus negocios.
     */
    fun cargarNegociosPorPrestador(prestadorId: String) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val result = businessRepository
                .obtenerNegociosPorPrestador(prestadorId)

            result.onSuccess { businesses ->
                _uiState.value = _uiState.value.copy(
                    businesses = businesses,
                    isLoading = false,
                    errorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message
                        ?: "No se pudieron cargar los negocios."
                )
            }
        }
    }
    /**
     * Carga un negocio específico utilizando su ID.
     *
     * Guarda el negocio obtenido dentro de selectedBusiness
     * para que la interfaz pueda mostrar su información.
     */
    fun cargarNegocioPorId(businessId: String) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                selectedBusiness = null
            )

            val result = businessRepository
                .obtenerNegocioPorId(businessId)

            result.onSuccess { business ->
                _uiState.value = _uiState.value.copy(
                    selectedBusiness = business,
                    isLoading = false,
                    errorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message
                        ?: "No se pudo cargar el negocio."
                )
            }
        }
    }

    /**
     * Actualiza la información de un negocio existente.
     *
     * Envía el negocio modificado al Repository para
     * actualizar el documento correspondiente en Firestore.
     */
    fun actualizarNegocio(business: Business) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                operationSuccess = false
            )

            val result = businessRepository
                .actualizarNegocio(business)

            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    operationSuccess = true,
                    errorMessage = null,
                    selectedBusiness = business
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    operationSuccess = false,
                    errorMessage = error.message
                        ?: "No se pudo actualizar el negocio."
                )
            }
        }
    }

    /**
     * Elimina un negocio existente.
     *
     * Después de eliminarlo correctamente, también se elimina
     * de la lista almacenada en el estado del ViewModel.
     */
    fun eliminarNegocio(business: Business) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                operationSuccess = false
            )

            val result = businessRepository
                .eliminarNegocio(business)

            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    businesses = _uiState.value.businesses.filter {
                        it.id != business.id
                    },
                    selectedBusiness = null,
                    isLoading = false,
                    operationSuccess = true,
                    errorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    operationSuccess = false,
                    errorMessage = error.message
                        ?: "No se pudo eliminar el negocio."
                )
            }
        }
    }

}