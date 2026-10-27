
package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petconnect.data.model.Business
import com.example.petconnect.viewmodel.BusinessViewModel
import com.google.firebase.auth.FirebaseAuth

// Identidad visual de PetConnect.
private val VerdeRegistro = Color(0xFF1F6F6B)
private val FondoRegistro = Color(0xFFFAF9F6)

@Composable
fun AddBusinessScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onCreated: () -> Unit,
    businessViewModel: BusinessViewModel = viewModel(
        key = "add_business"
    )
) {

    // ==========================================
    // ESTADO DEL FORMULARIO
    // ==========================================

    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var latitud by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }

    var mostrarCategorias by remember {
        mutableStateOf(false)
    }

    var errorFormulario by remember {
        mutableStateOf<String?>(null)
    }

    val uiState by businessViewModel.uiState.collectAsState()

    // Categorías conocidas de PetConnect.
    // Sus identificadores deben coincidir con Firestore.
    val categorias = listOf(
        "veterinaria" to "Veterinaria",
        "peluqueria" to "Peluquería",
        "bano" to "Baño",
        "guarderia" to "Guardería",
        "paseadores" to "Paseadores",
        "adiestramiento" to "Adiestramiento",
        "tiendas" to "Tienda"
    )

    val categoriaNombre = categorias
        .firstOrNull { it.first == categoria }
        ?.second ?: "Selecciona una categoría"

    // Regresamos al listado únicamente
    // después de confirmar el registro exitoso.
    LaunchedEffect(uiState.operationSuccess) {
        if (uiState.operationSuccess) {

            // Limpiamos el éxito anterior antes de navegar.
            businessViewModel.limpiarResultadoOperacion()

            // Regresamos al listado de negocios.
            onCreated()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoRegistro)
    ) {

        // ==========================================
        // ENCABEZADO DEL FORMULARIO
        // ==========================================

        BusinessHeader(
            titulo = "Registrar negocio",
            subtitulo = "Agrega tu establecimiento a PetConnect",
            onBack = onBack,
            backEnabled = !uiState.isLoading
        )
        // ==========================================
        // FORMULARIO
        // ==========================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Información del establecimiento",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Completa los datos de tu negocio.",
                color = Color.Gray
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del negocio") },
                singleLine = true,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            // Selección de categoría.
            Text(
                text = "Categoría del servicio",
                fontWeight = FontWeight.SemiBold
            )

            Box {
                OutlinedButton(
                    onClick = { mostrarCategorias = true },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = categoriaNombre,
                        color = VerdeRegistro
                    )

                    Spacer(Modifier.weight(1f))

                    Text("⌄", color = VerdeRegistro)
                }

                DropdownMenu(
                    expanded = mostrarCategorias,
                    onDismissRequest = {
                        mostrarCategorias = false
                    }
                ) {
                    categorias.forEach { (id, titulo) ->
                        DropdownMenuItem(
                            text = { Text(titulo) },
                            onClick = {
                                categoria = id
                                mostrarCategorias = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                minLines = 3,
                maxLines = 5,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            HorizontalDivider(
                color = Color(0xFFE5E7E4)
            )

            Text(
                text = "Ubicación y contacto",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                singleLine = true,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono de contacto") },
                singleLine = true,
                enabled = !uiState.isLoading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Text(
                text = "Coordenadas geográficas",
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Por ahora ingresa la latitud y longitud de tu establecimiento.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = latitud,
                    onValueChange = { latitud = it },
                    label = { Text("Latitud") },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = longitud,
                    onValueChange = { longitud = it },
                    label = { Text("Longitud") },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            // Aviso sobre la aprobación.
            Surface(
                color = Color(0xFFE3F3EF),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Tu negocio quedará pendiente de aprobación antes de aparecer en el directorio público.",
                    modifier = Modifier.padding(16.dp),
                    color = VerdeRegistro,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Errores de validación local.
            errorFormulario?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // Errores devueltos por Firestore.
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // ==========================================
            // BOTÓN REGISTRAR
            // ==========================================

            Button(
                onClick = {

                    val uid = FirebaseAuth.getInstance()
                        .currentUser?.uid

                    val latitudNumero = latitud
                        .trim()
                        .toDoubleOrNull()

                    val longitudNumero = longitud
                        .trim()
                        .toDoubleOrNull()

                    errorFormulario = when {
                        uid == null ->
                            "Debes iniciar sesión."

                        nombre.isBlank() ->
                            "Ingresa el nombre del negocio."

                        categoria.isBlank() ->
                            "Selecciona una categoría."

                        descripcion.isBlank() ->
                            "Ingresa una descripción."

                        direccion.isBlank() ->
                            "Ingresa la dirección."

                        telefono.isBlank() ->
                            "Ingresa un teléfono de contacto."

                        latitudNumero == null ||
                                latitudNumero !in -90.0..90.0 ->
                            "Ingresa una latitud válida entre -90 y 90."

                        longitudNumero == null ||
                                longitudNumero !in -180.0..180.0 ->
                            "Ingresa una longitud válida entre -180 y 180."

                        else -> null
                    }

                    if (errorFormulario == null && uid != null) {

                        val nuevoNegocio = Business(
                            prestadorId = uid,
                            nombre = nombre.trim(),
                            categoria = categoria,
                            descripcion = descripcion.trim(),
                            direccion = direccion.trim(),
                            telefono = telefono.trim(),
                            latitud = latitudNumero!!,
                            longitud = longitudNumero!!,
                            imagen = null,
                            calificacion = 0.0,
                            estadoVerificacion = "PENDIENTE"
                        )

                        // Reutilizamos el ViewModel existente.
                        businessViewModel.crearNegocio(
                            nuevoNegocio
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeRegistro
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Registrar negocio",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}
