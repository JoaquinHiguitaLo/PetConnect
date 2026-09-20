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
    // ============================================================
    suspend fun crearMascota(pet: Pet): Result<String> {
        return try {

            val documentReference = db
                .collection("mascotas")
                .add(pet)
                .await() //Espera a que Firebase termine esta operación y luego continúa

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
                    foto = document.getString("foto")
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

            db.collection("mascotas")
                .document(pet.id)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
