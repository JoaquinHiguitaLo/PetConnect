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
import android.net.Uri
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pets
import java.text.Normalizer
import java.util.Locale
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight


private fun normalizarTexto(texto: String): String {
    val textoNormalizado = Normalizer.normalize(
        texto.trim().lowercase(Locale.ROOT),
        Normalizer.Form.NFD
    )

    return textoNormalizado.replace(
        Regex("\\p{M}+"),
        ""
    )
}


@Composable
fun PetsScreen(
    modifier: Modifier = Modifier,
    onAddPet: () -> Unit = {},
    onEditPet: (Pet) -> Unit = {},
    onDeletePet: (Pet) -> Unit,
    onViewVaccines: (Pet) -> Unit = {},
    onPetSelected: (Pet) -> Unit = {}
) {

    // ViewModel encargado de manejar las mascotas.
    val petsViewModel: PetsViewModel = viewModel()

    // Observamos el estado del ViewModel.
    val uiState by petsViewModel.uiState.collectAsState()

    var buscadorVisible by remember {
        mutableStateOf(false)
    }

    var textoBusqueda by remember {
        mutableStateOf("")
    }

    val mascotasFiltradas = remember(
        uiState.pets,
        textoBusqueda
    ) {
        val consulta = normalizarTexto(textoBusqueda)

        if (consulta.isBlank()) {
            uiState.pets
        } else {
            uiState.pets.filter { mascota ->

                val nombre = normalizarTexto(mascota.nombre)
                val especie = normalizarTexto(mascota.especie)
                val raza = normalizarTexto(mascota.raza ?: "")

                nombre.contains(consulta) ||
                        especie.contains(consulta) ||
                        raza.contains(consulta)
            }
        }
    }


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

        PetsContent(
            pets = mascotasFiltradas,
            cantidadTotalMascotas = uiState.pets.size,
            buscadorVisible = buscadorVisible,
            textoBusqueda = textoBusqueda,

            onToggleSearch = {
                buscadorVisible = !buscadorVisible

                if (!buscadorVisible) {
                    textoBusqueda = ""
                }
            },

            onQueryChange = { nuevaBusqueda ->
                textoBusqueda = nuevaBusqueda
            },

            onAddPet = onAddPet,
            onEditPet = onEditPet,
            onDeletePet = onDeletePet,
            onViewVaccines = onViewVaccines,
            onPetSelected = onPetSelected
        )

    }
}



@Composable
private fun PetsHeader(
    cantidadMascotas: Int,
    buscadorVisible: Boolean,
    textoBusqueda: String,
    onToggleSearch: () -> Unit,
    onQueryChange: (String) -> Unit,
    onAddPet: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 12.dp
            )
    ) {

        Text(
            text = "Mascotas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Tus compañeros de aventuras",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = RoundedCornerShape(50.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = if (cantidadMascotas == 1) {
                        "1 mascota"
                    } else {
                        "$cantidadMascotas mascotas"
                    },
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 9.dp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick =  onToggleSearch
                ) {
                    Icon(
                        imageVector = if (buscadorVisible) {
                            Icons.Default.Close
                        } else {
                            Icons.Default.Search
                        },
                        contentDescription = if (buscadorVisible) {
                            "Cerrar búsqueda"
                        } else {
                            "Buscar mascotas"
                        },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                FilledIconButton(
                    onClick = onAddPet,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor =
                            MaterialTheme.colorScheme.primary,
                        contentColor =
                            MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar mascota"
                    )
                }
            }
        }

        if (buscadorVisible) {

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Buscar por nombre o raza...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
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
    cantidadTotalMascotas: Int,
    buscadorVisible: Boolean,
    textoBusqueda: String,
    onToggleSearch: () -> Unit,
    onQueryChange: (String) -> Unit,
    onAddPet: () -> Unit,
    onEditPet: (Pet) -> Unit,
    onDeletePet: (Pet) -> Unit,
    onViewVaccines: (Pet) -> Unit,
    onPetSelected: (Pet) -> Unit
) {


    // Fotografías temporales por ID de mascota.
    val fotosMascotas = remember {
        mutableStateMapOf<String, Uri>()
    }

    // Mascota a la que estamos cambiando la foto.
    var mascotaSeleccionadaId by remember {
        mutableStateOf<String?>(null)
    }

    // Selector de imágenes de Android.
    val selectorFotoMascota = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        val mascotaId = mascotaSeleccionadaId

        if (uri != null && mascotaId != null) {
            fotosMascotas[mascotaId] = uri
        }

        mascotaSeleccionadaId = null
    }

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
                start = 20.dp,
                end = 20.dp,
                top = 0.dp,
                bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                PetsHeader(
                    cantidadMascotas = cantidadTotalMascotas,
                    buscadorVisible = buscadorVisible,
                    textoBusqueda = textoBusqueda,
                    onToggleSearch = onToggleSearch,
                    onQueryChange = onQueryChange,
                    onAddPet = onAddPet
                )
            }

            items(
                items = petsUi,
                key = { it.id }
            ) { petUi ->

            val petOriginal = pets.firstOrNull {
                    it.id == petUi.id
                }


                PetCard(
                    pet = petUi,

                    fotoUri = fotosMascotas[petUi.id],

                    onSelectPhoto = {
                        mascotaSeleccionadaId = petUi.id
                        selectorFotoMascota.launch("image/*")
                    },

                    onEditPet = {
                        if (petOriginal != null) {
                            onEditPet(petOriginal)
                        }
                    },

                    onDeletePet = {
                        if (petOriginal != null) {
                            onDeletePet(petOriginal)
                        }
                    },

                    onViewVaccines = {
                        petOriginal?.let { mascota ->
                            onViewVaccines(mascota)
                        }
                    },

                    onOpenProfile = {
                        petOriginal?.let { mascota ->
                            onPetSelected(mascota)
                        }
                    }

                )

            }

            if (pets.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "🐾",
                            fontSize = 36.sp
                        )

                        Text(
                            text = if (textoBusqueda.isNotBlank()) {
                                "No encontramos mascotas"
                            } else {
                                "Todavía no tienes mascotas"
                            },
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = if (textoBusqueda.isNotBlank()) {
                                "Prueba buscando otro nombre, especie o raza."
                            } else {
                                "Agrega tu primera mascota con el botón +."
                            },
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }


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
    fotoUri: Uri?,
    onSelectPhoto: () -> Unit,
    onEditPet: () -> Unit,
    onDeletePet: () -> Unit,
    onViewVaccines: () -> Unit,
    onOpenProfile: () -> Unit
) {
    val context = LocalContext.current

    val fotoBitmap = remember(fotoUri) {
        fotoUri?.let { uri ->
            try {
                context.contentResolver
                    .openInputStream(uri)
                    ?.use { stream ->
                        BitmapFactory.decodeStream(stream)
                    }
            } catch (e: Exception) {
                null
            }
        }
    }

    Card(
        onClick = onOpenProfile,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Avatar
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFB9E7E4)),
                contentAlignment = Alignment.Center
            ) {
                if (fotoBitmap != null) {
                    Image(
                        bitmap = fotoBitmap.asImageBitmap(),
                        contentDescription = "Foto de ${pet.name}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = pet.emoji,
                        fontSize = 30.sp
                    )
                }
            }

            // Información principal
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = pet.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF202020),
                    maxLines = 1
                )

                Text(
                    text = "${pet.species} · ${pet.breed}",
                    fontSize = 13.sp,
                    color = Color(0xFF777777),
                    maxLines = 1
                )

                Text(
                    text = "${pet.age} · ${pet.size}",
                    fontSize = 13.sp,
                    color = Color(0xFF227C78),
                    maxLines = 1
                )
            }

            Icon(
                imageVector =
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Abrir perfil de ${pet.name}",
                tint = Color(0xFF227C78),
                modifier = Modifier.size(24.dp)
            )
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