
package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petconnect.data.model.Business
import com.example.petconnect.viewmodel.BusinessViewModel
import com.google.firebase.auth.FirebaseAuth


// Colores alineados con la identidad visual de PetConnect.
private val VerdePet = Color(0xFF1F6F6B)
private val VerdeClaroPet = Color(0xFFB7E4DE)
private val FondoPet = Color(0xFFFAF9F6)
private val TextoPet = Color(0xFF243B38)

@Composable
fun MyBusinessesScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onAddBusiness: () -> Unit,
    onBusinessSelected: (Business) -> Unit,
    successMessage: String? = null,
    onMessageShown: () -> Unit = {},
    businessViewModel: BusinessViewModel = viewModel(
        key = "my_businesses"
    )
) {
    // Identidad del usuario autenticado.
    val prestadorId = FirebaseAuth.getInstance()
        .currentUser?.uid

    // Estado de los negocios consultados.
    val uiState by businessViewModel.uiState.collectAsState()

    // Controla los mensajes temporales de confirmación.
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    // Muestra el mensaje cuando MainScreen confirma
    // que la actualización terminó correctamente.
    LaunchedEffect(successMessage) {
        if (successMessage != null) {
            snackbarHostState.showSnackbar(
                message = successMessage,
                duration = SnackbarDuration.Short
            )

            onMessageShown()
        }
    }

    // Se ejecuta al entrar o cambiar de usuario.
    LaunchedEffect(prestadorId) {
        if (prestadorId != null) {
            businessViewModel.cargarNegociosPorPrestador(
                prestadorId
            )
        }
    }
    Box() {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {

            // ==========================================
            // ENCABEZADO DEL PRESTADOR
            // ==========================================

            BusinessHeader(
                titulo = "Mis negocios",
                subtitulo = "Gestiona tus establecimientos",
                onBack = onBack
            )


            // ==========================================
            // CONTENIDO
            // ==========================================

            when {
                prestadorId == null -> {
                    MensajeNegocios(
                        titulo = "Sesión no disponible",
                        descripcion = "Inicia sesión para consultar tus negocios."
                    )
                }

                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = VerdePet
                        )
                    }
                }

                uiState.errorMessage != null -> {
                    MensajeNegocios(
                        titulo = "No pudimos cargar tus negocios",
                        descripcion = uiState.errorMessage
                            ?: "Ocurrió un error inesperado."
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {

                        Spacer(Modifier.height(26.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Negocios registrados",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextoPet,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Surface(
                                color = Color(0xFFE3F3EF),
                                shape = CircleShape
                            ) {
                                Text(
                                    text = if (uiState.businesses.size == 1) {
                                        "1 negocio"
                                    } else {
                                        "${uiState.businesses.size} negocios"
                                    },
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 8.dp
                                    ),
                                    color = VerdePet,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }

                        Spacer(Modifier.height(18.dp))

                        if (uiState.businesses.isEmpty()) {
                            MensajeNegocios(
                                titulo = "Aún no tienes negocios",
                                descripcion = "Los establecimientos registrados a tu nombre aparecerán aquí."
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                contentPadding = PaddingValues(
                                    bottom = 24.dp
                                )
                            ) {
                                items(
                                    items = uiState.businesses,
                                    key = { it.id }
                                ) { business ->

                                    TarjetaMiNegocio(
                                        business = business,
                                        onClick = {
                                            onBusinessSelected(business)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }

        // ==========================================
        // MENSAJE DE CONFIRMACIÓN
        // ==========================================

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 90.dp
                )
        )

        // ==========================================
        // BOTÓN PARA REGISTRAR NEGOCIOS
        // ==========================================

        FloatingActionButton(
            onClick = onAddBusiness,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            containerColor = Color(0xFFE0654F),
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// ==============================================
// TARJETA INDIVIDUAL DEL NEGOCIO
// ==============================================

@Composable
private fun TarjetaMiNegocio(
    business: Business,
    onClick: () -> Unit
) {

    val estadoColor = when (business.estadoVerificacion) {
        "APROBADO" -> Color(0xFF2E7D46)
        "PENDIENTE" -> Color(0xFFC9871F)
        else -> Color(0xFFB3261E)
    }

    val estadoFondo = when (business.estadoVerificacion) {
        "APROBADO" -> Color(0xFFE6F5EA)
        "PENDIENTE" -> Color(0xFFFFF3DC)
        else -> Color(0xFFFCE7E5)
    }

    val estadoTexto = when (business.estadoVerificacion) {
        "APROBADO" -> "Aprobado"
        "PENDIENTE" -> "Pendiente de aprobación"
        "RECHAZADO" -> "Rechazado"
        else -> business.estadoVerificacion
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Icono ilustrativo del establecimiento.
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            Color(0xFFE5F3F0),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🏪",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = business.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoPet,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = business.categoria.replaceFirstChar {
                            it.uppercase()
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF71817E)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(
                color = Color(0xFFEDEFEA)
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = "📍  ${business.direccion}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF667571)
            )

            Spacer(Modifier.height(14.dp))

            Surface(
                color = estadoFondo,
                shape = CircleShape
            ) {
                Text(
                    text = estadoTexto,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 7.dp
                    ),
                    color = estadoColor,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

// ==============================================
// MENSAJE PARA ESTADOS VACÍOS O ERRORES
// ==============================================

@Composable
private fun MensajeNegocios(
    titulo: String,
    descripcion: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🏪",
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextoPet
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}
