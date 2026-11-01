
package com.example.petconnect.data.model

data class Pet(
    val id: String = "",
    val usuarioId: String = "",
    val nombre: String = "",
    val especie: String = "",
    val raza: String? = null,
    val edad: Int = 0,
    val tamano: String = "",
    val foto: String? = null,

    // Información complementaria
    val fechaNacimiento: String? = null,
    val sexo: String? = null,
    val pesoKg: Double? = null
)
