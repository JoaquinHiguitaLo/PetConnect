
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
import com.example.petconnect.ui.components.PetConnectTopBar
import com.example.petconnect.data.model.petConnectCategories
import com.example.petconnect.ui.components.PetConnectFormStyle
import com.example.petconnect.ui.components.PetConnectFormTitle
import com.example.petconnect.ui.components.PetConnectFormLabel
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity

// Identidad visual de PetConnect.
private val VerdeRegistro = Color(0xFF227C78)
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
    val categorias = petConnectCategories.map {
        it.id to it.nombre
    }

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

    var anchoCategoriaPx by remember {
        mutableIntStateOf(0)
    }

    val density = LocalDensity.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoRegistro)
    ) {

        // ==========================================
        // ENCABEZADO DEL FORMULARIO
        // ==========================================
        PetConnectTopBar(
            onBack = {
                if (!uiState.isLoading) {
                    onBack()
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        PetConnectFormTitle(
            title = "Registrar negocio"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // FORMULARIO
        // ==========================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                PetConnectFormLabel(
                    text = "Nombre del negocio",
                    required = true
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = {
                        Text("Ej. Clínica veterinaria")
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(
                        PetConnectFormStyle.fieldRadius
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PetConnectFormStyle.primary,
                        unfocusedBorderColor = PetConnectFormStyle.border,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }



            // Selección de categoría.
            PetConnectFormLabel(
                text = "Categoría del servicio",
                required = true
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { size ->
                        anchoCategoriaPx = size.width
                    }
            ) {
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
                    },
                    modifier = Modifier.width(
                        with(density) {
                            anchoCategoriaPx.toDp()
                        }
                    ),
                    containerColor = Color.White,
                    shape = RoundedCornerShape(14.dp)
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


            Column(
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                PetConnectFormLabel(
                    text = "Descripción",
                    required = true
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    placeholder = {
                        Text("Describe los servicios que ofreces")
                    },
                    minLines = 3,
                    maxLines = 5,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(PetConnectFormStyle.fieldRadius),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PetConnectFormStyle.primary,
                        unfocusedBorderColor = PetConnectFormStyle.border,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }


            HorizontalDivider(
                color = Color(0xFFE5E7E4)
            )

            Text(
                text = "Ubicación y contacto",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )


            Column(
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                PetConnectFormLabel(
                    text = "Dirección",
                    required = true
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    placeholder = {
                        Text("Ej. Calle 45 # 23-10")
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(PetConnectFormStyle.fieldRadius),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PetConnectFormStyle.primary,
                        unfocusedBorderColor = PetConnectFormStyle.border,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                PetConnectFormLabel(
                    text = "Teléfono de contacto",
                    required = true
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    placeholder = {
                        Text("Ej. 3001234567")
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(PetConnectFormStyle.fieldRadius),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PetConnectFormStyle.primary,
                        unfocusedBorderColor = PetConnectFormStyle.border,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }


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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    PetConnectFormLabel(
                        text = "Latitud",
                        required = true
                    )

                    OutlinedTextField(
                        value = latitud,
                        onValueChange = { latitud = it },
                        placeholder = {
                            Text("Ej. 6.2442")
                        },
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(PetConnectFormStyle.fieldRadius),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PetConnectFormStyle.primary,
                            unfocusedBorderColor = PetConnectFormStyle.border,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    PetConnectFormLabel(
                        text = "Longitud",
                        required = true
                    )

                    OutlinedTextField(
                        value = longitud,
                        onValueChange = { longitud = it },
                        placeholder = {
                            Text("Ej. -75.5812")
                        },
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(PetConnectFormStyle.fieldRadius),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PetConnectFormStyle.primary,
                            unfocusedBorderColor = PetConnectFormStyle.border,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
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
                    .fillMaxWidth(0.72f)
                    .height(48.dp)
                    .align(Alignment.CenterHorizontally),
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
