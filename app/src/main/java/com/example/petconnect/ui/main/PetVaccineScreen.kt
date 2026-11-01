
package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petconnect.data.model.Pet
import com.example.petconnect.data.model.PetVaccine
import com.example.petconnect.viewmodel.PetVaccineViewModel
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material.icons.filled.Close
import java.text.Normalizer
import java.util.Locale
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import com.example.petconnect.ui.components.PetConnectTopBar

private fun normalizarBusquedaVacuna(texto: String): String {
    return Normalizer.normalize(
        texto.trim().lowercase(Locale.ROOT),
        Normalizer.Form.NFD
    ).replace(Regex("\\p{M}+"), "")
}

private val VerdeCarnet = Color(0xFF227C78)
private val FondoCarnet = Color(0xFFFAF9F6)

@Composable
fun PetVaccineScreen(
    mascota: Pet,
    onBack: () -> Unit,
    onAddVaccine: () -> Unit,
    onEditVaccine: (PetVaccine) -> Unit = {},
    modifier: Modifier = Modifier,
    vaccineViewModel: PetVaccineViewModel = viewModel()
) {
    val uiState by vaccineViewModel.uiState.collectAsState()


    var buscadorVisible by remember {
        mutableStateOf(false)
    }

    var textoBusqueda by remember {
        mutableStateOf("")
    }

    val vacunasFiltradas = remember(
        uiState.vacunas,
        textoBusqueda
    ) {
        val consulta = normalizarBusquedaVacuna(textoBusqueda)

        if (consulta.isBlank()) {
            uiState.vacunas
        } else {
            uiState.vacunas.filter { vacuna ->
                normalizarBusquedaVacuna(vacuna.nombreVacuna)
                    .contains(consulta) ||
                        normalizarBusquedaVacuna(vacuna.veterinaria)
                            .contains(consulta)
            }
        }
    }


    // Consultamos únicamente las vacunas de esta mascota.
    LaunchedEffect(mascota.id) {
        vaccineViewModel.cargarVacunas(mascota.id)
    }

    Scaffold(
        modifier = modifier,
        containerColor = FondoCarnet,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // Navegación común de PetConnect
            PetConnectTopBar(
                onBack = onBack
            )

            // Encabezado propio del carnet
            VaccineHeader(
                nombreMascota = mascota.nombre,
                cantidadVacunas = uiState.vacunas.size,
                onAddVaccine = onAddVaccine,
                onSearchClick = {
                    buscadorVisible = !buscadorVisible

                    if (!buscadorVisible) {
                        textoBusqueda = ""
                    }
                },
                buscadorVisible = buscadorVisible
            )

            if (buscadorVisible) {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 8.dp
                        ),
                    placeholder = {
                        Text("Buscar por vacuna o veterinaria...")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
            }

            // ============================================
            // CONTENIDO
            // ============================================

            when {
                uiState.mascotaId != mascota.id ||
                        uiState.isLoading -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = VerdeCarnet
                        )
                    }
                }

                uiState.errorMessage != null -> {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.errorMessage
                                    ?: "Error al cargar las vacunas.",
                                color = MaterialTheme.colorScheme.error
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            TextButton(
                                onClick = {
                                    vaccineViewModel.cargarVacunas(
                                        mascota.id
                                    )
                                }
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                uiState.vacunas.isEmpty() -> {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment =
                                Alignment.CenterHorizontally,
                            verticalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Vaccines,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = VerdeCarnet
                            )

                            Text(
                                text = "Aún no hay vacunas registradas",
                                style =
                                    MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Agrega las vacunas de ${mascota.nombre} para comenzar su carnet.",
                                style =
                                    MaterialTheme.typography.bodyMedium,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                else -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {


                        items(
                            vacunasFiltradas,
                            key = { it.id }
                        ) { vacuna ->


                            VaccineRecordCard(
                                vacuna = vacuna,
                                onEdit = {
                                    onEditVaccine(vacuna)
                                }
                            )

                        }

                        item {
                            Spacer(modifier = Modifier.height(70.dp))
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun VaccineRecordCard(
    vacuna: PetVaccine,
    onEdit: () -> Unit
) {
    Card(
        onClick = onEdit,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Vaccines,
                    contentDescription = null,
                    tint = VerdeCarnet
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = vacuna.nombreVacuna,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Aplicada: ${vacuna.fechaAplicacion}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Próxima: ${vacuna.proximaDosis?.takeIf { it.isNotBlank() } ?: "Sin programar"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF227C78)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF88958E)
                )
            }

        }
    }
}

@Composable
private fun VaccineHeader(
    nombreMascota: String,
    cantidadVacunas: Int,
    onAddVaccine: () -> Unit,
    onSearchClick: () -> Unit,
    buscadorVisible: Boolean
) {
    val verde = Color(0xFF227C78)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFAF9F6))
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 12.dp
            )
    ) {

        Text(
            text = "Carnet de vacunación",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = verde
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = nombreMascota,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color(0xFFB9E7E4)
            ) {
                Text(
                    text = if (cantidadVacunas == 1) {
                        "1 vacuna"
                    } else {
                        "$cantidadVacunas vacunas"
                    },
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 9.dp
                    ),
                    color = verde,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = if (buscadorVisible) {
                            Icons.Default.Close
                        } else {
                            Icons.Default.Search
                        },
                        contentDescription = if (buscadorVisible) {
                            "Cerrar búsqueda"
                        } else {
                            "Buscar vacunas"
                        },
                        tint = verde
                    )
                }

                FilledIconButton(
                    onClick = onAddVaccine,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = verde,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar vacuna"
                    )
                }
            }
        }
    }
}
