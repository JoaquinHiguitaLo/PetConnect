package com.example.petconnect.ui.home

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.petconnect.data.model.UserAddress
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.petconnect.viewmodel.BusinessViewModel
import com.example.petconnect.data.model.Business
import android.location.Location
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import java.util.Locale

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    onViewAllCategories: () -> Unit,
    onCategorySelected: (String) -> Unit,
    nombreUsuario: String,
    onAddressesClick: () -> Unit = {},
    direccionActiva: UserAddress? = null,
    onBusinessSelected: (String) -> Unit = {},
    onViewAllFeatured: () -> Unit = {}
) {
    var textoBusqueda by remember { mutableStateOf("") }

    // ViewModel exclusivo para los servicios destacados de Inicio.
    val homeBusinessViewModel: BusinessViewModel = viewModel(
        key = "home_featured_businesses"
    )

    val homeBusinessUiState by homeBusinessViewModel.uiState.collectAsState()

    // Consultamos los establecimientos aprobados.
    LaunchedEffect(homeBusinessViewModel) {
        homeBusinessViewModel.cargarNegociosAprobados()
    }

    // Los servicios destacados se ordenan por calificación.
    val serviciosDestacados = homeBusinessUiState.businesses
        .filter { business ->
            business.estadoVerificacion == "APROBADO"
        }
        .sortedByDescending { business ->
            business.calificacion
        }
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
                    top = 45.dp,
                    bottom = 20.dp
                )
        ) {

            // --------------------------------------------------------
            // UBICACIÓN + BOTONES SUPERIORES
            // --------------------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Información de ubicación
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onAddressesClick()
                        }
                ) {

                    Text(
                        text = "MI UBICACIÓN ▾",
                        color = MaterialTheme.colorScheme.primaryContainer,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))


                    Column {

                        Text(
                            text = if (direccionActiva != null) {
                                "📍 ${direccionActiva.nombre} · ${direccionActiva.ciudad}"
                            } else {
                                "📍 Seleccionar dirección"
                            },
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        if (direccionActiva != null) {

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = direccionActiva.direccion,
                                color = Color.White.copy(alpha = 0.85f),
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1
                            )
                        }
                    }

                }

                // Botón de notificaciones
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.15f)
                    )
                ) {
                    Text(
                        text = "🔔",
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

            }

            Spacer(modifier = Modifier.height(16.dp))

            // --------------------------------------------------------
            // BUSCADOR
            // --------------------------------------------------------

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Buscar veterinarias, paseadores...")
                },
                leadingIcon = {
                    Text("🔍")
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White.copy(alpha = 0.08f),
                    focusedContainerColor = Color.White.copy(alpha = 0.12f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.30f),
                    focusedBorderColor = Color.White.copy(alpha = 0.60f),
                    unfocusedPlaceholderColor = Color.White.copy(alpha = 0.70f),
                    focusedPlaceholderColor = Color.White.copy(alpha = 0.85f),
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White
                )
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
                text = "Hola, ${nombreUsuario.substringBefore(" ").ifBlank { "usuario" }}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )


            // ========================================================
            // CATEGORÍAS
            // ========================================================

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Categorías",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ver todas",
                    modifier = Modifier.clickable {
                        onViewAllCategories()
                    },
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CategoryChip(
                    icon = "🏥",
                    text = "Veterinarias",
                    selected = false,
                    onClick = {
                        onCategorySelected("veterinaria")
                    }
                )

                CategoryChip(
                    icon = "✂️",
                    text = "Peluquerías",
                    selected = false,
                    onClick = {
                        onCategorySelected("peluqueria")
                    }
                )

                CategoryChip(
                    icon = "🐕",
                    text = "Paseadores",
                    selected = false,
                    onClick = {
                        onCategorySelected("paseadores")
                    }
                )

                CategoryChip(
                    icon = "🏨",
                    text = "Guarderías",
                    selected = false,
                    onClick = {
                        onCategorySelected("guarderia")
                    }
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

                    TextButton(
                        onClick = onViewAllFeatured
                    ){
                        Text(
                            text = "Ver todos",
                            modifier = Modifier.clickable {
                                onViewAllCategories()
                            },
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium)
                    }

                }

                Spacer(modifier = Modifier.height(12.dp))


            if (homeBusinessUiState.isLoading) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            } else if (homeBusinessUiState.errorMessage != null) {

                Text(
                    text = "No se pudieron cargar los servicios destacados.",
                    color = MaterialTheme.colorScheme.error
                )

            } else if (serviciosDestacados.isEmpty()) {

                Text(
                    text = "Todavía no hay servicios destacados disponibles.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            } else {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    serviciosDestacados.forEach { negocio ->

                        ServiceCard(
                            emoji = when (
                                negocio.categoria.trim().lowercase()
                            ) {
                                "veterinaria", "veterinarias" -> "🏥"
                                "peluqueria", "peluquería", "baño" -> "✂️"
                                "paseador", "paseadores" -> "🐕"
                                "guarderia", "guardería" -> "🏨"
                                else -> "🐾"
                            },
                            name = negocio.nombre,
                            category = negocio.categoria,
                            distance = distanciaDesdeDireccion(
                                direccion = direccionActiva,
                                negocio = negocio
                            ),
                            rating = negocio.calificacion.toString(),
                            onClick = {
                                onBusinessSelected(negocio.id)
                            }
                        )
                    }
                }
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
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
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
                    text = "⭐ $rating",
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


/**
 * Calcula la distancia en línea recta
 * desde la dirección activa hasta un negocio.
 */
private fun distanciaDesdeDireccion(
    direccion: UserAddress?,
    negocio: Business
): String {

    if (direccion == null) {
        return "Sin ubicación"
    }

    if (
        !negocio.latitud.isFinite() ||
        !negocio.longitud.isFinite() ||
        negocio.latitud !in -90.0..90.0 ||
        negocio.longitud !in -180.0..180.0 ||
        (negocio.latitud == 0.0 && negocio.longitud == 0.0)
    ) {
        return "Sin distancia"
    }

    val resultado = FloatArray(1)

    Location.distanceBetween(
        direccion.latitud,
        direccion.longitud,
        negocio.latitud,
        negocio.longitud,
        resultado
    )

    val metros = resultado[0]

    return if (metros < 1000f) {
        "${metros.toInt()} m"
    } else {
        String.format(
            Locale("es", "CO"),
            "%.1f km",
            metros / 1000f
        )
    }
}
