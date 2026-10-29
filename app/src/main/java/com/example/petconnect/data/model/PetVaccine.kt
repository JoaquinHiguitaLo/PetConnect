package com.example.petconnect.data.model

/**
 * Registro de una vacuna aplicada a una mascota.
 *
 * Cada registro pertenece a una mascota y a un usuario.
 * Las fechas se almacenan en formato ISO: yyyy-MM-dd.
 */
data class PetVaccine(
    val id: String = "",
    val usuarioId: String = "",
    val mascotaId: String = "",
    val nombreVacuna: String = "",
    val fechaAplicacion: String = "",
    val proximaDosis: String? = null,
    val veterinaria: String = "",
    val observaciones: String = ""
)
