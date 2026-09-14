package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F8F4))
    ) {

        // ============================================================
        // BUSCADOR Y FILTROS
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9F8F4))
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 16.dp,
                    bottom = 12.dp
                )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = "Buscar en el mapa...",
                            color = Color(0xFF8A8A8A)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color(0xFF227A76)
                    )
                )

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtros",
                        tint = Color(0xFF227A76)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ========================================================
            // CATEGORÍAS
            // ========================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                MapCategoryChip(
                    text = "Todos",
                    selected = true
                )

                MapCategoryChip(
                    text = "🏥 Veterinarias",
                    selected = false
                )

                MapCategoryChip(
                    text = "✂️ Peluquerías",
                    selected = false
                )

                MapCategoryChip(
                    text = "🐕 Paseadores",
                    selected = false
                )

                MapCategoryChip(
                    text = "🏨 Guarderías",
                    selected = false
                )
            }
        }

        // ============================================================
        // MAPA
        // ============================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFE4F1EE))
        ) {

            // Calles horizontales
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .align(Alignment.Center)
                    .background(Color.White)
            )

            // Calle vertical
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .fillMaxHeight()
                    .align(Alignment.Center)
                    .background(Color.White)
            )

            // Bloques simulando edificios
            MapBlock(
                modifier = Modifier
                    .width(130.dp)
                    .height(105.dp)
                    .align(Alignment.TopStart)
                    .offset(x = 20.dp, y = 20.dp)
            )

            MapBlock(
                modifier = Modifier
                    .width(140.dp)
                    .height(105.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-20).dp, y = 20.dp)
            )

            MapBlock(
                modifier = Modifier
                    .width(130.dp)
                    .height(110.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = 20.dp, y = 45.dp)
            )

            MapBlock(
                modifier = Modifier
                    .width(140.dp)
                    .height(110.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-20).dp, y = 45.dp)
            )

            MapBlock(
                modifier = Modifier
                    .width(220.dp)
                    .height(115.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = (-20).dp)
            )

            // ========================================================
            // MARCADORES
            // ========================================================

            MapMarker(
                emoji = "🏥",
                color = Color(0xFF227A76),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 85.dp, y = 95.dp)
            )

            MapMarker(
                emoji = "🏥",
                color = Color(0xFF227A76),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(x = 10.dp, y = 60.dp)
            )

            MapMarker(
                emoji = "✂️",
                color = Color(0xFFD78C00),
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 80.dp, y = 20.dp)
            )

            MapMarker(
                emoji = "🐕",
                color = Color(0xFF3F925B),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = 145.dp, y = 50.dp)
            )

            MapMarker(
                emoji = "⭐",
                color = Color(0xFF7350B5),
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = (-35).dp, y = 100.dp)
            )

            // Ubicación del usuario
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Mi ubicación",
                    tint = Color(0xFF227A76),
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        // ============================================================
        // SERVICIOS CERCANOS
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 14.dp,
                    bottom = 12.dp
                )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Servicios cercanos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "6 encontrados",
                    color = Color(0xFF777777),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Ordenar ▾",
                    color = Color(0xFF227A76),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                NearbyServiceCard(
                    emoji = "🏥",
                    name = "Clínica VetVida",
                    distance = "0.8 km",
                    rating = "4.9",
                    modifier = Modifier.weight(1f)
                )

                NearbyServiceCard(
                    emoji = "🐕",
                    name = "Carlos el Paseador",
                    distance = "0.4 km",
                    rating = "5",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


// ====================================================================
// CHIP DE CATEGORÍA
// ====================================================================

@Composable
private fun MapCategoryChip(
    text: String,
    selected: Boolean
) {

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (selected) {
            Color(0xFF227A76)
        } else {
            Color.White
        },
        shadowElevation = if (selected) 0.dp else 2.dp
    ) {

        Text(
            text = text,
            color = if (selected) {
                Color.White
            } else {
                Color(0xFF222222)
            },
            fontSize = 14.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 10.dp
            )
        )
    }
}


// ====================================================================
// BLOQUE DEL MAPA
// ====================================================================

@Composable
private fun MapBlock(
    modifier: Modifier
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFCDE8E3))
    )
}


// ====================================================================
// MARCADOR DEL MAPA
// ====================================================================

@Composable
private fun MapMarker(
    emoji: String,
    color: Color,
    modifier: Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = emoji,
                fontSize = 20.sp
            )
        }

        Box(
            modifier = Modifier
                .size(12.dp)
                .offset(y = (-5).dp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 4.dp
                    )
                )
        )
    }
}


// ====================================================================
// TARJETA DE SERVICIO CERCANO
// ====================================================================

@Composable
private fun NearbyServiceCard(
    emoji: String,
    name: String,
    distance: String,
    rating: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFAF9F5)
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE5F1EE)),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = emoji,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = distance,
                color = Color(0xFF777777),
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "★ $rating",
                    color = Color(0xFFD58A00),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFE4F1E5)
                ) {

                    Text(
                        text = "Abierto",
                        color = Color(0xFF39844E),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        )
                    )
                }
            }
        }
    }
}