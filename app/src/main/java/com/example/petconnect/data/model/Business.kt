package com.example.petconnect.data.model

/**
 * Representa un negocio o establecimiento que ofrece
 * servicios relacionados con el cuidado de mascotas.
 */
data class Business(
    val id: String = "",
    val prestadorId: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val imagen: String? = null,
    val calificacion: Double = 0.0,
    val estadoVerificacion: String = "PENDIENTE"
)