package com.example.petconnect.data.model

//Representa qué es un usuario

data class User(
    val id: String = "",
    val nombreCompleto: String = "",
    val correo: String = "",
    val telefono: String = "",
    val foto: String? = null
)