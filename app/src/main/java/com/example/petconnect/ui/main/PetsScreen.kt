package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petconnect.ui.theme.PetConnectTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petconnect.viewmodel.PetsViewModel
import com.example.petconnect.data.model.Pet

@Composable
fun PetsScreen(
    modifier: Modifier = Modifier,
    onAddPet: () -> Unit = {},
    onEditPet: (Pet) -> Unit = {},
    onDeletePet: (Pet) -> Unit
) {

    // ViewModel encargado de manejar las mascotas.
    val petsViewModel: PetsViewModel = viewModel()

    // Observamos el estado del ViewModel.
    val uiState by petsViewModel.uiState.collectAsState()

    // Cargamos las mascotas cuando se muestra la pantalla.
    LaunchedEffect(Unit) {
        //obtiene el usuario que actualmente tiene sesión iniciada
        val usuarioId =
            com.google.firebase.auth.FirebaseAuth
                .getInstance()
                .currentUser
                ?.uid

        if (usuarioId != null) {
            //pasa ese UID al ViewModel
            petsViewModel.cargarMascotas(usuarioId)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        PetsHeader(
            cantidadMascotas = uiState.pets.size
        )

        PetsContent(
            pets = uiState.pets,
            onAddPet = onAddPet,
            onEditPet = onEditPet,
            onDeletePet = onDeletePet
        )
    }
}

@Composable
private fun PetsHeader(
    cantidadMascotas: Int
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary
            )
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 28.dp,
                bottom = 24.dp
            )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "TUS COMPAÑEROS",
                    color = MaterialTheme.colorScheme.primaryContainer,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Mis Mascotas",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(
                    alpha = 0.35f
                )
            ) {

                Text(
                    text = "•••",
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            SummaryChip(
                text = "$cantidadMascotas mascotas",
                color = MaterialTheme.colorScheme.primaryContainer
            )

            SummaryChip(
                text = "2 vacunas al día ✓",
                color = PetConnectTheme.extraColors.success
            )

            SummaryChip(
                text = "1 pendiente",
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
private fun SummaryChip(
    text: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = color
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 7.dp
            ),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PetsContent(
    pets: List<Pet>,
    onAddPet: () -> Unit,
    onEditPet: (Pet) -> Unit,
    onDeletePet: (Pet) -> Unit
) {
    val petsUi = pets.map { pet ->
        PetUi(
            id = pet.id,
            name = pet.nombre,
            species = pet.especie,
            breed = pet.raza ?: "Sin raza",
            age = "${pet.edad} años",
            size = pet.tamano,
            emoji = when (pet.especie.lowercase()) {
                "perro" -> "🐕"
                "gato" -> "🐈"
                "conejo" -> "🐇"
                "ave" -> "🐦"
                else -> "🐾"
            },
            vaccinated = false
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = 20.dp,
                vertical = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(petsUi) { petUi ->

                val petOriginal = pets.firstOrNull {
                    it.id == petUi.id
                }

                PetCard(
                    pet = petUi,

                    onEditPet = {
                        if (petOriginal != null) {
                            onEditPet(petOriginal)
                        }
                    },

                    onDeletePet = {
                        if (petOriginal != null) {
                            onDeletePet(petOriginal)
                        }
                    }
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(70.dp)
                )
            }
        }

        FloatingActionButton(
            onClick = onAddPet,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ) {
            Text(
                text = "+",
                fontSize = 30.sp
            )
        }
    }
}

private data class PetUi(
    val id: String,
    val name: String,
    val species: String,
    val breed: String,
    val age: String,
    val size: String,
    val emoji: String,
    val vaccinated: Boolean
)

@Composable
private fun PetCard(
    pet: PetUi,
    onEditPet: () -> Unit,
    onDeletePet: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        onClick = {
            expanded = !expanded
        }
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            // =====================================================
            // INFORMACIÓN PRINCIPAL DE LA MASCOTA
            // =====================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Foto / representación de la mascota
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = pet.emoji,
                        fontSize = 38.sp
                    )
                }

                Spacer(
                    modifier = Modifier.width(16.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = pet.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        VaccineBadge(
                            vaccinated = pet.vaccinated
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "${pet.species} · ${pet.breed}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "${pet.age} · ${pet.size}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp
                    )
                }

                // Flecha que indica si la tarjeta está abierta o cerrada
                Text(
                    text = if (expanded) "▴" else "▾",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 18.sp
                )
            }

            // =====================================================
            // INFORMACIÓN ADICIONAL
            // Solo aparece cuando la tarjeta está expandida
            // =====================================================

            if (expanded) {

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    PetInfoBox(
                        title = "Edad",
                        value = pet.age,
                        modifier = Modifier.weight(1f)
                    )

                    PetInfoBox(
                        title = "Tamaño",
                        value = pet.size,
                        modifier = Modifier.weight(1f)
                    )

                    PetInfoBox(
                        title = "Próx. cita",
                        value = "15 Sep",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 4.dp
                        ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    OutlinedButton(
                        onClick = {
                            // Por ahora solamente visual.
                            // Más adelante navegará a servicios.
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Ver servicios")
                    }

                    Button(
                        onClick = onEditPet,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Editar mascota")
                    }
                    IconButton(
                        onClick = onDeletePet,
                        modifier = Modifier
                            .size(45.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFE5E5))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar mascota",
                            tint = Color(0xFFE53935)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }
        }
    }
}

@Composable
private fun PetInfoBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 12.dp,
                    horizontal = 8.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
private fun VaccineBadge(
    vaccinated: Boolean
) {

    Surface(
        shape = RoundedCornerShape(50.dp),
        color = if (vaccinated) {
            PetConnectTheme.extraColors.successContainer
        } else {
            MaterialTheme.colorScheme.errorContainer
        }
    ) {

        Text(
            text = if (vaccinated) {
                "✓ Vacunas"
            } else {
                "⚠ Pendiente"
            },
            modifier = Modifier.padding(
                horizontal = 9.dp,
                vertical = 5.dp
            ),
            color = if (vaccinated) {
                PetConnectTheme.extraColors.success
            } else {
                MaterialTheme.colorScheme.error
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ============================================================
// FLUJO DE EDICIÓN DE MASCOTAS
// ============================================================
// PetCard no controla directamente la navegación.
//
// Cuando el usuario pulsa "Editar mascota", PetCard ejecuta
// un callback (onEditPet) y devuelve la mascota seleccionada.
//
// PetsScreen/MainScreen se encargan posteriormente de decidir
// qué pantalla abrir.
//
// Conservamos el ID de Firestore porque una actualización
// necesita identificar exactamente qué documento modificar.
//
// Flujo:
//
// PetCard
//    ↓
// onEditPet(Pet)
//    ↓
// MainScreen
//    ↓
// EditPetScreen
//    ↓
// PetsViewModel
//    ↓
// PetRepository
//    ↓
// Firestore
// ============================================================