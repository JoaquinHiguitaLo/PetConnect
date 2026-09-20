package com.example.petconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petconnect.data.model.Pet
import com.example.petconnect.data.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


// ============================================================
// ESTADO DE LA PANTALLA DE MASCOTAS
// ============================================================
// Representa toda la información que la interfaz necesita
// conocer sobre el estado actual de las mascotas.
// ============================================================

data class PetsUiState(
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val operationSuccess: Boolean = false
)


// ============================================================
// VIEWMODEL DE MASCOTAS
// ============================================================
// Se encarga de:
// - Administrar el estado de la pantalla.
// - Ejecutar las operaciones relacionadas con mascotas.
// - Comunicarse con PetRepository.
// ============================================================

class PetsViewModel(
    private val petRepository: PetRepository = PetRepository()
) : ViewModel() {

    // Estado interno que solamente el ViewModel puede modificar.
    private val _uiState = MutableStateFlow(PetsUiState())

    // Estado público que la interfaz puede observar,
    // pero no modificar directamente.
    val uiState: StateFlow<PetsUiState> = _uiState.asStateFlow()


    // ========================================================
    // READ - CARGAR MASCOTAS
    // ========================================================
    // Obtiene las mascotas pertenecientes al usuario actual.
    // ========================================================
    fun cargarMascotas(usuarioId: String) {
        //Una ventaja importante es que cuando el ViewModel deja de existir, las coroutines asociadas a viewModelScope se cancelan.
        viewModelScope.launch {

            // Indicamos que comenzó una operación.
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            // Pedimos al Repository las mascotas del usuario.
            val result = petRepository.obtenerMascotas(usuarioId)

            result
                .onSuccess { pets ->

                    // Firebase respondió correctamente.
                    _uiState.value = _uiState.value.copy(
                        pets = pets,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                .onFailure { error ->

                    // Firebase devolvió un error.
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message
                            ?: "No se pudieron cargar las mascotas"
                    )
                }
        }
    }


// ========================================================
// ADD - AGREGAR MASCOTAS
// ========================================================
// Obtiene las mascotas pertenecientes al usuario actual.
// ========================================================

fun agregarMascota(pet: Pet) {

    viewModelScope.launch {

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null,
            operationSuccess = false
        )

        val result = petRepository.crearMascota(pet)

        result
            .onSuccess {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    operationSuccess = true,
                    errorMessage = null
                )
            }
            .onFailure { error ->

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    operationSuccess = false,
                    errorMessage = error.message
                        ?: "No se pudo registrar la mascota"
                )
            }
    }
}

    // ========================================================
// UPDATE - ACTUALIZAR MASCOTA
// ========================================================
// Recibe una mascota que ya existe y solicita al Repository
// actualizar el documento correspondiente en Firestore.
//
// Es importante que el objeto conserve su ID original,
// porque ese ID permite identificar qué documento debemos
// modificar.
// ========================================================
    fun actualizarMascota(pet: Pet) {

        viewModelScope.launch {

            // Indicamos que comenzó la operación.
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                operationSuccess = false
            )

            // Enviamos la mascota modificada al Repository.
            val result = petRepository.actualizarMascota(pet)

            result
                .onSuccess {

                    // Firestore actualizó correctamente el documento.
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        operationSuccess = true,
                        errorMessage = null
                    )
                }
                .onFailure { error ->

                    // Se produjo un error durante la actualización.
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        operationSuccess = false,
                        errorMessage = error.message
                            ?: "No se pudo actualizar la mascota"
                    )
                }
        }
    }

    // ========================================================
    // DELETE - ELIMINAR MASCOTA
    // ========================================================
    // Coordina la eliminación de una mascota.
    //
    // El ViewModel:
    // 1. Indica que comenzó la operación.
    // 2. Llama al Repository.
    // 3. Actualiza el estado según el resultado.
    // ========================================================
    fun eliminarMascota(pet: Pet) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                operationSuccess = false
            )

            val result = petRepository.eliminarMascota(pet)

            result
                .onSuccess {
                    // ====================================================
                    // ACTUALIZAR LISTA LOCAL DESPUÉS DEL DELETE
                    // ====================================================
                    // Firestore confirmó que la mascota fue eliminada.
                    //
                    // Como la operación fue exitosa, también eliminamos
                    // la mascota de la lista que mantiene el ViewModel.
                    //
                    // Esto permite que Compose actualice inmediatamente
                    // la interfaz sin tener que salir y volver a entrar
                    // a la pantalla de mascotas.
                    //
                    // Ejemplo:
                    // [Figaro, Bart, Lunita]
                    //       ↓ eliminar Figaro
                    // [Bart, Lunita]
                    // ====================================================

                    _uiState.value = _uiState.value.copy(
                        pets = _uiState.value.pets.filter { it.id != pet.id },
                        isLoading = false,
                        operationSuccess = true,
                        errorMessage = null
                    )
                }
                .onFailure { error ->

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        operationSuccess = false,
                        errorMessage = error.message
                            ?: "No se pudo eliminar la mascota"
                    )
                }
        }
    }

    fun limpiarResultadoOperacion() {

        _uiState.value = _uiState.value.copy(
            operationSuccess = false,
            errorMessage = null
        )
    }
}