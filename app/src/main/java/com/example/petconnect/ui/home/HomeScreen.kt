package com.example.petconnect.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    // Igual que en Register/Login: HomeScreen no decide A DÓNDE ir tras el logout,
    // solo avisa que ya cerró sesión. Quien la llama (la navegación) decide el destino.
    onLogout: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¡Bienvenido a PetConnect!",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(8.dp))

        // Mostramos el correo del usuario logueado, si existe
        viewModel.currentUserEmail?.let { email ->
            Text(text = email, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(32.dp))

        Button(onClick = {
            viewModel.onLogoutClick()
            onLogout() // avisamos hacia afuera que ya se cerró sesión
        }) {
            Text("Cerrar sesión")
        }
    }
}