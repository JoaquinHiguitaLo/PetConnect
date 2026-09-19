package com.example.petconnect.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

// Representa el resultado de una operación de autenticación.
// En vez de devolver un booleano simple, usamos una "sealed class"
// para poder llevar información adicional en cada caso (el usuario o el mensaje de error).
sealed class AuthResult {
    data class Success(val user: FirebaseUser) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthRepository {

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    // Esta es la instancia de Firebase Auth que ya está conectada
    // a tu proyecto gracias al google-services.json
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Función para registrar un nuevo usuario.
    // "suspend" significa que esta función puede tardar (llama a internet)
    // y no debe bloquear la pantalla mientras espera respuesta.
    suspend fun register(email: String, password: String): AuthResult {
        return try {
            // Esta es la llamada real a Firebase.
            // .await() convierte la respuesta de Firebase (que normalmente es asíncrona
            // con callbacks) en algo que podemos "esperar" de forma secuencial y más legible.
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            val user = result.user
            if (user != null) {
                AuthResult.Success(user)
            } else {
                AuthResult.Error("No se pudo crear la cuenta.")
            }
        } catch (e: Exception) {
            // Si Firebase lanza un error (correo repetido, contraseña débil, etc.)
            // lo capturamos aquí y lo traducimos a un mensaje entendible.
            AuthResult.Error(mapFirebaseError(e))
        }
    }

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                AuthResult.Success(user)
            } else {
                AuthResult.Error("No se pudo iniciar sesión.")
            }
        } catch (e: Exception) {
            AuthResult.Error(mapFirebaseError(e))
        }
    }


    fun logout() {
        firebaseAuth.signOut()
    }
    // Esta función traduce los mensajes de error de Firebase (que vienen en inglés
    // y son técnicos) a mensajes en español que el usuario pueda entender.
    private fun mapFirebaseError(e: Exception): String {
        val msg = e.message ?: ""
        return when {
            msg.contains("email address is already in use", ignoreCase = true) ->
                "Este correo electrónico ya se encuentra registrado."
            msg.contains("badly formatted", ignoreCase = true) ->
                "El formato del correo electrónico no es válido."
            msg.contains("network error", ignoreCase = true) ->
                "Error de conexión. Verifica tu internet."
            msg.contains("weak password", ignoreCase = true) ->
                "La contraseña es demasiado débil (mínimo 6 caracteres)."
            msg.contains("no user record", ignoreCase = true) ->
                "No existe una cuenta con este correo electrónico."
            msg.contains("password is invalid", ignoreCase = true) ->
                "La contraseña es incorrecta."
            msg.contains("The supplied auth credential is incorrect", ignoreCase = true) ->
                "Correo o contraseña incorrectos."
            else -> "Ocurrió un error inesperado. Intenta de nuevo."
        }
    }
}