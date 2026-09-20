package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petconnect.data.model.Pet
import com.example.petconnect.viewmodel.PetsViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.*


@Composable
fun EditPetScreen(
    modifier: Modifier = Modifier,
    pet: Pet,
    onBack: () -> Unit
) {

    // ============================================================
    // ESTADO TEMPORAL DEL FORMULARIO
    // ============================================================
    // Por ahora solamente guardamos lo que escribe el usuario
    // mientras permanece en esta pantalla.
    //
    // Más adelante estos datos serán enviados a Firebase/Firestore.
    // ============================================================


    var name by remember {
        mutableStateOf(pet.nombre)
    }

    var species by remember {
        mutableStateOf(pet.especie)
    }

    var speciesExpanded by remember {
        mutableStateOf(false)
    }

    val speciesOptions = listOf(
        "Perro",
        "Gato",
        "Conejo",
        "Ave",
        "Otro"
    )

    var breed by remember {
        //raza puede ser null
        mutableStateOf(pet.raza ?: "")
    }

    var breedExpanded by remember {
        mutableStateOf(false)
    }

    val breedOptions = listOf(
        "Criollo",
        "Lobo siberiano",
        "Pastor alemán",
        "Pincher",
        "Pitbull",
        "Rottweiler",
        "Bulldog francés"
    )

    var age by remember {
        mutableStateOf(pet.edad.toString())
    }

    var size by remember {
        mutableStateOf(pet.tamano)
    }

    val petsViewModel: PetsViewModel = viewModel()
    val uiState by petsViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.operationSuccess) {

        if (uiState.operationSuccess) {

            petsViewModel.limpiarResultadoOperacion()

            onBack()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F6))
            .verticalScroll(rememberScrollState())
    ) {

        // ============================================================
        // ENCABEZADO
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF227C78)
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = onBack,
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4C918D)
                    )
                ) {
                    Text(
                        text = "←",
                        color = Color.White,
                        fontSize = 26.sp
                    )
                }

                Spacer(
                    modifier = Modifier.size(16.dp)
                )

                Column {

                    Text(
                        text = "NUEVA MASCOTA",
                        color = Color(0xFFB5E3E1),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "Editar mascota",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }


        // ============================================================
        // CONTENIDO DEL FORMULARIO
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // ========================================================
            // FOTO
            // ========================================================

            Box(
                modifier = Modifier
                    .size(125.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(
                        color = Color(0xFFF0EEE6),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "📷",
                        fontSize = 32.sp
                    )

                    Text(
                        text = "Subir foto",
                        color = Color(0xFF777777),
                        fontSize = 14.sp
                    )
                }
            }


            // ========================================================
            // NOMBRE
            // ========================================================

            PetFieldLabel(
                text = "Nombre de tu mascota",
                required = true
            )

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Ej: Luna, Michi, Coco...")
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )


            // ========================================================
            // ESPECIE
            // ========================================================

            PetFieldLabel(
                text = "Especie",
                required = true
            )

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // --------------------------------------------------------
                // CAMPO VISUAL
                // --------------------------------------------------------
                // El campo es solo de lectura porque la especie se
                // selecciona desde una lista y no se escribe manualmente.

                OutlinedTextField(
                    value = species,
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            speciesExpanded = true
                        },
                    placeholder = {
                        Text("Selecciona la especie")
                    },
                    trailingIcon = {
                        Text("▼")
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    readOnly = true
                )
                // --------------------------------------------------------
                // CAPA DE CLIC
                // --------------------------------------------------------
                // Esta capa transparente se coloca encima del TextField
                // para detectar cuando el usuario toca el campo.

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            speciesExpanded = true
                        }
                )

                // --------------------------------------------------------
                // MENÚ DESPLEGABLE
                // --------------------------------------------------------

                DropdownMenu(
                    expanded = speciesExpanded,
                    onDismissRequest = {
                        speciesExpanded = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    containerColor = Color.White,
                    tonalElevation = 2.dp,
                    shadowElevation = 6.dp
                ) {

                    speciesOptions.forEach { option ->

                        DropdownMenuItem(
                            text = {
                                Text(option)
                            },
                            onClick = {
                                // Guardamos la especie seleccionada.
                                species = option

                                speciesExpanded = false
                            }
                        )
                    }
                }
            }

            // ========================================================
            // RAZA
            // ========================================================

            PetFieldLabel(
                text = "Raza",
                required = false
            )
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // --------------------------------------------------------
                // CAMPO VISUAL
                // --------------------------------------------------------
                // El campo es solo de lectura porque la especie se
                // selecciona desde una lista y no se escribe manualmente.

                OutlinedTextField(
                    value = breed,
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            breedExpanded = true
                        },
                    placeholder = {
                        Text("Selecciona la raza")
                    },
                    trailingIcon = {
                        Text("▼")
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    readOnly = true
                )
                // --------------------------------------------------------
                // CAPA DE CLIC
                // --------------------------------------------------------
                // Esta capa transparente se coloca encima del TextField
                // para detectar cuando el usuario toca el campo.

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            breedExpanded = true
                        }
                )

                // --------------------------------------------------------
                // MENÚ DESPLEGABLE
                // --------------------------------------------------------

                DropdownMenu(
                    expanded = breedExpanded,
                    onDismissRequest = {
                        breedExpanded = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    containerColor = Color.White,
                    tonalElevation = 2.dp,
                    shadowElevation = 6.dp
                ) {

                    breedOptions.forEach { option ->

                        DropdownMenuItem(
                            text = {
                                Text(option)
                            },
                            onClick = {
                                // Guardamos la especie seleccionada.
                                breed = option

                                breedExpanded = false
                            }
                        )
                    }
                }
            }

            // ========================================================
            // EDAD
            // ========================================================

            PetFieldLabel(
                text = "Edad",
                required = true
            )

            OutlinedTextField(

                value = age,
                onValueChange = {
                    age = it
                },
                modifier = Modifier.fillMaxWidth(),

                placeholder = {
                    Text("Ej: 3")
                },

                keyboardOptions = KeyboardOptions (
                    keyboardType = KeyboardType.Number
                ),

                shape = RoundedCornerShape(16.dp),

                singleLine = true

            )


            // ========================================================
            // TAMAÑO
            // ========================================================

            PetFieldLabel(
                text = "Tamaño",
                required = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                SizeButton(
                    text = "Pequeño",
                    selected = size == "Pequeño",
                    onClick = {
                        size = "Pequeño"
                    }
                )

                SizeButton(
                    text = "Mediano",
                    selected = size == "Mediano",
                    onClick = {
                        size = "Mediano"
                    }
                )

                SizeButton(
                    text = "Grande",
                    selected = size == "Grande",
                    onClick = {
                        size = "Grande"
                    }
                )
            }

            SizeButton(
                text = "Gigante",
                selected = size == "Gigante",
                onClick = {
                    size = "Gigante"
                }
            )


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            // ========================================================
            // GUARDAR
            // ==============  ==========================================

            val formularioValido =
                name.isNotBlank() &&
                        species.isNotBlank() &&
                        age.toIntOrNull() != null &&
                        size.isNotBlank()

            Button(
                onClick = {
                    val mascotaActualizada = pet.copy(
                        nombre = name.trim(),
                        especie = species,
                        raza = breed.ifBlank { null },
                        edad = age.toInt(),
                        tamano = size
                    )

                    petsViewModel.actualizarMascota(mascotaActualizada)
                },

                enabled = formularioValido && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (formularioValido) {
                        Color(0xFF227C78)
                    } else {
                        Color(0xFFEAE7DE)
                    },
                    contentColor = if (formularioValido) {
                        Color.White
                    } else {
                        Color(0xFF777777)
                    }
                )
            ) {

                Text(
                    if (uiState.isLoading) {
                        "Guardando..."
                    } else {
                        "Guardar cambios" }

                )
            }
        }
    }
}


// ================================================================
// LABEL DE LOS CAMPOS
// ================================================================

@Composable
private fun PetFieldLabel(
    text: String,
    required: Boolean
) {

    Row {

        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF202020)
        )

        if (required) {

            Text(
                text = " *",
                color = Color(0xFFE05D4F),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// ================================================================
// BOTÓN DE TAMAÑO
// ================================================================

@Composable
private fun SizeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) {
                Color(0xFF227C78)
            } else {
                Color.White
            },
            contentColor = if (selected) {
                Color.White
            } else {
                Color(0xFF202020)
            }
        )
    ) {

        Text(
            text = text,
            fontWeight = FontWeight.Medium
        )
    }
}