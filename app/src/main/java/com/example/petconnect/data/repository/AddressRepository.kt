
package com.example.petconnect.data.repository

import com.example.petconnect.data.model.UserAddress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AddressRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    /**
     * Obtiene el UID del usuario autenticado.
     */
    private fun obtenerUsuarioId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException(
                "Debes iniciar sesión para gestionar direcciones."
            )
    }

    /**
     * Registra una dirección en Firestore.
     */
    suspend fun crearDireccion(
        direccion: UserAddress
    ): Result<String> {

        return try {

            val usuarioId = obtenerUsuarioId()

            val documento = db
                .collection("direcciones")
                .document()

            val direccionNueva = direccion.copy(
                id = documento.id,
                usuarioId = usuarioId,
                esPrincipal = false
            )

            documento.set(direccionNueva).await()

            Result.success(documento.id)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Consulta únicamente las direcciones
     * pertenecientes al usuario autenticado.
     */
    suspend fun obtenerMisDirecciones():
            Result<List<UserAddress>> {

        return try {

            val usuarioId = obtenerUsuarioId()

            val snapshot = db
                .collection("direcciones")
                .whereEqualTo("usuarioId", usuarioId)
                .get()
                .await()

            val direcciones = snapshot.documents.mapNotNull {
                    documento ->

                documento.toObject(UserAddress::class.java)
                    ?.copy(id = documento.id)
            }

            Result.success(direcciones)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Selecciona una dirección como ubicación activa.
     *
     * Se utiliza un único documento por usuario,
     * evitando varias selecciones simultáneas.
     */
    suspend fun seleccionarDireccionActiva(
        direccionId: String
    ): Result<Unit> {

        return try {

            val usuarioId = obtenerUsuarioId()

            require(direccionId.isNotBlank()) {
                "Debes seleccionar una dirección válida."
            }

            db.collection("direccionesActivas")
                .document(usuarioId)
                .set(
                    mapOf(
                        "direccionId" to direccionId
                    )
                )
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Consulta el identificador de la dirección
     * actualmente seleccionada.
     */
    suspend fun obtenerDireccionActivaId():
            Result<String?> {

        return try {

            val usuarioId = obtenerUsuarioId()

            val documento = db
                .collection("direccionesActivas")
                .document(usuarioId)
                .get()
                .await()

            val direccionId = if (documento.exists()) {
                documento.getString("direccionId")
            } else {
                null
            }

            Result.success(direccionId)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina una dirección del usuario.
     *
     * Firestore también verificará la propiedad
     * mediante sus reglas de seguridad.
     */
    suspend fun eliminarDireccion(
        direccionId: String
    ): Result<Unit> {

        return try {

            obtenerUsuarioId()

            require(direccionId.isNotBlank()) {
                "La dirección no tiene un ID válido."
            }

            db.collection("direcciones")
                .document(direccionId)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
