
package com.example.petconnect.ui.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petconnect.data.model.UserAddress
import com.example.petconnect.viewmodel.AddressViewModel
import com.example.petconnect.ui.components.PetConnectTopBar
import com.example.petconnect.ui.components.PetConnectFormTitle
import com.example.petconnect.ui.components.PetConnectFormStyle
import androidx.compose.foundation.shape.CircleShape

private val VerdePetConnect = Color(0xFF227C78)
private val FondoPetConnect = Color(0xFFFAF9F6)

@Composable
fun MyAddressesScreen(
    onBack: () -> Unit,
    onAddAddress: () -> Unit,
    modifier: Modifier = Modifier,
    addressViewModel: AddressViewModel = viewModel(
        key = "my_addresses"
    )
) {

    val uiState by addressViewModel.uiState.collectAsState()

    // Actualizamos las direcciones al entrar.
    LaunchedEffect(addressViewModel) {
        addressViewModel.cargarDirecciones()
        addressViewModel.cargarDireccionActiva()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoPetConnect)
    ) {

        // =========================================
        // ENCABEZADO
        // =========================================

        PetConnectTopBar(
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(8.dp))

        PetConnectFormTitle(
            title = "Mis direcciones"
        )

        Spacer(modifier = Modifier.height(16.dp))



        // =========================================
        // CONTENIDO
        // =========================================

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                )
        ) {
            when {

                uiState.isLoading -> {

                    CircularProgressIndicator(
                        modifier = Modifier.align(
                            Alignment.Center
                        ),
                        color = VerdePetConnect
                    )
                }

                uiState.errorMessage != null -> {

                    Column(
                        modifier = Modifier.align(
                            Alignment.Center
                        ),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = uiState.errorMessage
                                ?: "Ocurrió un error.",
                            color =
                                MaterialTheme.colorScheme.error
                        )

                        TextButton(
                            onClick = {
                                addressViewModel.cargarDirecciones()
                            }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                uiState.direcciones.isEmpty() -> {

                    Column(
                        modifier = Modifier.align(
                            Alignment.Center
                        ),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = "📍",
                            style =
                                MaterialTheme.typography.displayLarge
                        )

                        Text(
                            text = "Aún no tienes direcciones",
                            style =
                                MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Agrega tu casa, trabajo u otra " +
                                    "ubicación para encontrar servicios " +
                                    "cerca de ti.",
                            style =
                                MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF777777)
                        )
                    }
                }

                else -> {

                    Column(
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {


                        Surface(
                            color = Color(0xFFB9E7E4),
                            shape = CircleShape
                        ) {
                            Text(
                                text = if (uiState.direcciones.size == 1) {
                                    "1 dirección"
                                } else {
                                    "${uiState.direcciones.size} direcciones"
                                },
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 7.dp
                                ),
                                color = VerdePetConnect,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }



                        uiState.direcciones.forEach { direccion ->

                            AddressCard(
                                direccion = direccion,

                                // Comparamos con el ID guardado en Firestore.
                                seleccionada = uiState.direccionActivaId == direccion.id,

                                habilitada = !uiState.isSaving,

                                onClick = {
                                    if (uiState.direccionActivaId != direccion.id) {

                                        addressViewModel.limpiarResultadoOperacion()

                                        addressViewModel.seleccionarDireccion(
                                            direccion.id
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // =========================================
        // BOTÓN INFERIOR
        // =========================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onAddAddress,
                modifier = Modifier
                    .fillMaxWidth(
                        PetConnectFormStyle.buttonWidthFraction
                    )
                    .height(PetConnectFormStyle.buttonHeight),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PetConnectFormStyle.primary
                ),
                shape = RoundedCornerShape(
                    PetConnectFormStyle.buttonRadius
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Agregar dirección",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

    }
}

@Composable
private fun AddressCard(
    direccion: UserAddress,
    seleccionada: Boolean,
    habilitada: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        enabled = habilitada,
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),

        border = androidx.compose.foundation.BorderStroke(
            width = if (seleccionada) 2.dp else 1.dp,
            color = if (seleccionada) {
                VerdePetConnect
            } else {
                Color(0xFFE5E9E7)
            }
        )
    ) {
    Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = VerdePetConnect,
                modifier = Modifier.size(28.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = direccion.nombre,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = direccion.direccion,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF666666)
                )

                Text(
                    text = "${direccion.ciudad}, " +
                            direccion.departamento,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF777777)
                )
            }

            if (seleccionada) {

                Text(
                    text = "Activa ✓",
                    color = VerdePetConnect,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
