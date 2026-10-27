
package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Encabezado reutilizable para las pantallas
 * de administración del prestador.
 */
@Composable
fun BusinessHeader(
    titulo: String,
    subtitulo: String,
    onBack: () -> Unit,
    backEnabled: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF1F6F6B),
                shape = RoundedCornerShape(
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            )
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 12.dp,
                bottom = 24.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                onClick = onBack,
                enabled = backEnabled,
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        Color.White.copy(alpha = 0.14f),
                        RoundedCornerShape(14.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }

            Text(
                text = "PANEL DEL PRESTADOR",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFB7E4DE)
            )
        }

        Spacer(Modifier.height(18.dp))

        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = subtitulo,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFB7E4DE)
        )
    }
}
