
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

private val VerdeCarnet = Color(0xFF227C78)
private val FondoCarnet = Color(0xFFFAF9F6)

@Composable
fun PetVaccineScreen(
    mascota: Pet,
    onBack: () -> Unit,
    onAddVaccine: () -> Unit,
    modifier: Modifier = Modifier,
    vaccineViewModel: PetVaccineViewModel = viewModel()
) {
    val uiState by vaccineViewModel.uiState.collectAsState()

    // Vacuna que el usuario desea eliminar.
    var vacunaPorEliminar by remember {
        mutableStateOf<PetVaccine?>(null)
    }

    // Consultamos únicamente las vacunas de esta mascota.
    LaunchedEffect(mascota.id) {
        vaccineViewModel.cargarVacunas(mascota.id)
    }

    Scaffold(
        modifier = modifier,
        containerColor = FondoCarnet,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {

        FloatingActionButton(
                onClick = onAddVaccine,
                containerColor = VerdeCarnet,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar vacuna"
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // ============================================
            // ENCABEZADO
            // ============================================

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = VerdeCarnet,
                        shape = RoundedCornerShape(
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        )
                    )
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 12.dp,
                        bottom = 24.dp
                    )
            ) {

                IconButton(onClick = onBack) {
                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "CARNET DE VACUNACIÓN",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = mascota.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Historial de vacunas y próximas dosis",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
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

                        item {
                            Text(
                                text = "${uiState.vacunas.size} vacunas registradas",
                                style =
                                    MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(
                            items = uiState.vacunas,
                            key = { it.id }
                        ) { vacuna ->


                            VaccineRecordCard(
                                vacuna = vacuna,
                                onEdit = {
                                    // Conectaremos la edición después.
                                },
                                onDelete = {
                                    vacunaPorEliminar = vacuna
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

    vacunaPorEliminar?.let { vacuna ->

        AlertDialog(
            onDismissRequest = {
                if (!uiState.isSaving) {
                    vacunaPorEliminar = null
                }
            },

            title = {
                Text("Eliminar vacuna")
            },


            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Text(
                        text = "Estás a punto de eliminar esta vacuna del carnet de ${mascota.nombre}.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Vaccines,
                                contentDescription = null,
                                tint = Color(0xFF227C78)
                            )

                            Text(
                                text = vacuna.nombreVacuna,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    Text(
                        text = "Esta acción no se puede deshacer.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Medium
                    )
                }
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        vaccineViewModel.eliminarVacuna(vacuna.id)
                        vacunaPorEliminar = null
                    },
                    enabled = !uiState.isSaving
                ) {
                    Text(
                        text = "Eliminar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        vacunaPorEliminar = null
                    },
                    enabled = !uiState.isSaving
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

}

@Composable
private fun VaccineRecordCard(
    vacuna: PetVaccine,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Vaccines,
                    contentDescription = null,
                    tint = VerdeCarnet
                )

                Text(
                    text = vacuna.nombreVacuna,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider()

            Text(
                text = "Aplicación: ${vacuna.fechaAplicacion}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Próxima dosis: ${vacuna.proximaDosis ?: "No registrada"}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (vacuna.veterinaria.isNotBlank()) {
                Text(
                    text = "Veterinaria: ${vacuna.veterinaria}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (vacuna.observaciones.isNotBlank()) {
                Text(
                    text = vacuna.observaciones,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text("Editar")
                }

                TextButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Eliminar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

        }
    }
}
