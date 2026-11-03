
package com.example.petconnect.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.example.petconnect.ui.components.PetConnectTopBar
import com.example.petconnect.ui.components.PetConnectFormTitle
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.clickable

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

        PetConnectFormTitle(
            title = "Detalle del negocio"
        )

        Spacer(modifier = Modifier.height(16.dp))


        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
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
                        .fillMaxSize(),
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
                    "peluqueria" -> R.drawable.vetvida
                    "bano" -> R.drawable.banonegocio
                    "guarderia" -> R.drawable.guarderianegocio
                    "paseadores" -> R.drawable.vetvida
                    "adiestramiento" -> R.drawable.vetvida
                    "tiendas" -> R.drawable.tiendanegocio
                    "parques" -> R.drawable.vetvida
                    else -> R.drawable.vetvida
                }


                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = 24.dp,
                            end = 24.dp,
                            bottom = 24.dp
                        ),
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // NOMBRE DEL NEGOCIO Y FAVORITO
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Text(
                                text = business.nombre,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF303534)
                            )

                            // Estado visual temporal.
                            var esFavorito by remember(business.id) {
                                mutableStateOf(false)
                            }

                            IconButton(
                                onClick = {
                                    esFavorito = !esFavorito
                                },
                                modifier = Modifier.size(44.dp)
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = if (esFavorito) {
                                            R.drawable.icon_fav_activ
                                        } else {
                                            R.drawable.icon_fav_desac
                                        }
                                    ),
                                    contentDescription = if (esFavorito) {
                                        "Quitar de favoritos"
                                    } else {
                                        "Agregar a favoritos"
                                    },
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        // CATEGORÍA Y CALIFICACIÓN
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = Color(0xFFB9E7E4)
                            ) {
                                Text(
                                    text = categoryName,
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF227C78)
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFD58A00),
                                    modifier = Modifier.size(20.dp)
                                )

                                Text(
                                    text = if (business.calificacion > 0) {
                                        "%.1f".format(business.calificacion)
                                    } else {
                                        "Sin calificaciones"
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF303534)
                                )
                            }
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
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Información de contacto",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF303534)
                            )

                            ContactItem(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Color(0xFF227C78),
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                title = "Dirección",
                                value = business.direccion.ifBlank {
                                    "Dirección no registrada"
                                }
                            )

                            HorizontalDivider(
                                color = Color(0xFFE5E9E7)
                            )

                            ContactItem(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = Color(0xFF227C78),
                                        modifier = Modifier.size(22.dp)
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    color = Color(0xFFE3F2EF),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF303534)
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF71817E)
            )
        }
    }
}

