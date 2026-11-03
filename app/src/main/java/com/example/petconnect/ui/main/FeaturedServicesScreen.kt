
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
import java.text.Normalizer
import com.example.petconnect.ui.components.PetConnectTopBar
import com.example.petconnect.ui.components.PetConnectFormTitle

import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.shape.CircleShape

private val VerdeDestacados = Color(0xFF227C78)
private val FondoDestacados = Color(0xFFFAF9F6)
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

    var categoriaSeleccionada by remember {
        mutableStateOf("Todos")
    }

    var ordenSeleccionado by remember {
        mutableStateOf("Calificación")
    }

    var textoBusqueda by remember {
        mutableStateOf("")
    }

    LaunchedEffect(businessViewModel) {
        businessViewModel.cargarNegociosAprobados()
    }



    val negociosFiltrados = uiState.businesses
        .filter { negocio ->

            // Solo establecimientos aprobados.
            if (negocio.estadoVerificacion != "APROBADO") {
                return@filter false
            }

            val categoria = negocio.categoria
                .trim()
                .lowercase()

            // Primero comprobamos la categoría seleccionada.
            val coincideCategoria = when (categoriaSeleccionada) {
                "Veterinarias" ->
                    categoria == "veterinaria" ||
                            categoria == "veterinarias"

                "Peluquerías" ->
                    categoria == "peluquería" ||
                            categoria == "peluqueria" ||
                            categoria == "baño"

                "Guarderías" ->
                    categoria == "guardería" ||
                            categoria == "guarderia"

                "Paseadores" ->
                    categoria == "paseador" ||
                            categoria == "paseadores"

                else -> true
            }

            // Después comprobamos el texto escrito.
            val busqueda = normalizarBusqueda(textoBusqueda)

            val coincideBusqueda =
                busqueda.isBlank() ||
                        normalizarBusqueda(negocio.nombre).contains(busqueda) ||
                        normalizarBusqueda(negocio.categoria).contains(busqueda)

            coincideCategoria && coincideBusqueda
        }



    val negocios = when (ordenSeleccionado) {

        "Cercanía" -> negociosFiltrados.sortedBy { negocio ->

            if (
                direccionActiva == null ||
                !coordenadasValidas(
                    direccionActiva.latitud,
                    direccionActiva.longitud
                ) ||
                !coordenadasValidas(
                    negocio.latitud,
                    negocio.longitud
                )
            ) {
                Float.MAX_VALUE
            } else {
                val resultado = FloatArray(1)

                Location.distanceBetween(
                    direccionActiva.latitud,
                    direccionActiva.longitud,
                    negocio.latitud,
                    negocio.longitud,
                    resultado
                )

                resultado[0]
            }
        }

        else -> negociosFiltrados.sortedByDescending {
            it.calificacion
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoDestacados)
    ) {


        PetConnectTopBar(
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(8.dp))

        PetConnectFormTitle(
            title = "Servicios destacados"
        )

        Spacer(modifier = Modifier.height(16.dp))



        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {


            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Buscar establecimientos...",
                        color = Color(0xFF8A9995)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = VerdeDestacados,
                        modifier = Modifier.size(22.dp)
                    )
                },
                trailingIcon = {
                    if (textoBusqueda.isNotEmpty()) {
                        IconButton(
                            onClick = { textoBusqueda = "" }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar búsqueda",
                                tint = VerdeDestacados
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdeDestacados,
                    unfocusedBorderColor = Color(0xFFD9DEDC),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )


            val categorias = listOf(
                "Todos",
                "Veterinarias",
                "Peluquerías",
                "Guarderías",
                "Paseadores"
            )

            androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categorias.size) { index ->

                    val categoria = categorias[index]

                    FilterChip(
                        selected = categoriaSeleccionada == categoria,
                        onClick = {
                            categoriaSeleccionada = categoria
                        },
                        label = {
                            Text(categoria)
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color(0xFF303534),
                            selectedContainerColor = VerdeDestacados,
                            selectedLabelColor = Color.White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = categoriaSeleccionada == categoria,
                            borderColor = Color(0xFFD9DEDC),
                            selectedBorderColor = VerdeDestacados
                        )
                    )

                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                Surface(
                    color = Color(0xFFB9E7E4),
                    shape = CircleShape
                ) {
                    Text(
                        text = if (negocios.size == 1) {
                            "1 servicio"
                        } else {
                            "${negocios.size} servicios"
                        },
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 7.dp
                        ),
                        color = VerdeDestacados,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }


                var menuAbierto by remember {
                    mutableStateOf(false)
                }

                Box {

                    TextButton(
                        onClick = { menuAbierto = true }
                    ) {
                        Text(
                            text = ordenSeleccionado
                        )
                        Text(" ▾")
                    }

                    DropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = {
                            menuAbierto = false
                        }
                    ) {

                        listOf(
                            "Calificación",
                            "Cercanía"
                        ).forEach { opcion ->

                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    ordenSeleccionado = opcion
                                    menuAbierto = false
                                }
                            )
                        }
                    }
                }
            }
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
                        text = if (textoBusqueda.isNotBlank()) {
                            "No encontramos establecimientos para \"$textoBusqueda\"."
                        } else if (categoriaSeleccionada != "Todos") {
                            "No hay servicios disponibles en esta categoría."
                        } else {
                            "Aún no hay servicios destacados."
                        },
                        color = Color.Gray,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }


            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 24.dp,
                            end = 24.dp,
                            top = 12.dp,
                            bottom = 24.dp
                        ),
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
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
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
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = negocio.nombre,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    minLines = 2,
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
        !coordenadasValidas(direccion.latitud, direccion.longitud) ||
        !coordenadasValidas(negocio.latitud, negocio.longitud)
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

private fun normalizarBusqueda(texto: String): String {
    val textoSinTildes = Normalizer.normalize(
        texto.trim().lowercase(),
        Normalizer.Form.NFD
    )

    return textoSinTildes
        .replace(Regex("\\p{M}+"), "")
}


private fun coordenadasValidas(
    latitud: Double,
    longitud: Double
): Boolean {
    return latitud.isFinite() &&
            longitud.isFinite() &&
            latitud in -90.0..90.0 &&
            longitud in -180.0..180.0 &&
            !(latitud == 0.0 && longitud == 0.0)
}
