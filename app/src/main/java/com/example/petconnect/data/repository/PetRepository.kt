package com.example.petconnect.data.repository

//Representará cómo obtenemos y modificamos mascotas

import com.example.petconnect.data.model.Pet
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PetRepository {


    //Obtiene una instacia/conexión con FireStrore que se utilizará para trabajar con Firestore
    private val db = FirebaseFirestore.getInstance()

    // ============================================================
    // CREATE
    // Crea una nueva mascota en Firestore.
    // El ID del documento y el campo "id" del objeto Pet
    // serán exactamente el mismo valor.
    // ============================================================
    suspend fun crearMascota(pet: Pet): Result<String> {
        return try {

            // Primero pedimos a Firestore que genere un ID para
            // el nuevo documento, pero todavía no guardamos nada.
            val documentReference = db
                .collection("mascotas")
                .document()

            // Creamos una copia de la mascota utilizando como ID
            // el mismo ID que tendrá el documento de Firestore.
            val petConId = pet.copy(
                id = documentReference.id
            )

            // Guardamos la mascota utilizando ese documento.
            documentReference
                .set(petConId)
                .await()

            // Devolvemos el ID generado.
            Result.success(documentReference.id)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    // ============================================================
    // READ
    // Obtiene las mascotas pertenecientes a un usuario.
    // ============================================================
    suspend fun obtenerMascotas(usuarioId: String): Result<List<Pet>> {
        return try {

            val snapshot = db
                .collection("mascotas")
                .whereEqualTo("usuarioId", usuarioId) //equivalente conceptualmente a un filtro
                .get() //Esto ejecuta la consulta contra Firestore
                .await() //Esperar la respuesta dentro de una coroutine

            //Por cada documento de Firestore, se construye un objeto Pet

            val pets = snapshot.documents.map { document ->
                Pet(
                    id = document.id,
                    usuarioId = document.getString("usuarioId") ?: "",
                    nombre = document.getString("nombre") ?: "",
                    especie = document.getString("especie") ?: "",
                    raza = document.getString("raza"),
                    edad = document.getLong("edad")?.toInt() ?: 0,
                    tamano = document.getString("tamano") ?: "",
                    foto = document.getString("foto"),

                    fechaNacimiento = document.getString("fechaNacimiento"),
                    sexo = document.getString("sexo"),
                    pesoKg = document.getDouble("pesoKg")
                        ?: document.getLong("pesoKg")?.toDouble()
                )
            }


            Result.success(pets)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ============================================================
    // UPDATE
    // Actualiza una mascota existente.
    // ============================================================
    suspend fun actualizarMascota(pet: Pet): Result<Unit> {
        return try {

            db
                .collection("mascotas")
                .document(pet.id)
                .set(pet)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


// ============================================================
// DELETE
// Elimina una mascota existente.
// ============================================================


    suspend fun eliminarMascota(pet: Pet): Result<Unit> {
        return try {

            val usuarioActual = com.google.firebase.auth.FirebaseAuth
                .getInstance()
                .currentUser?.uid
                ?: throw IllegalStateException(
                    "No hay un usuario autenticado."
                )

            require(pet.id.isNotBlank()) {
                "La mascota no tiene identificador."
            }

            // Consultamos el perfil real antes de eliminarlo.
            val referenciaMascota = db
                .collection("mascotas")
                .document(pet.id)

            val documentoMascota = referenciaMascota
                .get()
                .await()

            require(documentoMascota.exists()) {
                "La mascota ya no existe."
            }

            require(
                documentoMascota.getString("usuarioId") == usuarioActual
            ) {
                "No puedes eliminar mascotas de otro usuario."
            }

            // Buscamos las vacunas asociadas a esta mascota.
            val vacunasAsociadas = db
                .collection("vacunasMascotas")
                .whereEqualTo("usuarioId", usuarioActual)
                .whereEqualTo("mascotaId", pet.id)
                .get()
                .await()

            // Firestore admite un máximo de 500 escrituras
            // por lote. Reservamos una para la mascota.
            require(vacunasAsociadas.size() < 500) {
                "La mascota tiene demasiadas vacunas para " +
                        "eliminarlas en una sola operación."
            }

            val batch = db.batch()

            // Primero incluimos las vacunas.
            vacunasAsociadas.documents.forEach { vacuna ->
                batch.delete(vacuna.reference)
            }

            // Después incluimos el perfil.
            batch.delete(referenciaMascota)

            // Confirmamos todas las eliminaciones juntas.
            batch.commit().await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
