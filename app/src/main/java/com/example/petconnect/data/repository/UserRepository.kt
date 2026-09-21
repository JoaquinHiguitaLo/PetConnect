package com.example.petconnect.data.repository

import com.example.petconnect.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repository encargado de gestionar los datos del usuario en Firestore.
 *
 * Firebase Authentication se encarga de:
 * - Crear la cuenta.
 * - Iniciar sesión.
 * - Gestionar la contraseña.
 * - Proporcionar el UID del usuario.
 *
 * Firestore se encarga de almacenar los datos adicionales del perfil:
 * - Nombre completo.
 * - Correo electrónico.
 * - Teléfono.
 * - Foto.
 */
class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Referencia a la colección donde se almacenan los usuarios.
     */
    private val usersCollection = firestore.collection("usuarios")

    /**
     * Crea el documento del usuario en Firestore.
     *
     * El ID del documento será el UID generado por Firebase Authentication.
     *
     * Estructura:
     *
     * usuarios
     *   └── {uid}
     *       ├── nombreCompleto
     *       ├── correo
     *       ├── telefono
     *       └── foto
     */
    suspend fun crearUsuario(user: User): Result<Unit> {
        return try {

            usersCollection
                .document(user.id)
                .set(user)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    /**
     * Obtiene la información de un usuario utilizando su UID.
     *
     * El UID corresponde al ID del documento en Firestore.
     */
    suspend fun obtenerUsuario(uid: String): Result<User?> {
        return try {

            val document = usersCollection
                .document(uid)
                .get()
                .await()

            if (document.exists()) {
                val user = document.toObject(User::class.java)
                Result.success(user)
            } else {
                Result.success(null)
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    /**
     * Actualiza la información del perfil del usuario.
     *
     * update() modifica únicamente los campos enviados y no necesita
     * reemplazar todo el documento.
     */
    suspend fun actualizarUsuario(user: User): Result<Unit> {
        return try {

            usersCollection
                .document(user.id)
                .update(
                    mapOf(
                        "nombreCompleto" to user.nombreCompleto,
                        "correo" to user.correo,
                        "telefono" to user.telefono,
                        "foto" to user.foto
                    )
                )
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}