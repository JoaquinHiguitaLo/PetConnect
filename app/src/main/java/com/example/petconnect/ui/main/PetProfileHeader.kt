
package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petconnect.data.model.Pet
import com.example.petconnect.ui.components.PetConnectTopBar

@Composable
fun PetProfileHeader(
    mascota: Pet,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onPhotoClick: (() -> Unit)? = null
) {
    val verde = Color(0xFF227C78)

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        PetConnectTopBar(
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = mascota.nombre,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = verde
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .size(
                        width = 180.dp,
                        height = 130.dp
                    )
                    .background(
                        color = Color(0xFFB9E7E4),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .then(
                        if (onPhotoClick != null) {
                            Modifier.clickable {
                                onPhotoClick()
                            }
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (mascota.especie.lowercase()) {
                        "perro" -> "🐕"
                        "gato" -> "🐈"
                        "conejo" -> "🐇"
                        "ave" -> "🐦"
                        else -> "🐾"
                    },
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}
