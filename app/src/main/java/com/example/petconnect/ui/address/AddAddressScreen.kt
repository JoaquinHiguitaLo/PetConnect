
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
import com.example.petconnect.ui.components.PetConnectTopBar
import com.example.petconnect.ui.components.PetConnectFormTitle
import com.example.petconnect.ui.components.PetConnectFormLabel
import com.example.petconnect.ui.components.PetConnectFormStyle

private val VerdeDireccion = Color(0xFF227C78)
private val FondoDireccion = Color(0xFFFAF9F6)

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
        PetConnectTopBar(
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(8.dp))

        PetConnectFormTitle(
            title = "Agregar dirección"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // FORMULARIO
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Información de la dirección",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )


            CampoDireccion(
                titulo = "Nombre de la dirección",
                valor = nombre,
                onCambio = { nombre = it },
                ejemplo = "Ej. Casa, Trabajo, Universidad"
            )

            CampoDireccion(
                titulo = "Dirección",
                valor = direccion,
                onCambio = { direccion = it },
                ejemplo = "Ej. Cra 31A # 71-46"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CampoDireccion(
                    titulo = "Ciudad",
                    valor = ciudad,
                    onCambio = { ciudad = it },
                    ejemplo = "Ciudad",
                    modifier = Modifier.weight(1f)
                )

                CampoDireccion(
                    titulo = "Departamento",
                    valor = departamento,
                    onCambio = { departamento = it },
                    ejemplo = "Departamento",
                    modifier = Modifier.weight(1f)
                )
            }


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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(
                    PetConnectFormStyle.fieldRadius
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    PetConnectFormStyle.border
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = VerdeDireccion
                )
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (coordenadasDireccion == null) {
                        "Seleccionar ubicación en el mapa"
                    } else {
                        "Cambiar ubicación en el mapa"
                    }
                )
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
                    coordenadasDireccion != null &&
                    !addressUiState.isSaving,

            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(48.dp)
                .align(Alignment.CenterHorizontally),

            colors = ButtonDefaults.buttonColors(
                containerColor = PetConnectFormStyle.primary
            ),

            shape = RoundedCornerShape(
                PetConnectFormStyle.buttonRadius
            )
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



        }
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

@Composable
fun CampoDireccion(
    titulo: String,
    valor: String,
    onCambio: (String) -> Unit,
    ejemplo: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        PetConnectFormLabel(
            text = titulo,
            required = true
        )

        OutlinedTextField(
            value = valor,
            onValueChange = onCambio,
            placeholder = {
                Text(ejemplo)
            },
            singleLine = true,
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
}
