package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun PersonalInfoScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {

    // ============================================================
    // ESTADO DEL PERFIL
    // ============================================================

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Controla si estamos viendo los datos o editándolos.
    var isEditing by remember { mutableStateOf(false) }

    // Campos temporales utilizados durante la edición.
    var nombreCompleto by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }


    // ============================================================
    // CARGAR USUARIO
    // ============================================================

    LaunchedEffect(Unit) {
        viewModel.cargarUsuario()
    }


    // ============================================================
    // SINCRONIZAR LOS CAMPOS CUANDO LLEGA EL USUARIO
    // ============================================================

    LaunchedEffect(uiState.user) {

        uiState.user?.let { user ->

            nombreCompleto = user.nombreCompleto
            telefono = user.telefono
        }
    }


    // ============================================================
    // INTERFAZ
    // ============================================================

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F6))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ============================================================
        // ENCABEZADO
        // ============================================================

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {

            // Botón para regresar a la pantalla anterior.
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }

            Text(
                text = "MI PERFIL",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF227C78),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ============================================================
        // AVATAR
        // ============================================================

        Box(
            modifier = Modifier
                .size(82.dp)
                .background(
                    color = Color(0xFFB5E3E1),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🐾",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(10.dp))


        // ============================================================
        // NOMBRE Y CORREO PRINCIPAL
        // ============================================================

        Text(
            text = uiState.user?.nombreCompleto
                ?.takeIf { it.isNotBlank() }
                ?: "Usuario",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = uiState.user?.correo ?: "Cargando...",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(28.dp))


        // ============================================================
        // TÍTULO
        // ============================================================

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Información personal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))


            // ========================================================
            // MODO EDICIÓN
            // ========================================================

            if (isEditing) {

                OutlinedTextField(
                    value = nombreCompleto,
                    onValueChange = {
                        nombreCompleto = it
                    },
                    label = {
                        Text("Nombre completo")
                    },
                    placeholder = {
                        Text("Ej: Joaquin Higuita")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // El correo se muestra como información,
                // pero no se modifica desde aquí.
                OutlinedTextField(
                    value = uiState.user?.correo ?: "",
                    onValueChange = {},
                    label = {
                        Text("Correo electrónico")
                    },
                    enabled = false,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = telefono,
                    onValueChange = {
                        telefono = it
                    },
                    label = {
                        Text("Teléfono")
                    },
                    placeholder = {
                        Text("Ej: 3001234567")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

            } else {

                // ====================================================
                // MODO CONSULTA
                // ====================================================

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        )
                ) {

                    PersonalDataItem(
                        label = "Nombre completo",
                        value = uiState.user?.nombreCompleto
                            ?.takeIf { it.isNotBlank() }
                            ?: "Sin registrar"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PersonalDataItem(
                        label = "Correo electrónico",
                        value = uiState.user?.correo
                            ?: "Sin registrar"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PersonalDataItem(
                        label = "Teléfono",
                        value = uiState.user?.telefono
                            ?.takeIf { it.isNotBlank() }
                            ?: "Sin registrar"
                    )
                }
            }
        }


        // ============================================================
        // ERROR
        // ============================================================

        uiState.errorMessage?.let { error ->

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }


        Spacer(modifier = Modifier.height(18.dp))


        // ============================================================
        // BOTÓN PRINCIPAL
        // ============================================================

        if (isEditing) {

            Button(
                onClick = {

                    val currentUser = uiState.user

                    if (currentUser != null) {

                        val updatedUser = currentUser.copy(
                            nombreCompleto = nombreCompleto.trim(),
                            telefono = telefono.trim()
                        )

                        viewModel.actualizarUsuario(updatedUser)

                        isEditing = false
                    }
                },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF227C78)
                )
            ) {

                if (uiState.isLoading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )

                } else {

                    Text(
                        text = "Guardar cambios",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {
                    // Cancelamos la edición y restauramos
                    // los valores que vienen de Firestore.
                    nombreCompleto = uiState.user?.nombreCompleto ?: ""
                    telefono = uiState.user?.telefono ?: ""

                    isEditing = false
                },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Cancelar",
                    fontWeight = FontWeight.Bold
                )
            }

        } else {

            Button(
                onClick = {
                    isEditing = true
                },
                enabled = uiState.user != null && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF227C78)
                )
            ) {
                Text(
                    text = "Editar perfil",
                    fontWeight = FontWeight.Bold
                )
            }
        }


        Spacer(modifier = Modifier.height(14.dp))


        // ============================================================
        // CERRAR SESIÓN
        // ============================================================

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFFFFD8D2),
                contentColor = Color(0xFF8B1E14)
            )
        ) {
            Text(
                text = "Cerrar sesión",
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// ====================================================================
// COMPONENTE REUTILIZABLE
// ====================================================================

@Composable
private fun PersonalDataItem(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}