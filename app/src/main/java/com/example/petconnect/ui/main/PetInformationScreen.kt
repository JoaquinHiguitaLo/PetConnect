
package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petconnect.data.model.Pet
import androidx.compose.runtime.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

@Composable
fun PetInformationScreen(
    mascota: Pet,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val verde = Color(0xFF227C78)
    var mostrarConfirmacion by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F6))
            .verticalScroll(rememberScrollState())
    ) {

        // Misma cabecera del perfil individual.
        PetProfileHeader(
            mascota = mascota,
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Solo cambia el contenido debajo de la foto.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Text(
                        text = "Datos de la mascota",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )


                    val datos = listOf(
                        "Especie" to mascota.especie,
                        "Raza" to (mascota.raza?.ifBlank { null } ?: "No registrada"),
                        "Fecha de nacimiento" to (
                                mascota.fechaNacimiento
                                    ?.takeIf { it.isNotBlank() }
                                    ?: "No registrada"
                                ),
                        "Edad" to "${mascota.edad} años",
                        "Tamaño" to mascota.tamano,
                        "Sexo" to (
                                mascota.sexo
                                    ?.takeIf { it.isNotBlank() }
                                    ?: "No especificado"
                                ),
                        "Peso" to (
                                mascota.pesoKg?.let {
                                    "${java.text.DecimalFormat("0.##").format(it)} kg"
                                } ?: "No registrado"
                                )
                    )

                    datos.chunked(2).forEach { fila ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            fila.forEach { (etiqueta, valor) ->
                                Box(modifier = Modifier.weight(1f)) {
                                    PetInfoDato(
                                        etiqueta = etiqueta,
                                        valor = valor
                                    )
                                }
                            }

                            if (fila.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                }
            }

            Button(
                onClick = onEdit,
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(48.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde
                )
            ) {
                Text("Editar información")
            }

            HorizontalDivider(
                color = Color(0xFFE8E8E3)
            )

            TextButton(
                onClick = {
                    mostrarConfirmacion = true
                },
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            ) {
                Text(
                    text = "Eliminar perfil de mascota",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        if (mostrarConfirmacion) {
            AlertDialog(
                onDismissRequest = {
                    mostrarConfirmacion = false
                },
                title = {
                    Text("¿Eliminar a ${mascota.nombre}?")
                },
                text = {
                    Text(
                        "Esta acción es permanente. Se eliminarán " +
                                "el perfil de ${mascota.nombre} y sus " +
                                "vacunas registradas."
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            mostrarConfirmacion = false
                            onDelete()
                        }
                    ) {
                        Text(
                            "Eliminar",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            mostrarConfirmacion = false
                        }
                    ) {
                        Text("Cancelar")
                    }
                },
                containerColor = Color.White
            )
        }

    }
}

@Composable
private fun PetInfoDato(
    etiqueta: String,
    valor: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
