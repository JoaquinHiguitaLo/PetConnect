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
import com.example.petconnect.ui.components.PetConnectTopBar
import androidx.compose.material3.MaterialTheme

@Composable
fun AddPetScreen(
    modifier: Modifier = Modifier,
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
        mutableStateOf("")
    }

    var species by remember {
        mutableStateOf("")
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
        mutableStateOf("")
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
        mutableStateOf("")
    }

    var size by remember {
        mutableStateOf("")
    }
    var fechaNacimiento by remember {
        mutableStateOf("")
    }

    var sexo by remember {
        mutableStateOf("")
    }

    var peso by remember {
        mutableStateOf("")
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

        PetConnectTopBar(
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Agregar mascota",
            color = Color(0xFF227C78),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))


        // ============================================================
        // CONTENIDO DEL FORMULARIO
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // ========================================================
            // FOTO
            // ========================================================


            Box(
                modifier = Modifier
                    .size(width = 180.dp, height = 130.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(
                        color = Color(0xFFB9E7E4),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (species.lowercase()) {
                        "perro" -> "🐕"
                        "gato" -> "🐈"
                        "conejo" -> "🐰"
                        "ave" -> "🐦"
                        else -> "🐾"
                    },
                    fontSize = 36.sp
                )
            }

            PetFormFields(
                nombre = name,
                onNombreChange = { name = it },

                especie = species,
                onEspecieChange = { species = it },

                raza = breed,
                onRazaChange = { breed = it },

                fechaNacimiento = fechaNacimiento,
                onFechaNacimientoChange = {
                    fechaNacimiento = it
                },

                edad = age,
                onEdadChange = { age = it },

                tamano = size,
                onTamanoChange = { size = it },

                sexo = sexo,
                onSexoChange = { sexo = it },

                peso = peso,
                onPesoChange = { peso = it }
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
                    val usuarioId = FirebaseAuth
                        .getInstance()
                        .currentUser
                        ?.uid

                    if (usuarioId != null) {

                        val pet = Pet(
                            id = "",
                            usuarioId = usuarioId,
                            nombre = name.trim(),
                            especie = species,
                            raza = breed.ifBlank { null },
                            edad = age.toInt(),
                            tamano = size,
                            foto = null,
                            fechaNacimiento = fechaNacimiento.ifBlank { null },
                            sexo = sexo.ifBlank { null },
                            pesoKg = peso.replace(",", ".").toDoubleOrNull()
                        )

                        petsViewModel.agregarMascota(pet)
                    }
                },

                enabled = formularioValido && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(48.dp)
                    .align(Alignment.CenterHorizontally),
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
                        "Guardar mascota" }

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