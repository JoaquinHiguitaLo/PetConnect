
package com.example.petconnect.ui.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import org.maplibre.android.geometry.LatLng
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petconnect.viewmodel.AddressViewModel
import com.example.petconnect.data.model.UserAddress

private val VerdeDireccion = Color(0xFF227A76)
private val FondoDireccion = Color(0xFFF9F8F4)

@Composable
fun AddAddressScreen(
    onBack: () -> Unit,
    onSelectLocation: () -> Unit,
    modifier: Modifier = Modifier,
    onSaved: () -> Unit = {},
    addressViewModel: AddressViewModel = viewModel(
        key = "my_addresses"
    )
) {


    var nombre by rememberSaveable {
        mutableStateOf("")
    }

    var direccion by rememberSaveable {
        mutableStateOf("")
    }

    var ciudad by rememberSaveable {
        mutableStateOf("Medellín")
    }

    var departamento by rememberSaveable {
        mutableStateOf("Antioquia")
    }

    // Controla si está abierto el selector de MapLibre.
    var mostrarSelectorMapa by remember {
        mutableStateOf(false)
    }

    // Coordenadas seleccionadas para esta dirección.
    var coordenadasDireccion by remember {
        mutableStateOf<LatLng?>(null)
    }

    val addressUiState by addressViewModel.uiState.collectAsState()

    // Cuando Firestore confirma el guardado,
    // regresamos a la lista de direcciones.
    LaunchedEffect(addressUiState.operationSuccess) {

        if (addressUiState.operationSuccess) {

            addressViewModel.limpiarResultadoOperacion()

            onSaved()
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoDireccion)
    ) {

        // ENCABEZADO
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    VerdeDireccion,
                    RoundedCornerShape(
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp
                    )
                )
                .padding(20.dp)
        ) {

            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.White
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Agregar dirección",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Registra una ubicación para buscar servicios cercanos",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.85f)
            )
        }

        // FORMULARIO
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Información de la dirección",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre (Casa, Trabajo, Otra)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ciudad,
                onValueChange = { ciudad = it },
                label = { Text("Ciudad") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = departamento,
                onValueChange = { departamento = it },
                label = { Text("Departamento") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider()

            Text(
                text = "Ubicación en el mapa",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedButton(
                onClick = {
                    mostrarSelectorMapa = true
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null
                )

                Spacer(Modifier.width(8.dp))

                Text("Seleccionar ubicación en el mapa")
            }
            coordenadasDireccion?.let { coordenadas ->

                Text(
                    text = "Ubicación seleccionada correctamente",
                    color = VerdeDireccion,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Latitud: %.6f".format(coordenadas.latitude),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Longitud: %.6f".format(coordenadas.longitude),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "Selecciona el punto exacto donde deseas " +
                        "buscar establecimientos cercanos.",
                color = Color(0xFF777777),
                style = MaterialTheme.typography.bodySmall
            )
            addressUiState.errorMessage?.let { error ->

                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 6.dp
                    )
                )
            }
        }

        // BOTÓN INFERIOR
        Button(

            onClick = {

                val coordenadas = coordenadasDireccion

                if (coordenadas != null &&
                    !addressUiState.isSaving
                ) {

                    // Limpiamos resultados anteriores.
                    addressViewModel.limpiarResultadoOperacion()

                    val nuevaDireccion = UserAddress(
                        nombre = nombre.trim(),
                        direccion = direccion.trim(),
                        ciudad = ciudad.trim(),
                        departamento = departamento.trim(),
                        latitud = coordenadas.latitude,
                        longitud = coordenadas.longitude
                    )

                    // El Repository asigna el UID y el ID
                    // generado por Firestore.
                    addressViewModel.crearDireccion(nuevaDireccion)
                }
            },
            enabled = nombre.isNotBlank() &&
                    direccion.isNotBlank() &&
                    ciudad.isNotBlank() &&
                    departamento.isNotBlank() &&
                    coordenadasDireccion != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeDireccion
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            if (addressUiState.isSaving) {

                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )

                Spacer(Modifier.width(8.dp))

                Text("Guardando...")

            } else {

                Text(
                    text = "Guardar dirección",
                    fontWeight = FontWeight.Bold
                )
            }

            if (mostrarSelectorMapa) {

                SelectAddressLocationScreen(
                    ubicacionInicial = coordenadasDireccion
                        ?: LatLng(6.2442, -75.5812),

                    onBack = {
                        mostrarSelectorMapa = false
                    },

                    onLocationSelected = { coordenadas ->

                        // Conservamos el punto seleccionado.
                        coordenadasDireccion = coordenadas

                        // Cerramos el mapa y regresamos al formulario.
                        mostrarSelectorMapa = false
                    }
                )
            }

        }
    }
}
