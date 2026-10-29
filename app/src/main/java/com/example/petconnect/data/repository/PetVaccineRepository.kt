
package com.example.petconnect.data.repository

import com.example.petconnect.data.model.PetVaccine
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PetVaccineRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val vacunas = db.collection("vacunasMascotas")

    private fun obtenerUsuarioId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException(
                "No hay un usuario autenticado."
            )
    }

    // =====================================================
    // REGISTRAR VACUNA
    // =====================================================

    suspend fun registrarVacuna(
        vacuna: PetVaccine
    ): Result<Unit> = runCatching {

        val uid = obtenerUsuarioId()

        require(vacuna.mascotaId.isNotBlank()) {
            "Debes seleccionar una mascota."
        }

        require(vacuna.nombreVacuna.isNotBlank()) {
            "Debes ingresar el nombre de la vacuna."
        }

        require(vacuna.fechaAplicacion.isNotBlank()) {
            "Debes ingresar la fecha de aplicación."
        }

        // La asociación entre mascota y usuario se
        // verificará también en las reglas de Firestore.
        val documento = vacunas.document()

        val nuevaVacuna = vacuna.copy(
            id = documento.id,
            usuarioId = uid
        )

        documento.set(nuevaVacuna).await()
    }

    // =====================================================
    // CONSULTAR VACUNAS DE UNA MASCOTA
    // =====================================================

    suspend fun obtenerVacunasPorMascota(
        mascotaId: String
    ): Result<List<PetVaccine>> = runCatching {

        val uid = obtenerUsuarioId()

        require(mascotaId.isNotBlank()) {
            "El identificador de la mascota es obligatorio."
        }

        val resultado = vacunas
            .whereEqualTo("usuarioId", uid)
            .whereEqualTo("mascotaId", mascotaId)
            .get()
            .await()

        resultado.documents
            .mapNotNull { documento ->
                documento.toObject(PetVaccine::class.java)
            }
            .sortedByDescending { it.fechaAplicacion }
    }

    // =====================================================
    // ACTUALIZAR VACUNA
    // =====================================================

    suspend fun actualizarVacuna(
        vacuna: PetVaccine
    ): Result<Unit> = runCatching {

        val uid = obtenerUsuarioId()

        require(vacuna.id.isNotBlank()) {
            "La vacuna no tiene identificador."
        }

        require(vacuna.usuarioId == uid) {
            "No puedes modificar vacunas de otro usuario."
        }

        require(vacuna.mascotaId.isNotBlank()) {
            "La mascota es obligatoria."
        }

        require(vacuna.nombreVacuna.isNotBlank()) {
            "El nombre de la vacuna es obligatorio."
        }

        require(vacuna.fechaAplicacion.isNotBlank()) {
            "La fecha de aplicación es obligatoria."
        }

        vacunas
            .document(vacuna.id)
            .set(vacuna)
            .await()
    }

    // =====================================================
    // ELIMINAR VACUNA
    // =====================================================

    suspend fun eliminarVacuna(
        vacunaId: String
    ): Result<Unit> = runCatching {

        obtenerUsuarioId()

        require(vacunaId.isNotBlank()) {
            "El identificador de la vacuna es obligatorio."
        }

        vacunas
            .document(vacunaId)
            .delete()
            .await()
    }
}
