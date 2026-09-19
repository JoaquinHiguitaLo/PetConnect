package com.example.petconnect.data.model

//Representa qué es una mascota

data class Pet(
    val id: String = "",
    val usuarioId: String = "",
    val nombre: String = "",
    val especie: String = "",
    val raza: String? = null,
    val edad: Int = 0,
    val tamano: String = "",
    val foto: String? = null
)