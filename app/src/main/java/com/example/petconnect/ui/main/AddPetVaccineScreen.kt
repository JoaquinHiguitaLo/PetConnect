
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

@Composable
fun AddPetVaccineScreen(
    mascota: Pet,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    vaccineViewModel: PetVaccineViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by vaccineViewModel.uiState.collectAsState()

    var nombreVacuna by remember { mutableStateOf("") }
    var fechaAplicacion by remember { mutableStateOf("") }
    var proximaDosis by remember { mutableStateOf("") }
    var veterinaria by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var guardando by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSaving, uiState.operationSuccess) {
        if (guardando && uiState.operationSuccess && !uiState.isSaving) {
            guardando = false
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F6))
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFF227C78),
                    RoundedCornerShape(
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp
                    )
                )
                .padding(20.dp)
        ) {
            TextButton(onClick = onBack) {
                Text("← Regresar", color = Color.White)
            }

            Text(
                text = "Registrar vacuna",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Carnet de ${mascota.nombre}",
                color = Color.White.copy(alpha = 0.85f)
            )
        }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = nombreVacuna,
                onValueChange = { nombreVacuna = it },
                label = { Text("Nombre de la vacuna *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedButton(
                onClick = {
                    abrirCalendario(fechaAplicacion) {
                        fechaAplicacion = it
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (fechaAplicacion.isBlank())
                        "Seleccionar fecha de aplicación *"
                    else
                        "Aplicación: $fechaAplicacion"
                )
            }

            OutlinedButton(
                onClick = {
                    abrirCalendario(proximaDosis) {
                        proximaDosis = it
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (proximaDosis.isBlank())
                        "Seleccionar próxima dosis (opcional)"
                    else
                        "Próxima dosis: $proximaDosis"
                )
            }

            if (proximaDosis.isNotBlank()) {
                TextButton(
                    onClick = { proximaDosis = "" }
                ) {
                    Text("Quitar próxima dosis")
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

            OutlinedTextField(
                value = veterinaria,
                onValueChange = { veterinaria = it },
                label = { Text("Veterinaria (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = { Text("Observaciones (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            uiState.errorMessage?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    guardando = true

                    vaccineViewModel.registrarVacuna(
                        PetVaccine(
                            mascotaId = mascota.id,
                            nombreVacuna = nombreVacuna.trim(),
                            fechaAplicacion = fechaAplicacion,
                            proximaDosis = proximaDosis.ifBlank { null },
                            veterinaria = veterinaria.trim(),
                            observaciones = observaciones.trim()
                        )
                    )
                },
                enabled = formularioValido && !uiState.isSaving,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF227C78)
                )
            ) {
                Text(
                    if (uiState.isSaving)
                        "Guardando..."
                    else
                        "Guardar vacuna"
                )
            }
        }
    }
}
