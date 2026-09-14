package com.example.petconnect.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // ============================================================
        // HEADER
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp
                    )
                )
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 20.dp,
                    bottom = 20.dp
                )
        ) {

            Text(
                text = "UBICACIÓN ACTUAL",
                color = MaterialTheme.colorScheme.primaryContainer,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "📍 Antioquia, Medellín",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Buscar veterinarias, paseadores..."
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )
        }


        // ============================================================
        // CONTENIDO PRINCIPAL
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Buenos días 👋",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Hola, Manuela",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )


            // ========================================================
            // CATEGORÍAS
            // ========================================================

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                CategoryChip(
                    icon = "🏥",
                    text = "Veterinarias",
                    selected = true,
                    onClick = { }
                )

                CategoryChip(
                    icon = "✂️",
                    text = "Peluquerías",
                    selected = false,
                    onClick = { }
                )

                CategoryChip(
                    icon = "🐕",
                    text = "Paseadores",
                    selected = false,
                    onClick = { }
                )

                CategoryChip(
                    icon = "🏨",
                    text = "Guarderías",
                    selected = false,
                    onClick = { }
                )
            }


            // ========================================================
            // SERVICIOS DESTACADOS
            // ========================================================

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Servicios destacados",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Ver todos",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ServiceCard(
                    emoji = "🏥",
                    name = "Clínica VetVida",
                    category = "Veterinaria",
                    distance = "0.8 km",
                    rating = "4.9",
                    reviews = "128"
                )

                ServiceCard(
                    emoji = "✂️",
                    name = "PeloPet Spa",
                    category = "Peluquería",
                    distance = "1.2 km",
                    rating = "4.7",
                    reviews = "84"
                )
            }


            // ========================================================
            // RECOMENDACIONES
            // ========================================================

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Recomendados para Luna 🐾",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Ver más",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            RecommendationCard(
                emoji = "🐕",
                name = "Carlos el Paseador",
                category = "Paseador",
                distance = "0.4 km",
                rating = "5.0",
                price = "$15.000/paseo"
            )

            Spacer(modifier = Modifier.height(10.dp))

            RecommendationCard(
                emoji = "👩‍⚕️",
                name = "Dra. Sofía Torres",
                category = "Veterinaria",
                distance = "0.7 km",
                rating = "4.9",
                price = "Desde $30.000"
            )


            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}


// ====================================================================
// COMPONENTE: CATEGORÍA
// ====================================================================

@Composable
private fun CategoryChip(
    icon: String,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surface
        ),
        border =
            if (!selected)
                androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant
                )
            else
                null
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 10.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = icon)

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = text,
                color =
                    if (selected)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


// ====================================================================
// COMPONENTE: TARJETA DE SERVICIO
// ====================================================================

@Composable
private fun ServiceCard(
    emoji: String,
    name: String,
    category: String,
    distance: String,
    rating: String,
    reviews: String
) {

    Card(
        modifier = Modifier.width(250.dp),
        shape = RoundedCornerShape(18.dp)
    ) {

        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = emoji,
                    style = MaterialTheme.typography.displaySmall
                )
            }

            Column(
                modifier = Modifier.padding(14.dp)
            ) {

                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$category · $distance",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "⭐ $rating ($reviews)",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


// ====================================================================
// COMPONENTE: RECOMENDACIÓN
// ====================================================================

@Composable
private fun RecommendationCard(
    emoji: String,
    name: String,
    category: String,
    distance: String,
    rating: String,
    price: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = emoji,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "$category · $distance",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = "⭐ $rating",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = price,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


// ====================================================================
// NAVEGACIÓN INFERIOR
// ====================================================================

@Composable
private fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onMapClick: () -> Unit,
    onPetsClick: () -> Unit,
    onAiClick: () -> Unit,
    onProfileClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            NavigationItem(
                icon = "🏠",
                label = "Inicio",
                selected = true,
                onClick = onHomeClick
            )

            NavigationItem(
                icon = "🗺️",
                label = "Mapa",
                selected = false,
                onClick = onMapClick
            )

            NavigationItem(
                icon = "🐾",
                label = "Mascotas",
                selected = false,
                onClick = onPetsClick
            )

            NavigationItem(
                icon = "✨",
                label = "IA",
                selected = false,
                onClick = onAiClick
            )

            NavigationItem(
                icon = "👤",
                label = "Perfil",
                selected = false,
                onClick = onProfileClick
            )
        }
    }
}


// ====================================================================
// COMPONENTE: ITEM DE NAVEGACIÓN
// ====================================================================

@Composable
private fun NavigationItem(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {

            Text(text = icon)

            Text(
                text = label,
                color =
                    if (selected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium,
                fontWeight =
                    if (selected)
                        FontWeight.Bold
                    else
                        FontWeight.Normal
            )
        }
    }
}