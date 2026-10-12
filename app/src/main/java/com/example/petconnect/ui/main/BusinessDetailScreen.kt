package com.example.petconnect.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petconnect.data.model.petConnectCategories
import com.example.petconnect.viewmodel.BusinessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessDetailScreen(
    businessId: String,
    onBack: () -> Unit,
    viewModel: BusinessViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargar el negocio seleccionado desde Firestore.
    LaunchedEffect(businessId) {
        viewModel.cargarNegocioPorId(businessId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del negocio") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage
                            ?: "No se pudo cargar el negocio.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            uiState.selectedBusiness == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontró la información del negocio.")
                }
            }

            else -> {
                val business = uiState.selectedBusiness!!
                val categoryName = petConnectCategories
                    .find { it.id == business.categoria }
                    ?.nombre ?: business.categoria

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = business.nombre,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    HorizontalDivider()

                    DetailItem(
                        title = "Categoría",
                        value = categoryName
                    )

                    DetailItem(
                        title = "Descripción",
                        value = business.descripcion.ifBlank {
                            "Sin descripción disponible."
                        }
                    )

                    DetailItem(
                        title = "Dirección",
                        value = business.direccion.ifBlank {
                            "Sin dirección registrada."
                        }
                    )

                    DetailItem(
                        title = "Teléfono",
                        value = business.telefono.ifBlank {
                            "Sin teléfono registrado."
                        }
                    )

                    DetailItem(
                        title = "Calificación",
                        value = if (business.calificacion > 0) {
                            "⭐ ${"%.1f".format(business.calificacion)}"
                        } else {
                            "Sin calificaciones"
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailItem(
    title: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
