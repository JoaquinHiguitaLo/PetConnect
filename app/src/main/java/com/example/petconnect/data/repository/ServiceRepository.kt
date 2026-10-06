package com.example.petconnect.data.repository

import com.example.petconnect.data.model.Service
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ServiceRepository {

    private val db = FirebaseFirestore.getInstance()

    /**
     * Crea un nuevo servicio en Firestore.
     *
     * El ID generado por Firestore se guarda también
     * dentro del objeto Service.
     */
    suspend fun crearServicio(
        service: Service
    ): Result<String> {
        return try {

            val documentReference = db
                .collection("servicios")
                .document()

            val serviceConId = service.copy(
                id = documentReference.id
            )

            documentReference
                .set(serviceConId)
                .await()

            Result.success(documentReference.id)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene todos los servicios asociados a un negocio.
     *
     * Utiliza businessId para consultar únicamente
     * los servicios pertenecientes al negocio indicado.
     */
    suspend fun obtenerServiciosPorNegocio(
        businessId: String
    ): Result<List<Service>> {
        return try {

            val snapshot = db
                .collection("servicios")
                .whereEqualTo("businessId", businessId)
                .get()
                .await()

            val services = snapshot.documents.map { document ->
                Service(
                    id = document.id,
                    businessId = document.getString("businessId") ?: "",
                    nombre = document.getString("nombre") ?: "",
                    descripcion = document.getString("descripcion") ?: "",
                    precio = document.getDouble("precio") ?: 0.0,
                    activo = document.getBoolean("activo") ?: true
                )
            }

            Result.success(services)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene un servicio específico utilizando su ID.
     *
     * Se utiliza para consultar la información completa
     * de un servicio seleccionado por el usuario.
     */
    suspend fun obtenerServicioPorId(
        serviceId: String
    ): Result<Service> {
        return try {

            val document = db
                .collection("servicios")
                .document(serviceId)
                .get()
                .await()

            if (!document.exists()) {
                return Result.failure(
                    Exception("El servicio no existe.")
                )
            }

            val service = Service(
                id = document.id,
                businessId = document.getString("businessId") ?: "",
                nombre = document.getString("nombre") ?: "",
                descripcion = document.getString("descripcion") ?: "",
                precio = document.getDouble("precio") ?: 0.0,
                activo = document.getBoolean("activo") ?: true
            )

            Result.success(service)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualiza la información de un servicio existente.
     *
     * Utiliza el ID del servicio para modificar
     * el documento correspondiente en Firestore.
     */
    suspend fun actualizarServicio(
        service: Service
    ): Result<Unit> {
        return try {

            db.collection("servicios")
                .document(service.id)
                .set(service)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina un servicio de Firestore.
     *
     * Utiliza el ID del servicio para localizar y eliminar
     * el documento correspondiente.
     */
    suspend fun eliminarServicio(
        service: Service
    ): Result<Unit> {
        return try {

            db.collection("servicios")
                .document(service.id)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}