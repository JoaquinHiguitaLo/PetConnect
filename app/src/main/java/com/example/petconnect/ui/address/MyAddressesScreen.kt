
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

private val VerdePetConnect = Color(0xFF227A76)
private val FondoPetConnect = Color(0xFFF9F8F4)

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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    VerdePetConnect,
                    RoundedCornerShape(
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp
                    )
                )
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 16.dp,
                    bottom = 24.dp
                )
        ) {

            IconButton(
                onClick = onBack,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White.copy(
                        alpha = 0.12f
                    )
                )
            ) {
                Icon(
                    imageVector =
                        Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Mis direcciones",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Administra tus ubicaciones guardadas",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.85f)
            )
        }

        // =========================================
        // CONTENIDO
        // =========================================

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(20.dp)
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

                        Text(
                            text =
                                "${uiState.direcciones.size} direcciones guardadas",
                            style =
                                MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )


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

        Button(
            onClick = onAddAddress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdePetConnect
            ),
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(14.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Agregar dirección",
                fontWeight = FontWeight.Bold
            )
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
        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        border = if (seleccionada) {
            androidx.compose.foundation.BorderStroke(
                2.dp,
                VerdePetConnect
            )
        } else {
            null
        }
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
