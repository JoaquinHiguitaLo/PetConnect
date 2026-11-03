package com.example.petconnect.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petconnect.data.model.Business
import com.example.petconnect.data.model.petConnectCategories
import com.example.petconnect.viewmodel.BusinessViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.petconnect.R
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import com.example.petconnect.ui.components.PetConnectTopBar
@Composable
fun ServicesByCategoryScreen(
    categoryId: String,
    onBack: () -> Unit,
    onBusinessSelected: (String) -> Unit,
    viewModel: BusinessViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val category = petConnectCategories.find {
        it.id == categoryId
    }

    LaunchedEffect(categoryId) {
        viewModel.cargarNegociosPorCategoria(categoryId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F6))
            .statusBarsPadding()
    ) {

        PetConnectTopBar(
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = category?.nombre ?: "Servicios",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF227C78),
                modifier = Modifier.weight(1f)
            )

            if (!uiState.isLoading && uiState.errorMessage == null) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFB9E7E4)
                ) {
                    Text(
                        text = "${uiState.businesses.size} encontrados",
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ),
                        color = Color(0xFF227C78),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Buscando negocios...")
                }
            }

            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage
                        ?: "No se pudieron cargar los negocios.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            uiState.businesses.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🐾",
                        style = MaterialTheme.typography.displaySmall
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Todavía no hay negocios disponibles",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Pronto encontrarás opciones en esta categoría.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }


            else -> {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 28.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = uiState.businesses,
                        key = { it.id }
                    ) { business ->

                        BusinessCard(
                            business = business,
                            onClick = {
                                onBusinessSelected(business.id)
                            }
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun BusinessCard(
    business: Business,
    onClick: () -> Unit
) {
    // Fotografía específica del negocio o ilustración de respaldo.
    val imageRes = when {
        business.nombre.contains("VetVida", ignoreCase = true) ->
            R.drawable.vetvida

        business.nombre.contains("Terranova", ignoreCase = true) ->
            R.drawable.banonegocio

        business.nombre.contains("Cuidador JM", ignoreCase = true) ->
            R.drawable.guarderianegocio

        business.nombre.contains("peluquer", ignoreCase = true) ->
            R.drawable.vetvida

        business.nombre.contains("tienda", ignoreCase = true) ->
            R.drawable.tiendanegocio

        business.nombre.contains("baño", ignoreCase = true) ||
                business.nombre.contains("bano", ignoreCase = true) ->
            R.drawable.banonegocio

        business.nombre.contains("guarder", ignoreCase = true) ->
            R.drawable.guarderianegocio

        else -> when (business.categoria) {
            "veterinaria" -> R.drawable.cat_hospital
            "peluqueria" -> R.drawable.cat_peluqueria
            "bano" -> R.drawable.cat_peluqueria
            "guarderia" -> R.drawable.cat_guarderia
            "paseadores" -> R.drawable.cat_paseadores
            "adiestramiento" -> R.drawable.cat_guarderia
            "tiendas" -> R.drawable.cat_tienda
            "parques" -> R.drawable.cat_parques
            else -> R.drawable.icon_ubicacion
        }
    }

    val categoryName = petConnectCategories
        .find { it.id == business.categoria }
        ?.nombre ?: business.categoria

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column {
            // Fotografía principal del negocio.
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Imagen de ${business.nombre}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    ),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Nombre y acceso al detalle.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = business.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = categoryName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                }

                // Calificación.
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Calificación",
                        tint = androidx.compose.ui.graphics.Color(
                            0xFFC9871F
                        ),
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = if (business.calificacion > 0.0) {
                            "%.1f".format(business.calificacion)
                        } else {
                            "Sin calificaciones"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Dirección.
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Dirección",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = business.direccion.ifBlank {
                            "Dirección no disponible"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }

                // Descripción.
                if (business.descripcion.isNotBlank()) {
                    Text(
                        text = business.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

