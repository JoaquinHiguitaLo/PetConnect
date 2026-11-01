
package com.example.petconnect.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity


private val PetFormGreen = Color(0xFF227C78)

@Composable
fun PetFormFields(
    nombre: String,
    onNombreChange: (String) -> Unit,
    especie: String,
    onEspecieChange: (String) -> Unit,
    raza: String,
    onRazaChange: (String) -> Unit,
    fechaNacimiento: String,
    onFechaNacimientoChange: (String) -> Unit,
    edad: String,
    onEdadChange: (String) -> Unit,
    tamano: String,
    onTamanoChange: (String) -> Unit,
    sexo: String,
    onSexoChange: (String) -> Unit,
    peso: String,
    onPesoChange: (String) -> Unit
) {
    var anchoCampoPx by remember {
        mutableIntStateOf(0)
    }

    val density = LocalDensity.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        PetFormTextField(
            titulo = "Nombre de la mascota",
            valor = nombre,
            onValueChange = onNombreChange,
            placeholder = "Ej. Luna, Coco, Milo",
            obligatorio = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                PetFormDropdown(
                    titulo = "Especie",
                    valor = especie,
                    opciones = listOf(
                        "Perro", "Gato", "Conejo", "Ave", "Otro"
                    ),
                    onSeleccionar = onEspecieChange,
                    obligatorio = true
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                PetFormDropdown(
                    titulo = "Raza",
                    valor = raza,
                    opciones = listOf(
                        "Criollo",
                        "Lobo siberiano",
                        "Pastor alemán",
                        "Pincher",
                        "Pitbull",
                        "Rottweiler",
                        "Bulldog francés"
                    ),
                    onSeleccionar = onRazaChange
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                PetFormBirthDate(
                    valor = fechaNacimiento,
                    onFechaChange = onFechaNacimientoChange
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                PetFormTextField(
                    titulo = "Edad (años)",
                    valor = edad,
                    onValueChange = onEdadChange,
                    placeholder = "Ej. 3",
                    obligatorio = true,
                    keyboardType = KeyboardType.Number
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                PetFormDropdown(
                    titulo = "Tamaño",
                    valor = tamano,
                    opciones = listOf(
                        "Pequeño",
                        "Mediano",
                        "Grande",
                        "Gigante"
                    ),
                    onSeleccionar = onTamanoChange,
                    obligatorio = true
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                PetFormDropdown(
                    titulo = "Sexo",
                    valor = sexo,
                    opciones = listOf(
                        "Macho",
                        "Hembra",
                        "No especificado"
                    ),
                    onSeleccionar = onSexoChange
                )
            }
        }

        PetFormTextField(
            titulo = "Peso (kg)",
            valor = peso,
            onValueChange = onPesoChange,
            placeholder = "Ej. 4.5",
            keyboardType = KeyboardType.Decimal
        )
    }
}

@Composable
private fun PetFormTextField(
    titulo: String,
    valor: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    obligatorio: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        PetFormLabel(titulo, obligatorio)

        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    maxLines = 1
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PetFormGreen,
                unfocusedBorderColor = Color(0xFFD9DEDC),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}

@Composable
private fun PetFormDropdown(
    titulo: String,
    valor: String,
    opciones: List<String>,
    onSeleccionar: (String) -> Unit,
    obligatorio: Boolean = false
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    var anchoCampoPx by remember {
        mutableIntStateOf(0)
    }

    val density = LocalDensity.current

    Column(
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        PetFormLabel(titulo, obligatorio)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { size ->
                    anchoCampoPx = size.width
                }
        ) {

            OutlinedTextField(
                value = valor,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                singleLine = true,
                placeholder = {
                    Text("Seleccionar", maxLines = 1)
                },
                trailingIcon = {
                    Text(
                        text = "⌄",
                        color = PetFormGreen
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PetFormGreen,
                    unfocusedBorderColor = Color(0xFFD9DEDC),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                modifier = Modifier.width(
                    with(density) {
                        anchoCampoPx.toDp()
                    }
                ),
                containerColor = Color.White,
                shape = RoundedCornerShape(14.dp)
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            onSeleccionar(opcion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PetFormBirthDate(
    valor: String,
     onFechaChange: (String) -> Unit
){
    var mostrarCalendario by remember {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        PetFormLabel("Fecha de nacimiento", false)

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = valor,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                singleLine = true,
                placeholder = {
                    Text("Seleccionar")
                },
                trailingIcon = {
                    Text("▦", color = PetFormGreen)
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PetFormGreen,
                    unfocusedBorderColor = Color(0xFFD9DEDC),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable {
                        mostrarCalendario = true
                    }
            )
        }
    }

    if (mostrarCalendario) {

        val formato = remember {
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.US
            ).apply {
                timeZone = TimeZone.getTimeZone("UTC")
                isLenient = false
            }
        }

        val fechaInicial = remember(valor) {
            runCatching {
                formato.parse(valor)?.time
            }.getOrNull()
        }

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = fechaInicial
        )

        DatePickerDialog(
            onDismissRequest = {
                mostrarCalendario = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onFechaChange(
                                formato.format(Date(it))
                            )
                        }
                        mostrarCalendario = false
                    }
                ) {
                    Text("Aceptar", color = PetFormGreen)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarCalendario = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = null,
                headline = null
            )
        }
    }
}

@Composable
private fun PetFormLabel(
    texto: String,
    obligatorio: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF303534)
        )

        if (obligatorio) {
            Text(
                text = " *",
                color = Color(0xFFE05D4F)
            )
        }
    }
}
