package com.example.petconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petconnect.data.model.Service
import com.example.petconnect.data.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ServiceUiState(
    val services: List<Service> = emptyList(),
    val selectedService: Service? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val operationSuccess: Boolean = false
)

class ServiceViewModel(
    private val serviceRepository: ServiceRepository = ServiceRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServiceUiState())

    val uiState: StateFlow<ServiceUiState> =
        _uiState.asStateFlow()

    /**
     * Registra un nuevo servicio en Firestore.
     *
     * El servicio debe contener el businessId del negocio al que pertenece.*/

    fun crearServicio(service: Service) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                operationSuccess = false
            )

            val result = serviceRepository
                .crearServicio(service)

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
                        ?: "No se pudo registrar el servicio."
                )
            }
        }
    }

    /**
     * Carga todos los servicios asociados a un negocio.
     *
     * Utiliza el businessId para consultar únicamente
     * los servicios pertenecientes al negocio indicado.
     */
    fun cargarServiciosPorNegocio(businessId: String) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val result = serviceRepository
                .obtenerServiciosPorNegocio(businessId)

            result.onSuccess { services ->
                _uiState.value = _uiState.value.copy(
                    services = services,
                    isLoading = false,
                    errorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message
                        ?: "No se pudieron cargar los servicios."
                )
            }
        }
    }

    /**
     * Carga un servicio específico utilizando su ID.
     *
     * Guarda el servicio obtenido en selectedService
     * para que la interfaz pueda mostrar su información.
     */
    fun cargarServicioPorId(serviceId: String) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                selectedService = null
            )

            val result = serviceRepository
                .obtenerServicioPorId(serviceId)

            result.onSuccess { service ->
                _uiState.value = _uiState.value.copy(
                    selectedService = service,
                    isLoading = false,
                    errorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message
                        ?: "No se pudo cargar el servicio."
                )
            }
        }
    }

    /**
     * Actualiza la información de un servicio existente.
     *
     * Envía el servicio modificado al Repository para
     * actualizar el documento correspondiente en Firestore.
     */
    fun actualizarServicio(service: Service) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                operationSuccess = false
            )

            val result = serviceRepository
                .actualizarServicio(service)

            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    operationSuccess = true,
                    errorMessage = null,
                    selectedService = service
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    operationSuccess = false,
                    errorMessage = error.message
                        ?: "No se pudo actualizar el servicio."
                )
            }
        }
    }

    /**
     * Elimina un servicio existente.
     *
     * Después de eliminarlo correctamente, también lo elimina
     * de la lista actual almacenada en el estado.
     */
    fun eliminarServicio(service: Service) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                operationSuccess = false
            )

            val result = serviceRepository
                .eliminarServicio(service)

            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    services = _uiState.value.services.filter {
                        it.id != service.id
                    },
                    selectedService = null,
                    isLoading = false,
                    operationSuccess = true,
                    errorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    operationSuccess = false,
                    errorMessage = error.message
                        ?: "No se pudo eliminar el servicio."
                )
            }
        }
    }
}