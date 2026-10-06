package com.example.petconnect.data.repository


import com.example.petconnect.data.model.Business
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BusinessRepository {

    private val db = FirebaseFirestore.getInstance()

    /**
     * Crea un nuevo negocio en Firestore.
     *
     * El ID generado por Firestore se guarda también
     * dentro del objeto Business.
     */
    suspend fun crearNegocio(business: Business): Result<String> {
        return try {

            val documentReference = db
                .collection("negocios")
                .document()

            val businessConId = business.copy(
                id = documentReference.id
            )

            documentReference
                .set(businessConId)
                .await()

            Result.success(documentReference.id)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    /**
     * Obtiene los negocios registrados por un prestador.
     *
     * Se utiliza el prestadorId para consultar únicamente
     * los negocios asociados al usuario autenticado.
     */
    suspend fun obtenerNegociosPorPrestador(
        prestadorId: String
    ): Result<List<Business>> {
        return try {

            val snapshot = db
                .collection("negocios")
                .whereEqualTo("prestadorId", prestadorId)
                .get()
                .await()

            val businesses = snapshot.documents.map { document ->
                Business(
                    id = document.id,
                    prestadorId = document.getString("prestadorId") ?: "",
                    nombre = document.getString("nombre") ?: "",
                    categoria = document.getString("categoria") ?: "",
                    descripcion = document.getString("descripcion") ?: "",
                    direccion = document.getString("direccion") ?: "",
                    telefono = document.getString("telefono") ?: "",
                    latitud = document.getDouble("latitud") ?: 0.0,
                    longitud = document.getDouble("longitud") ?: 0.0,
                    imagen = document.getString("imagen"),
                    calificacion = document.getDouble("calificacion") ?: 0.0,
                    estadoVerificacion = document.getString("estadoVerificacion")
                        ?: "PENDIENTE"
                )
            }

            Result.success(businesses)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


/**
 * Obtiene un negocio específico utilizando su ID.
 *
 * Se utiliza para consultar la información completa
 * de un negocio seleccionado por el usuario.
 */
suspend fun obtenerNegocioPorId(
    businessId: String
): Result<Business> {
    return try {

        val document = db
            .collection("negocios")
            .document(businessId)
            .get()
            .await()

        if (!document.exists()) {
            return Result.failure(
                Exception("El negocio no existe.")
            )
        }

        val business = Business(
            id = document.id,
            prestadorId = document.getString("prestadorId") ?: "",
            nombre = document.getString("nombre") ?: "",
            categoria = document.getString("categoria") ?: "",
            descripcion = document.getString("descripcion") ?: "",
            direccion = document.getString("direccion") ?: "",
            telefono = document.getString("telefono") ?: "",
            latitud = document.getDouble("latitud") ?: 0.0,
            longitud = document.getDouble("longitud") ?: 0.0,
            imagen = document.getString("imagen"),
            calificacion = document.getDouble("calificacion") ?: 0.0,
            estadoVerificacion = document.getString("estadoVerificacion")
                ?: "PENDIENTE"
        )

        Result.success(business)

    } catch (e: Exception) {
        Result.failure(e)
    }
}

    /**
     * Actualiza la información de un negocio existente.
     *
     * Utiliza el ID del negocio para modificar el documento
     * correspondiente en Firestore.
     */
    suspend fun actualizarNegocio(
        business: Business
    ): Result<Unit> {
        return try {

            db.collection("negocios")
                .document(business.id)
                .set(business)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina un negocio de Firestore.
     *
     * Utiliza el ID del negocio para localizar y eliminar
     * el documento correspondiente.
     */
    suspend fun eliminarNegocio(
        business: Business
    ): Result<Unit> {
        return try {

            db.collection("negocios")
                .document(business.id)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}