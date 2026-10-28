
package com.example.petconnect.data.model

/**
 * Dirección registrada por un usuario.
 *
 * Las coordenadas permiten buscar servicios
 * cercanos sin depender constantemente del GPS.
 */
data class UserAddress(

    // Identificador del documento en Firestore.
    val id: String = "",

    // UID del propietario de la dirección.
    val usuarioId: String = "",

    // Nombre que identifica la dirección.
    // Ejemplo: Casa, Trabajo u Otra.
    val nombre: String = "",

    // Dirección escrita por el usuario.
    val direccion: String = "",

    // Ciudad o municipio.
    val ciudad: String = "",

    // Departamento.
    val departamento: String = "",

    // Coordenadas seleccionadas en el mapa.
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,

    // Indica si es la dirección que el usuario
    // seleccionó para buscar servicios.
    val esPrincipal: Boolean = false
)
