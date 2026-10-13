
package com.example.petconnect.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petconnect.R
import com.example.petconnect.data.model.petConnectCategories
import com.example.petconnect.viewmodel.BusinessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessDetailScreen(
    businessId: String,
    onBack: () -> Unit,
    viewModel: BusinessViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(businessId) {
        viewModel.cargarNegocioPorId(businessId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del negocio") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage
                            ?: "No se pudo cargar el negocio.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            uiState.selectedBusiness == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontró la información del negocio.")
                }
            }

            else -> {
                val business = uiState.selectedBusiness!!

                val categoryName = petConnectCategories
                    .find { it.id == business.categoria }
                    ?.nombre ?: business.categoria

                // Imagen ilustrativa según la categoría.
                val imageRes = when (business.categoria) {
                    "veterinaria" -> R.drawable.vetvida
                    "peluqueria" -> R.drawable.peluquerianegocio
                    "bano" -> R.drawable.banonegocio
                    "guarderia" -> R.drawable.guarderianegocio
                    "paseadores" -> R.drawable.paseadores
                    "adiestramiento" -> R.drawable.adiestramiento
                    "tiendas" -> R.drawable.tiendanegocio
                    "parques" -> R.drawable.parque
                    else -> R.drawable.vetvida
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Imagen destacada del negocio.
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Imagen de $categoryName",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // Nombre, categoría y calificación.
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = business.nombre,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = categoryName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Calificación",
                                tint = MaterialTheme.colorScheme.tertiary
                            )

                            Text(
                                text = if (business.calificacion > 0) {
                                    "%.1f".format(business.calificacion)
                                } else {
                                    "Sin calificaciones"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    HorizontalDivider()

                    // Descripción.
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Sobre este negocio",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = business.descripcion.ifBlank {
                                "No hay una descripción disponible."
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Información de contacto.
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            Text(
                                text = "Información de contacto",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            ContactItem(
                                icon = {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null
                                    )
                                },
                                title = "Dirección",
                                value = business.direccion.ifBlank {
                                    "Dirección no registrada"
                                }
                            )

                            ContactItem(
                                icon = {
                                    Icon(
                                        Icons.Default.Phone,
                                        contentDescription = null
                                    )
                                },
                                title = "Teléfono",
                                value = business.telefono.ifBlank {
                                    "Teléfono no registrado"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactItem(
    icon: @Composable () -> Unit,
    title: String,
    value: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
