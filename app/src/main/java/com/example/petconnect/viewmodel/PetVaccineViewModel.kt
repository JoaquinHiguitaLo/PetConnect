
package com.example.petconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petconnect.data.model.PetVaccine
import com.example.petconnect.data.repository.PetVaccineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado de la pantalla del carnet de vacunación.
 */
data class PetVaccineUiState(
    val vacunas: List<PetVaccine> = emptyList(),
    val mascotaId: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val operationSuccess: Boolean = false
)

/**
 * ViewModel encargado de gestionar las vacunas
 * registradas para cada mascota.
 */
class PetVaccineViewModel(
    private val repository: PetVaccineRepository =
        PetVaccineRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(PetVaccineUiState())

    val uiState: StateFlow<PetVaccineUiState> =
        _uiState.asStateFlow()

    // ==========================================
    // CARGAR VACUNAS DE UNA MASCOTA
    // ==========================================

    fun cargarVacunas(mascotaId: String) {

        if (mascotaId.isBlank()) {
            _uiState.update {
                it.copy(
                    vacunas = emptyList(),
                    mascotaId = null,
                    isLoading = false,
                    errorMessage = "La mascota no tiene identificador."
                )
            }
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    mascotaId = mascotaId,
                    vacunas = emptyList(),
                    isLoading = true,
                    errorMessage = null
                )
            }

            repository.obtenerVacunasPorMascota(mascotaId)
                .onSuccess { vacunas ->

                    if (_uiState.value.mascotaId == mascotaId) {
                        _uiState.update {
                            it.copy(
                                vacunas = vacunas,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                }
                .onFailure { error ->

                    if (_uiState.value.mascotaId == mascotaId) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = error.message
                                    ?: "No se pudieron cargar las vacunas."
                            )
                        }
                    }
                }
        }
    }

    // ==========================================
    // REGISTRAR VACUNA
    // ==========================================

    fun registrarVacuna(vacuna: PetVaccine) {

        if (_uiState.value.isSaving) return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSaving = true,
                    operationSuccess = false,
                    errorMessage = null
                )
            }

            repository.registrarVacuna(vacuna)
                .onSuccess {

                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            operationSuccess = true
                        )
                    }

                    cargarVacunas(vacuna.mascotaId)
                }
                .onFailure { error ->

                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            operationSuccess = false,
                            errorMessage = error.message
                                ?: "No se pudo registrar la vacuna."
                        )
                    }
                }
        }
    }

    // ==========================================
    // ACTUALIZAR VACUNA
    // ==========================================

    fun actualizarVacuna(vacuna: PetVaccine) {

        if (_uiState.value.isSaving) return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSaving = true,
                    operationSuccess = false,
                    errorMessage = null
                )
            }

            repository.actualizarVacuna(vacuna)
                .onSuccess {

                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            operationSuccess = true
                        )
                    }

                    cargarVacunas(vacuna.mascotaId)
                }
                .onFailure { error ->

                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            operationSuccess = false,
                            errorMessage = error.message
                                ?: "No se pudo actualizar la vacuna."
                        )
                    }
                }
        }
    }

    // ==========================================
    // ELIMINAR VACUNA
    // ==========================================

    fun eliminarVacuna(vacunaId: String) {

        if (_uiState.value.isSaving) return

        val mascotaIdActual = _uiState.value.mascotaId
            ?: return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSaving = true,
                    operationSuccess = false,
                    errorMessage = null
                )
            }

            repository.eliminarVacuna(vacunaId)
                .onSuccess {

                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            operationSuccess = true
                        )
                    }

                    cargarVacunas(mascotaIdActual)
                }
                .onFailure { error ->

                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            operationSuccess = false,
                            errorMessage = error.message
                                ?: "No se pudo eliminar la vacuna."
                        )
                    }
                }
        }
    }

    // ==========================================
    // LIMPIAR RESULTADOS
    // ==========================================

    fun limpiarResultadoOperacion() {
        _uiState.update {
            it.copy(
                operationSuccess = false,
                errorMessage = null
            )
        }
    }
}
