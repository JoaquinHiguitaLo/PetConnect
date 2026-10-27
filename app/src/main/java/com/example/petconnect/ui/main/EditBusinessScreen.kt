
package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petconnect.data.model.Business
import com.example.petconnect.viewmodel.BusinessViewModel

private val VerdeEdicion = Color(0xFF1F6F6B)
private val FondoEdicion = Color(0xFFFAF9F6)

@Composable
fun EditBusinessScreen(
    business: Business,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onUpdated: () -> Unit,
    businessViewModel: BusinessViewModel = viewModel(
        key = "edit_business"
    )
) {
    // Los campos se inicializan con los datos del negocio.
    // La clave evita conservar los datos de otro negocio.
    var nombre by remember(business.id) {
        mutableStateOf(business.nombre)
    }
    var categoria by remember(business.id) {
        mutableStateOf(business.categoria)
    }
    var descripcion by remember(business.id) {
        mutableStateOf(business.descripcion)
    }
    var direccion by remember(business.id) {
        mutableStateOf(business.direccion)
    }
    var telefono by remember(business.id) {
        mutableStateOf(business.telefono)
    }
    var latitud by remember(business.id) {
        mutableStateOf(business.latitud.toString())
    }
    var longitud by remember(business.id) {
        mutableStateOf(business.longitud.toString())
    }

    var mostrarCategorias by remember {
        mutableStateOf(false)
    }
    var errorFormulario by remember {
        mutableStateOf<String?>(null)
    }
    var guardadoSolicitado by remember(business.id) {
        mutableStateOf(false)
    }

    val uiState by businessViewModel.uiState.collectAsState()

    val categorias = listOf(
        "veterinaria" to "Veterinaria",
        "peluqueria" to "Peluquería",
        "bano" to "Baño",
        "guarderia" to "Guardería",
        "paseadores" to "Paseadores",
        "adiestramiento" to "Adiestramiento",
        "tiendas" to "Tienda",
        "parques" to "Parques"
    )

    val categoriaNombre = categorias
        .firstOrNull { it.first == categoria }
        ?.second ?: categoria

    // Limpiamos cualquier resultado anterior al abrir.
    LaunchedEffect(business.id) {
        businessViewModel.limpiarResultadoOperacion()
    }

    // Solo navegamos si este formulario solicitó guardar.
    LaunchedEffect(
        guardadoSolicitado,
        uiState.operationSuccess
    ) {
        if (guardadoSolicitado && uiState.operationSuccess) {
            guardadoSolicitado = false
            businessViewModel.limpiarResultadoOperacion()
            onUpdated()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoEdicion)
    ) {
        BusinessHeader(
            titulo = "Editar establecimiento",
            subtitulo = "Actualiza la información de tu negocio",
            onBack = onBack,
            backEnabled = !uiState.isLoading
        )

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
                text = "Modifica los datos que necesites.",
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
                    Text(categoriaNombre, color = VerdeEdicion)
                    Spacer(Modifier.weight(1f))
                    Text("⌄", color = VerdeEdicion)
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

            HorizontalDivider()

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

            Surface(
                color = Color(0xFFE3F3EF),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Tu estado de verificación y calificación no pueden modificarse desde este formulario.",
                    modifier = Modifier.padding(16.dp),
                    color = VerdeEdicion
                )
            }

            errorFormulario?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    val lat = latitud.trim().toDoubleOrNull()
                    val lon = longitud.trim().toDoubleOrNull()

                    errorFormulario = when {
                        nombre.isBlank() ->
                            "Ingresa el nombre del negocio."
                        categoria.isBlank() ->
                            "Selecciona una categoría."
                        descripcion.isBlank() ->
                            "Ingresa una descripción."
                        direccion.isBlank() ->
                            "Ingresa una dirección."
                        telefono.isBlank() ->
                            "Ingresa un teléfono."
                        lat == null || lat !in -90.0..90.0 ->
                            "La latitud debe estar entre -90 y 90."
                        lon == null || lon !in -180.0..180.0 ->
                            "La longitud debe estar entre -180 y 180."
                        else -> null
                    }

                    if (errorFormulario == null &&
                        lat != null && lon != null
                    ) {
                        // copy conserva los campos protegidos.
                        val negocioActualizado = business.copy(
                            nombre = nombre.trim(),
                            categoria = categoria,
                            descripcion = descripcion.trim(),
                            direccion = direccion.trim(),
                            telefono = telefono.trim(),
                            latitud = lat,
                            longitud = lon
                        )

                        guardadoSolicitado = true
                        businessViewModel.actualizarNegocio(
                            negocioActualizado
                        )
                    }
                },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeEdicion
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
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

            Spacer(Modifier.height(20.dp))
        }
    }
}
