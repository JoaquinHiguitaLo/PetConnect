
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity

import com.example.petconnect.data.model.petConnectCategories
import com.example.petconnect.ui.components.PetConnectTopBar
import com.example.petconnect.ui.components.PetConnectFormTitle
import com.example.petconnect.ui.components.PetConnectFormStyle
import com.example.petconnect.ui.components.PetConnectFormLabel

private val VerdeEdicion = Color(0xFF227C78)
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

    val categorias = petConnectCategories.map {
        it.id to it.nombre
    }

    val categoriaNombre = categorias
        .firstOrNull { it.first == categoria }
        ?.second ?: categoria
    var anchoCategoriaPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

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

        PetConnectTopBar(
            onBack = {
                if (!uiState.isLoading) {
                    onBack()
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        PetConnectFormTitle(
            title = "Editar negocio"
        )

        Spacer(modifier = Modifier.height(16.dp))


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


            PetConnectFormLabel(
                text = "Categoría del servicio",
                required = true
            )

            Box {
                OutlinedButton(
                    onClick = { mostrarCategorias = true },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { anchoCategoriaPx = it.width }
                ) {
                    Text(categoriaNombre, color = VerdeEdicion)
                    Spacer(Modifier.weight(1f))
                    Text("⌄", color = VerdeEdicion)
                }

                DropdownMenu(
                    expanded = mostrarCategorias,
                    modifier = Modifier.width(
                        with(density) { anchoCategoriaPx.toDp() }
                    ),
                    containerColor = Color.White,
                    shape = RoundedCornerShape(14.dp),
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


            HorizontalDivider()

            Text(
                text = "Ubicación y contacto",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )


            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                PetConnectFormLabel("Dirección", required = true)

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
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

            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                PetConnectFormLabel("Teléfono de contacto", required = true)

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    PetConnectFormLabel("Latitud", required = true)

                    OutlinedTextField(
                        value = latitud,
                        onValueChange = { latitud = it },
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
                    PetConnectFormLabel("Longitud", required = true)

                    OutlinedTextField(
                        value = longitud,
                        onValueChange = { longitud = it },
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
                    .fillMaxWidth(0.72f)
                    .height(48.dp)
                    .align(Alignment.CenterHorizontally),
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
