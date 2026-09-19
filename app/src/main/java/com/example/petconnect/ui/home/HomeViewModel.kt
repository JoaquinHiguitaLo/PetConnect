package com.example.petconnect.ui.home

import androidx.lifecycle.ViewModel
import com.example.petconnect.data.repository.AuthRepository

class HomeViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    // Exponemos el correo del usuario actual, para poder mostrar
    // algo como "Bienvenido, correo@ejemplo.com" en la pantalla.
    val currentUserEmail: String?
        get() = authRepository.currentUser?.email

    // El logout no necesita ser "suspend" (no espera respuesta de internet),
    // ni necesita manejar errores — signOut() de Firebase es una operación
    // local e inmediata, simplemente borra la sesión guardada en el dispositivo.
    fun onLogoutClick() {
        authRepository.logout()
    }
}