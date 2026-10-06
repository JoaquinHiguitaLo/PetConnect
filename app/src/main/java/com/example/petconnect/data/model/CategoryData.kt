package com.example.petconnect.data.model

/**
 * Lista de categorías disponibles en PetConnect.
 *
 * Estas categorías son datos locales del MVP y no requieren
 * una consulta a Firebase.
 */
val petConnectCategories = listOf(
    Category(
        id = "veterinaria",
        nombre = "Veterinaria",
        icono = "🐾"
    ),
    Category(
        id = "peluqueria",
        nombre = "Peluquería y estética",
        icono = "✂️"
    ),
    Category(
        id = "bano",
        nombre = "Baño",
        icono = "🛁"
    ),
    Category(
        id = "guarderia",
        nombre = "Guardería",
        icono = "🏠"
    ),
    Category(
        id = "paseadores",
        nombre = "Paseadores",
        icono = "🦮"
    ),
    Category(
        id = "adiestramiento",
        nombre = "Adiestramiento",
        icono = "🎓"
    ),
    Category(
        id = "farmacias",
        nombre = "Farmacias",
        icono = "💊"
    ),
    Category(
        id = "tiendas",
        nombre = "Tiendas",
        icono = "🛍️"
    )
)