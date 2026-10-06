package com.example.petconnect.data.model

/**
 * Representa un servicio ofrecido por un negocio registrado
 * en PetConnect.
 */
data class Service(
    val id: String = "",
    val businessId: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val activo: Boolean = true
)