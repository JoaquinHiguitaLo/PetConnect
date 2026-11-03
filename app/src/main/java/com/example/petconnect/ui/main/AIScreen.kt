package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextOverflow

import com.example.petconnect.viewmodel.PetsViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AIScreen(
    modifier: Modifier = Modifier
) {

    val teal = Color(0xFF227C78)
    val cream = Color(0xFFFAF9F6)
    val lightGreen = Color(0xFFE3F2EF)


    // ViewModel exclusivo de mascotas para el asistente.
    val petsViewModel: PetsViewModel = viewModel(
        key = "ai_pets"
    )
    val petsUiState by petsViewModel.uiState.collectAsState()
    // Usuario autenticado en Firebase.
    val usuarioId = FirebaseAuth.getInstance().currentUser?.uid
    // Mascota seleccionada.
    var mascotaSeleccionadaId by remember {
        mutableStateOf<String?>(null)
    }
    // Servicio seleccionado.
    var necesidadSeleccionada by remember {
        mutableStateOf("veterinaria")
    }
    // Consultamos las mascotas al entrar al asistente.
    LaunchedEffect(usuarioId) {
        if (usuarioId != null) {
            petsViewModel.cargarMascotas(usuarioId)
        }
    }
    // Mascotas reales obtenidas de Firestore.
    val mascotas = petsUiState.pets
    // Seleccionamos automáticamente la primera mascota
    // cuando termina la carga.
    LaunchedEffect(mascotas) {
        if (mascotas.isNotEmpty()) {
            val seleccionExiste = mascotas.any {
                it.id == mascotaSeleccionadaId
            }

            if (!seleccionExiste) {
                mascotaSeleccionadaId = mascotas.first().id
            }
        } else {
            mascotaSeleccionadaId = null
        }
    }

    // Obtenemos la mascota seleccionada.
    val mascotaSeleccionada = mascotas.find {
        it.id == mascotaSeleccionadaId
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(cream)
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 24.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {

            // TÍTULO DEL ASISTENTE
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            color = lightGreen,
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SmartToy,
                        contentDescription = null,
                        tint = teal,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Asistente de recomendaciones IA",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = teal
                    )

                    Text(
                        text = "Encuentra servicios para tu mascota",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF71817E)
                    )
                }
            }

            // SELECCIÓN DE MASCOTA
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Selecciona tu mascota",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF303534)
                )
                when {
                    usuarioId == null -> {
                        Text(
                            text = "Inicia sesión para consultar tus mascotas.",
                            color = Color(0xFF71817E),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    petsUiState.isLoading -> {
                        CircularProgressIndicator(
                            color = teal,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    petsUiState.errorMessage != null -> {
                        Text(
                            text = "No se pudieron cargar tus mascotas.",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    mascotas.isEmpty() -> {
                        Text(
                            text = "Todavía no tienes mascotas registradas.",
                            color = Color(0xFF71817E),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    else -> {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(end = 8.dp)
                        ) {
                            items(
                                items = mascotas,
                                key = { it.id }
                            ) { mascota ->

                                val emoji = when (
                                    mascota.especie.trim().lowercase()
                                ) {
                                    "perro", "canino" -> "🐶"
                                    "gato", "felino" -> "🐱"
                                    else -> "🐾"
                                }

                                PetChip(
                                    emoji = emoji,
                                    text = mascota.nombre,
                                    selected = mascotaSeleccionadaId == mascota.id,
                                    onClick = {
                                        mascotaSeleccionadaId = mascota.id
                                    }
                                )
                            }
                        }
                    }
                }

            }

            // SELECCIÓN DEL SERVICIO
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "¿Qué necesita tu mascota?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF303534)
                )


                val necesidades = listOf(
                    Triple("veterinaria", "Veterinaria", "🏥"),
                    Triple("peluqueria", "Peluquería", "✂️"),
                    Triple("bano", "Baño", "🛁"),
                    Triple("guarderia", "Guardería", "🏠"),
                    Triple("paseadores", "Paseadores", "🐕"),
                    Triple("especialistas", "Especialistas", "🩺")
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(end = 8.dp)
                ) {
                    items(necesidades) { (id, nombre, emoji) ->

                        NeedChip(
                            emoji = emoji,
                            text = nombre,
                            selected = necesidadSeleccionada == id,
                            onClick = {
                                necesidadSeleccionada = id
                            }
                        )
                    }
                }

            }

            // ============================================================
            // CONTENIDO
            // ============================================================

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 18.dp
                    )
            ) {
                // ========================================================
                // ANÁLISIS IA
                // ========================================================

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF7F4EA)
                    )
                ) {

                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {

                        Text(
                            text = "🤖",
                            fontSize = 28.sp
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {

                            Text(
                                text = if (mascotaSeleccionada != null) {
                                    "Recomendaciones para ${mascotaSeleccionada.nombre}"
                                } else {
                                    "Recomendaciones para tu mascota"
                                },
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Explora algunos servicios de ejemplo para tu mascota. " +
                                        "Las recomendaciones personalizadas con IA estarán " +
                                        "disponibles en una próxima versión.",
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // ========================================================
                // RECOMENDACIÓN 1
                // ========================================================

                RecommendationCard(
                    emoji = "🏥",
                    name = "Clínica VetVida",
                    description = "Especialistas en Golden Retriever, vacunas al día",
                    category = "Veterinaria",
                    distance = "0.8 km",
                    rating = "4.9",
                    price = "$25.000",
                    match = "98%",
                    matchColor = Color(0xFF398B5E)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // ========================================================
                // RECOMENDACIÓN 2
                // ========================================================

                RecommendationCard(
                    emoji = "👩‍⚕️",
                    name = "Dra. Sofía Torres",
                    description = "Alta calificación, atención personalizada",
                    category = "Veterinaria",
                    distance = "1.1 km",
                    rating = "4.9",
                    price = "$40.000",
                    match = "95%",
                    matchColor = Color(0xFF398B5E)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // ========================================================
                // RECOMENDACIÓN 3
                // ========================================================

                RecommendationCard(
                    emoji = "🏥",
                    name = "VetPlus 24h",
                    description = "Urgencias 24h, laboratorio en sitio",
                    category = "Veterinaria",
                    distance = "2.3 km",
                    rating = "4.8",
                    price = "$35.000",
                    match = "87%",
                    matchColor = Color.Gray
                )

                Spacer(modifier = Modifier.height(110.dp))
            }
        }
    }

}

@Composable
private fun PetChip(
    emoji: String,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (selected) {
            Color(0xFF227C78)
        } else {
            Color.White
        },
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) {
                Color(0xFF227C78)
            } else {
                Color(0xFFD9DEDC)
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 11.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = emoji,
                fontSize = 18.sp
            )

            Text(
                text = text,
                color = if (selected) Color.White
                else Color(0xFF303534),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}



@Composable
private fun NeedChip(
    emoji: String,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (selected) {
            Color(0xFF227C78)
        } else {
            Color.White
        },
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) {
                Color(0xFF227C78)
            } else {
                Color(0xFFD9DEDC)
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 11.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = emoji,
                fontSize = 17.sp
            )

            Text(
                text = text,
                color = if (selected) Color.White
                else Color(0xFF303534),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Composable
private fun RecommendationCard(
    emoji: String,
    name: String,
    description: String,
    category: String,
    distance: String,
    rating: String,
    price: String,
    match: String,
    matchColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Color(0xFFE5E9E7)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // NOMBRE, DESCRIPCIÓN E ICONO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            Color(0xFFE3F2EF),
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 25.sp
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF303534)
                    )

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF71817E)
                    )
                }
            }

            HorizontalDivider(
                color = Color(0xFFE5E9E7)
            )

            // CATEGORÍA
            Row {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFB9E7E4)
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF227C78),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // DISTANCIA Y CALIFICACIÓN
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "📍 $distance",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF71817E)
                )

                Text(
                    text = "⭐ $rating",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF303534)
                )
            }

            // PRECIO DEMOSTRATIVO
            Text(
                text = price,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF227C78)
            )
        }
    }
}



