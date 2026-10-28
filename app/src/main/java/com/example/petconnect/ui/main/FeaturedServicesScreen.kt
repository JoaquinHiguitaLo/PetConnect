
package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petconnect.data.model.Business
import com.example.petconnect.data.model.UserAddress
import com.example.petconnect.viewmodel.BusinessViewModel
import android.location.Location
import java.util.Locale

private val VerdeDestacados = Color(0xFF227A76)
private val FondoDestacados = Color(0xFFF9F8F4)

@Composable
fun FeaturedServicesScreen(
    direccionActiva: UserAddress?,
    onBack: () -> Unit,
    onBusinessSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    businessViewModel: BusinessViewModel = viewModel(
        key = "featured_services"
    )
) {
    val uiState by businessViewModel.uiState.collectAsState()

    LaunchedEffect(businessViewModel) {
        businessViewModel.cargarNegociosAprobados()
    }

    val negocios = uiState.businesses
        .filter { it.estadoVerificacion == "APROBADO" }
        .sortedByDescending { it.calificacion }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoDestacados)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    VerdeDestacados,
                    RoundedCornerShape(
                        bottomStart = 22.dp,
                        bottomEnd = 22.dp
                    )
                )
                .padding(18.dp)
        ) {

            IconButton(onClick = onBack) {
                Icon(
                    imageVector =
                        Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.White
                )
            }

            Text(
                text = "Servicios destacados",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Establecimientos para el cuidado de tus mascotas",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.85f)
            )
        }

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = VerdeDestacados
                    )
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se pudieron cargar los servicios.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            negocios.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aún no hay servicios destacados.",
                        color = Color.Gray
                    )
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = negocios,
                        key = { it.id }
                    ) { negocio ->

                        FeaturedBusinessCard(
                            negocio = negocio,
                            distancia = distanciaDestacado(
                                direccionActiva,
                                negocio
                            ),
                            onClick = {
                                onBusinessSelected(negocio.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturedBusinessCard(
    negocio: Business,
    distancia: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
                    .background(Color(0xFFB6E5DF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (
                        negocio.categoria.trim().lowercase()
                    ) {
                        "veterinaria", "veterinarias" -> "🏥"
                        "peluquería", "peluqueria", "baño" -> "✂️"
                        "paseador", "paseadores" -> "🐕"
                        "guardería", "guarderia" -> "🏨"
                        else -> "🐾"
                    },
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Text(
                    text = negocio.nombre,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${negocio.categoria} · $distancia",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF777777),
                    maxLines = 2
                )

                Text(
                    text = "★ ${negocio.calificacion}",
                    color = Color(0xFFD58A00),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun distanciaDestacado(
    direccion: UserAddress?,
    negocio: Business
): String {

    if (direccion == null) return "Sin ubicación"

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
