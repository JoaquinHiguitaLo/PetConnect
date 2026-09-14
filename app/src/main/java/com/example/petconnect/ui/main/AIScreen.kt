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

@Composable
fun AIScreen(
    modifier: Modifier = Modifier
) {

    val teal = Color(0xFF217A76)
    val cream = Color(0xFFFAF9F5)
    val orange = Color(0xFFE59A18)
    val lightGreen = Color(0xFFE8F4EE)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(cream)
    ) {

        // ============================================================
        // ENCABEZADO
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = teal,
                    shape = RoundedCornerShape(
                        bottomStart = 28.dp,
                        bottomEnd = 28.dp
                    )
                )
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 28.dp,
                    bottom = 20.dp
                )
        ) {

            Text(
                text = "ASISTENTE INTELIGENTE",
                color = Color(0xFF9ED8D4),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Recomendaciones IA ✨",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ========================================================
            // SELECCIONA TU MASCOTA
            // ========================================================

            Text(
                text = "SELECCIONA TU MASCOTA",
                color = Color(0xFF9ED8D4),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                PetChip(
                    emoji = "🐕",
                    text = "Luna",
                    selected = true
                )

                PetChip(
                    emoji = "🐈",
                    text = "Michi",
                    selected = false
                )

                PetChip(
                    emoji = "🐶",
                    text = "Coco",
                    selected = false
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ========================================================
            // NECESIDAD
            // ========================================================

            Text(
                text = "¿QUÉ NECESITA?",
                color = Color(0xFF9ED8D4),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                NeedChip(
                    emoji = "🏥",
                    text = "Veterinaria",
                    selected = true
                )

                NeedChip(
                    emoji = "✂️",
                    text = "Peluquería",
                    selected = false
                )

                NeedChip(
                    emoji = "🍖",
                    text = "Alimentación",
                    selected = false
                )
            }
        }

        // ============================================================
        // CONTENIDO
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                            text = "Análisis para 🐕 Luna",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Basado en su raza, edad y necesidades, "
                                    + "encontré 3 opciones ideales cerca de ti "
                                    + "ordenadas por compatibilidad.",
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

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PetChip(
    emoji: String,
    text: String,
    selected: Boolean
) {

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = if (selected) {
            Color.White
        } else {
            Color(0xFF398985)
        }
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 11.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = emoji,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                color = if (selected) {
                    Color(0xFF217A76)
                } else {
                    Color.White
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun NeedChip(
    emoji: String,
    text: String,
    selected: Boolean
) {

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = if (selected) {
            Color(0xFFE59A18)
        } else {
            Color(0xFF398985)
        }
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 11.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = emoji,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                color = Color.White,
                fontSize = 15.sp,
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
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {

            // ========================================================
            // ICONO
            // ========================================================

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = Color(0xFFF5F6F1),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = emoji,
                    fontSize = 30.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // ========================================================
            // INFORMACIÓN
            // ========================================================

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFE0A8)
                    ) {

                        Text(
                            text = category,
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 4.dp
                            ),
                            fontSize = 11.sp,
                            color = Color(0xFFB56B00),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "📍 $distance",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "⭐ $rating",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = price,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF217A76)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // ========================================================
            // MATCH
            // ========================================================

            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFF0F5F0),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 8.dp
                    ),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = match,
                        color = matchColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "match",
                        color = matchColor,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

