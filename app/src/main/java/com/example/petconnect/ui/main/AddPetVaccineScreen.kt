
package com.example.petconnect.ui.main

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petconnect.data.model.Pet
import com.example.petconnect.data.model.PetVaccine
import com.example.petconnect.viewmodel.PetVaccineViewModel
import java.time.LocalDate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import com.example.petconnect.ui.components.PetConnectTopBar

@Composable
fun AddPetVaccineScreen(
    mascota: Pet,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    vacunaEditar: PetVaccine? = null,
    vaccineViewModel: PetVaccineViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by vaccineViewModel.uiState.collectAsState()


    val esEdicion = vacunaEditar != null

    var mostrarConfirmacionEliminar by remember(vacunaEditar?.id) {
        mutableStateOf(false)
    }

    var eliminando by remember(vacunaEditar?.id) {
        mutableStateOf(false)
    }

    var nombreVacuna by remember(vacunaEditar?.id) {
        mutableStateOf(vacunaEditar?.nombreVacuna ?: "")
    }

    var fechaAplicacion by remember(vacunaEditar?.id) {
        mutableStateOf(vacunaEditar?.fechaAplicacion ?: "")
    }

    var proximaDosis by remember(vacunaEditar?.id) {
        mutableStateOf(vacunaEditar?.proximaDosis ?: "")
    }

    var veterinaria by remember(vacunaEditar?.id) {
        mutableStateOf(vacunaEditar?.veterinaria ?: "")
    }

    var observaciones by remember(vacunaEditar?.id) {
        mutableStateOf(vacunaEditar?.observaciones ?: "")
    }

    var guardando by remember(vacunaEditar?.id) {
        mutableStateOf(false)
    }


    LaunchedEffect(uiState.isSaving, uiState.operationSuccess) {
        if (
            (guardando || eliminando) &&
            uiState.operationSuccess &&
            !uiState.isSaving
        ) {
            guardando = false
            eliminando = false

            vaccineViewModel.limpiarResultadoOperacion()
            onSaved()
        }
    }

    fun abrirCalendario(
        fechaActual: String,
        alSeleccionar: (String) -> Unit
    ) {
        val fecha = runCatching {
            LocalDate.parse(fechaActual)
        }.getOrDefault(LocalDate.now())

        DatePickerDialog(
            context,
            { _, anio, mes, dia ->
                alSeleccionar(
                    LocalDate.of(anio, mes + 1, dia).toString()
                )
            },
            fecha.year,
            fecha.monthValue - 1,
            fecha.dayOfMonth
        ).show()
    }

    val formularioValido =
        nombreVacuna.isNotBlank() &&
                fechaAplicacion.isNotBlank() &&
                (proximaDosis.isBlank() || proximaDosis >= fechaAplicacion)

    val formaCampo = RoundedCornerShape(14.dp)
    val colorBorde = Color(0xFFD5DAD4)
    val verdeFormulario = Color(0xFF227C78)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F6))
            .verticalScroll(rememberScrollState())
    ) {
        PetConnectTopBar(
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 8.dp,
                    bottom = 12.dp
                )
        ) {


            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (esEdicion) {
                    "Editar vacuna"
                } else {
                    "Registrar vacuna"
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF227C78)
            )
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 28.dp,
                    end = 28.dp,
                    top = 16.dp,
                    bottom = 32.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    text = "Nombre de la vacuna *",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF475951),
                    fontWeight = FontWeight.Medium
                )

                OutlinedTextField(
                    value = nombreVacuna,
                    onValueChange = { nombreVacuna = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = {
                        Text("Ej. Triple felina")
                    },
                    shape = formaCampo,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = verdeFormulario,
                        unfocusedBorderColor = colorBorde
                    )
                )
            }

            CampoFechaVacuna(
                etiqueta = "Fecha de aplicación *",
                fecha = fechaAplicacion,
                placeholder = "Seleccionar fecha",
                onClick = {
                    abrirCalendario(fechaAplicacion) {
                        fechaAplicacion = it
                    }
                }
            )

            CampoFechaVacuna(
                etiqueta = "Próxima dosis (opcional)",
                fecha = proximaDosis,
                placeholder = "Seleccionar próxima dosis",
                onClick = {
                    abrirCalendario(proximaDosis) {
                        proximaDosis = it
                    }
                }
            )


            if (proximaDosis.isNotBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { proximaDosis = "" }
                    ) {
                        Text(
                            text = "Quitar fecha",
                            color = Color(0xFF227C78),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }


            if (
                proximaDosis.isNotBlank() &&
                fechaAplicacion.isNotBlank() &&
                proximaDosis < fechaAplicacion
            ) {
                Text(
                    "La próxima dosis no puede ser anterior a la aplicación.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    text = "Veterinaria (opcional)",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF475951),
                    fontWeight = FontWeight.Medium
                )

                OutlinedTextField(
                    value = veterinaria,
                    onValueChange = { veterinaria = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Nombre de la veterinaria")
                    },
                    singleLine = true,
                    shape = formaCampo,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = verdeFormulario,
                        unfocusedBorderColor = colorBorde
                    )
                )
            }



            Column(
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    text = "Observaciones (opcional)",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF475951),
                    fontWeight = FontWeight.Medium
                )

                OutlinedTextField(
                    value = observaciones,
                    onValueChange = { observaciones = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Ej. Reacciones, recomendaciones o notas")
                    },
                    minLines = 3,
                    shape = formaCampo,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = verdeFormulario,
                        unfocusedBorderColor = colorBorde
                    )
                )
            }


            uiState.errorMessage?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    if (!guardando && !uiState.isSaving) {

                        val vacuna = PetVaccine(
                            id = vacunaEditar?.id ?: "",
                            usuarioId = vacunaEditar?.usuarioId ?: "",
                            mascotaId = mascota.id,
                            nombreVacuna = nombreVacuna.trim(),
                            fechaAplicacion = fechaAplicacion,
                            proximaDosis = proximaDosis.ifBlank { null },
                            veterinaria = veterinaria.trim(),
                            observaciones = observaciones.trim()
                        )

                        guardando = true

                        if (esEdicion) {
                            vaccineViewModel.actualizarVacuna(vacuna)
                        } else {
                            vaccineViewModel.registrarVacuna(vacuna)
                        }
                    }
                },

                enabled = formularioValido && !uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(48.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF227C78)
                )
            ) {
                Text(
                    text = when {
                        uiState.isSaving -> "Guardando..."
                        esEdicion -> "Guardar cambios"
                        else -> "Guardar vacuna"
                    }
                )
            }
            if (esEdicion) {

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = {
                        mostrarConfirmacionEliminar = true
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enabled = !uiState.isSaving
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Eliminar vacuna",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        if (mostrarConfirmacionEliminar && vacunaEditar != null) {

            AlertDialog(
                onDismissRequest = {
                    if (!uiState.isSaving) {
                        mostrarConfirmacionEliminar = false
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
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = vacunaEditar.nombreVacuna,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Text(
                            text = "Esta acción no se puede deshacer.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },

                confirmButton = {
                    TextButton(
                        onClick = {
                            eliminando = true
                            vaccineViewModel.eliminarVacuna(vacunaEditar.id)
                            mostrarConfirmacionEliminar = false
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
                            mostrarConfirmacionEliminar = false
                        },
                        enabled = !uiState.isSaving
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
private fun CampoFechaVacuna(
    etiqueta: String,
    fecha: String,
    placeholder: String,
    onClick: () -> Unit
) {
    val verde = Color(0xFF227C78)

    Column(
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF475951)
        )

        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color(0xFFD5DAD4)
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = verde,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = fecha.ifBlank { placeholder },
                    modifier = Modifier.weight(1f),
                    color = if (fecha.isBlank()) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
