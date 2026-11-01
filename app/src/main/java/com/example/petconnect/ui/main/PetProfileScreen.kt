
package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petconnect.data.model.Pet
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.petconnect.R
import com.example.petconnect.ui.main.PetProfileHeader

@Composable
fun PetProfileScreen(
    mascota: Pet,
    onBack: () -> Unit,
    onInformation: () -> Unit,
    onVaccines: () -> Unit,
    modifier: Modifier = Modifier
) {
    val verde = Color(0xFF227C78)

    val opciones = listOf(
        Triple("Información", R.drawable.pet_info, true),
        Triple("Recordatorios", R.drawable.pet_recordatorios, false),
        Triple("Carnet de vacunas", R.drawable.pet_vacunas, true),
        Triple("Citas", R.drawable.pet_citas, false),
        Triple("Historial médico", R.drawable.pet_historial, false),
        Triple("Medicamentos", R.drawable.pet_medicamentos, false),
        Triple("Baños", R.drawable.pet_banos, false),
        Triple("Alimentación", R.drawable.pet_alimentacion, false)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 0.dp,
                end = 0.dp,
                bottom = 48.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PetProfileHeader(
                    mascota = mascota,
                    onBack = onBack
                )
            }

            items(4) { fila ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    for (columna in 0..1) {
                        val indice = fila * 2 + columna
                        val (titulo, icono, disponible) = opciones[indice]

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(116.dp)
                                .clickable(enabled = disponible) {
                                    when (titulo) {
                                        "Información" -> onInformation()
                                        "Carnet de vacunas" -> onVaccines()
                                    }
                                },
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(16.dp)
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = icono),
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    contentScale = ContentScale.Fit,
                                    alpha = if (disponible) 1f else 0.5f
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = titulo,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = if (disponible) {
                                        MaterialTheme.colorScheme.onSurface
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                    maxLines = 1
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}
