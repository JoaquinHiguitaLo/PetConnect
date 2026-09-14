package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F6))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ============================================================
        // ENCABEZADO
        // ============================================================

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "MI PERFIL",
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF227C78),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))


        // ============================================================
        // FOTO / AVATAR
        // ============================================================

        Box(
            modifier = Modifier
                .size(82.dp)
                .background(
                    color = Color(0xFFB5E3E1),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🐾",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(10.dp))


        // ============================================================
        // INFORMACIÓN PRINCIPAL
        // ============================================================

        Text(
            text = "Manuela",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "correo@email.com",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(28.dp))


        // ============================================================
        // INFORMACIÓN PERSONAL
        // ============================================================

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Información personal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
            ) {

                ProfileDataItem(
                    label = "Nombre completo",
                    value = "Manuela"
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color(0xFFD8D8D8)
                )

                ProfileDataItem(
                    label = "Correo electrónico",
                    value = "correo@email.com"
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color(0xFFD8D8D8)
                )

                ProfileDataItem(
                    label = "Teléfono",
                    value = "300 000 0000"
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))


        // ============================================================
        // BOTÓN EDITAR PERFIL
        // ============================================================

        Button(
            onClick = {
                // Por ahora solo dejamos el botón visual.
                // Más adelante aquí abriremos la edición del perfil.
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF227C78)
            )
        ) {
            Text(
                text = "Editar perfil",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))


        // ============================================================
        // CERRAR SESIÓN
        // ============================================================

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFFFFD8D2),
                contentColor = Color(0xFF8B1E14)
            )
        ) {
            Text(
                text = "Cerrar sesión",
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// ================================================================
// COMPONENTE REUTILIZABLE PARA LOS DATOS DEL PERFIL
// ================================================================

@Composable
private fun ProfileDataItem(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}