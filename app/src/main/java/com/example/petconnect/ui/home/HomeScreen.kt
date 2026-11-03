package com.example.petconnect.ui.home

import com.example.petconnect.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.petconnect.data.model.UserAddress
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.petconnect.viewmodel.BusinessViewModel
import com.example.petconnect.data.model.Business
import android.location.Location
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import java.util.Locale
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Close
import com.google.common.math.LinearTransformation.vertical
import java.text.Normalizer
import androidx.compose.material3.Surface
import com.example.petconnect.data.model.petConnectCategories

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    onViewAllCategories: () -> Unit,
    onCategorySelected: (String) -> Unit,
    nombreUsuario: String,
    onAddressesClick: () -> Unit = {},
    direccionActiva: UserAddress? = null,
    onBusinessSelected: (String) -> Unit = {},
    onViewAllFeatured: () -> Unit = {}
) {
    var textoBusqueda by remember { mutableStateOf("") }

    // ViewModel exclusivo para los servicios destacados de Inicio.
    val homeBusinessViewModel: BusinessViewModel = viewModel(
        key = "home_featured_businesses"
    )

    val homeBusinessUiState by homeBusinessViewModel.uiState.collectAsState()

    // Consultamos los establecimientos aprobados.
    LaunchedEffect(homeBusinessViewModel) {
        homeBusinessViewModel.cargarNegociosAprobados()
    }

    // Los servicios destacados se ordenan por calificación.
    val serviciosDestacados = homeBusinessUiState.businesses
        .filter { business ->
            business.estadoVerificacion == "APROBADO"
        }
        .sortedByDescending { business ->
            business.calificacion
        }

    val textoNormalizado = normalizarBusqueda(textoBusqueda)

    val resultadosBusqueda = serviciosDestacados.filter { negocio ->

        val nombre = normalizarBusqueda(negocio.nombre)
        val categoria = normalizarBusqueda(negocio.categoria)
        val descripcion = normalizarBusqueda(negocio.descripcion)

        nombre.contains(textoNormalizado) ||
                categoria.contains(textoNormalizado) ||
                descripcion.contains(textoNormalizado)
    }

    val buscando = textoBusqueda.isNotBlank()

    // Especialistas aprobados registrados en PetConnect.
    val especialistasDestacados = serviciosDestacados
        .filter { negocio ->
            negocio.categoria.trim().lowercase() == "especialistas"
        }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F6))
    ) {

        // ============================================================
        // HEADER
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFAF9F6),
                    shape = RoundedCornerShape(
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp
                    )
                )
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 45.dp,
                    bottom = 20.dp
                )
        ) {

            // --------------------------------------------------------
            // UBICACIÓN + BOTONES SUPERIORES
            // --------------------------------------------------------


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Toda la zona de ubicación abre Direcciones guardadas.
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onAddressesClick() },
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    Text(
                        text = direccionActiva?.direccion
                            ?: "Selecciona una dirección",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8A9690),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = direccionActiva?.nombre ?: "Mi ubicación",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF344A43),
                            maxLines = 1
                        )

                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF227C78)
                        )

                        Text(
                            text = "· ${direccionActiva?.ciudad ?: "Sin municipio"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF8A9690),
                            maxLines = 1
                        )
                    }
                }

                // Reservado para futuras notificaciones.
                IconButton(
                    onClick = {
                        // Futuras notificaciones.
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.NotificationsNone,
                        contentDescription = "Notificaciones",
                        tint = Color(0xFF227C78),
                        modifier = Modifier.size(21.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --------------------------------------------------------
            // BUSCADOR
            // --------------------------------------------------------


            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Buscar veterinarias, paseadores...",
                        maxLines = 1
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar servicios",
                        tint = Color(0xFF227C78),
                        modifier = Modifier.size(21.dp)
                    )
                },

                trailingIcon = {
                    if (textoBusqueda.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                textoBusqueda = ""
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar búsqueda",
                                tint = Color(0xFF227C78)
                            )
                        }
                    }
                },

                singleLine = true,
                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF227C78),
                    unfocusedBorderColor = Color(0xFFE5E9E7),
                    focusedTextColor = Color(0xFF303534),
                    unfocusedTextColor = Color(0xFF303534),
                    focusedPlaceholderColor = Color(0xFF909995),
                    unfocusedPlaceholderColor = Color(0xFF909995),
                    cursorColor = Color(0xFF227C78)
                )
            )
        }

        // ============================================================
        // CONTENIDO PRINCIPAL
        // ============================================================
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {

            if (buscando) {

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Resultados de búsqueda",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF303534)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = Color(0xFFB9E7E4),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "${resultadosBusqueda.size} encontrados",
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF227C78)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    homeBusinessUiState.isLoading -> {
                        CircularProgressIndicator(
                            color = Color(0xFF227C78)
                        )
                    }

                    homeBusinessUiState.errorMessage != null -> {
                        Text(
                            text = "No se pudieron cargar los negocios.",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    resultadosBusqueda.isEmpty() -> {
                        Text(
                            text = "No encontramos negocios con ese nombre o categoría.",
                            color = Color(0xFF71817E)
                        )
                    }

                    else -> {
                        resultadosBusqueda.forEach { negocio ->
                            val categoria = petConnectCategories.find {
                                it.id == negocio.categoria
                            }

                            val nombreCategoria = categoria?.nombre ?: negocio.categoria

                            val imagenCategoria = when (negocio.categoria) {
                                "veterinaria" -> R.drawable.cat_veterinaria
                                "peluqueria" -> R.drawable.cat_peluqueria
                                "bano" -> R.drawable.cat_bano
                                "guarderia" -> R.drawable.cat_guarderia
                                "paseadores" -> R.drawable.cat_paseadores
                                "adiestramiento" -> R.drawable.cat_adiestramiento
                                "parques" -> R.drawable.cat_parques
                                "tiendas" -> R.drawable.cat_tienda
                                "especialistas" -> R.drawable.cat_especialistas
                                "hoteles_petfriendly" -> R.drawable.cat_hotelpet
                                else -> R.drawable.cat_veterinaria
                            }

                            Card(
                                onClick = {
                                    onBusinessSelected(negocio.id)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                shape = RoundedCornerShape(16.dp),
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
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {

                                    // IMAGEN DE LA CATEGORÍA
                                    Box(
                                        modifier = Modifier
                                            .size(62.dp)
                                            .background(
                                                color = Color(0xFFE3F2EF),
                                                shape = RoundedCornerShape(14.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = imagenCategoria),
                                            contentDescription = nombreCategoria,
                                            modifier = Modifier.size(52.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    }

                                    // DATOS DEL NEGOCIO
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(7.dp)
                                    ) {

                                        Text(
                                            text = negocio.nombre,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF303534),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Surface(
                                            shape = RoundedCornerShape(50.dp),
                                            color = Color(0xFFB9E7E4)
                                        ) {
                                            Text(
                                                text = nombreCategoria,
                                                modifier = Modifier.padding(
                                                    horizontal = 10.dp,
                                                    vertical = 4.dp
                                                ),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFF227C78)
                                            )
                                        }

                                        Text(
                                            text = distanciaDesdeDireccion(
                                                direccion = direccionActiva,
                                                negocio = negocio
                                            ),
                                            color = Color(0xFF71817E),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }

                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(110.dp))

            } else {

                // Aquí permanece el contenido habitual de Inicio:
                // Categorías, Servicios destacados y Especialistas destacados.


            // ========================================================
            // CATEGORÍAS
            // ========================================================
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Categorías",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF303534)
                )
                Text(
                    text = "Ver todas",
                    modifier = Modifier.clickable {
                        onViewAllCategories()
                    },
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            val categoriasInicio = listOf(
                Triple("veterinaria", "Veterinaria", R.drawable.cat_veterinaria),
                Triple("peluqueria", "Peluquería", R.drawable.cat_peluqueria),
                Triple("bano", "Baño", R.drawable.cat_bano),
                Triple("guarderia", "Guardería", R.drawable.cat_guarderia),
                Triple("paseadores", "Paseadores", R.drawable.cat_paseadores),
                Triple("adiestramiento", "Adiestramiento", R.drawable.cat_adiestramiento),
                Triple("parques", "Parques y jardines", R.drawable.cat_parques),
                Triple("tiendas", "Tiendas", R.drawable.cat_tienda)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(end = 8.dp)
            ) {
                items(categoriasInicio) { (id, nombre, imagen) ->
                    HomeCategoryCard(
                        nombre = nombre,
                        imagen = imagen,
                        onClick = {
                            onCategorySelected(id)
                        }
                    )
                }
            }
                // ========================================================
                // SERVICIOS DESTACADOS
                // ========================================================
                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Servicios destacados",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF303534)
                    )

                    Text(
                        text = "Ver todos",
                        modifier = Modifier.clickable {
                            onViewAllFeatured()
                        },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                }
                Spacer(modifier = Modifier.height(12.dp))
            if (homeBusinessUiState.isLoading) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            } else if (homeBusinessUiState.errorMessage != null) {

                Text(
                    text = "No se pudieron cargar los servicios destacados.",
                    color = MaterialTheme.colorScheme.error
                )

            } else if (serviciosDestacados.isEmpty()) {

                Text(
                    text = "Todavía no hay servicios destacados disponibles.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            } else {

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                serviciosDestacados.forEach { negocio ->

                        ServiceCard(
                            emoji = when (
                                negocio.categoria.trim().lowercase()
                            ) {
                                "veterinaria", "veterinarias" -> "🏥"
                                "peluqueria", "peluquería", "baño" -> "✂️"
                                "paseador", "paseadores" -> "🐕"
                                "guarderia", "guardería" -> "🏨"
                                else -> "🐾"
                            },
                            name = negocio.nombre,
                            category = negocio.categoria,
                            distance = distanciaDesdeDireccion(
                                direccion = direccionActiva,
                                negocio = negocio
                            ),
                            rating = negocio.calificacion.toString(),
                            onClick = {
                                onBusinessSelected(negocio.id)
                            }
                        )
                    }
                }
            }
                // ========================================================
                // ESPECIALISTAS DESTACADOS
                // ========================================================

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Especialistas destacados",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF303534)
                    )

                    Text(
                        text = "Ver más",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))


            if (homeBusinessUiState.isLoading) {

                CircularProgressIndicator(
                    color = Color(0xFF227C78)
                )

            } else if (homeBusinessUiState.errorMessage != null) {

                Text(
                    text = "No se pudieron cargar los especialistas.",
                    color = MaterialTheme.colorScheme.error
                )

            } else if (especialistasDestacados.isEmpty()) {

                Text(
                    text = "Todavía no hay especialistas destacados disponibles.",
                    color = Color(0xFF71817E),
                    style = MaterialTheme.typography.bodyMedium
                )

            } else {

                especialistasDestacados.forEach { especialista ->

                    ServiceCard(
                        emoji = "👩‍⚕️",
                        name = especialista.nombre,
                        category = "Especialistas",
                        distance = distanciaDesdeDireccion(
                            direccion = direccionActiva,
                            negocio = especialista
                        ),
                        rating = especialista.calificacion.toString(),
                        onClick = {
                            onBusinessSelected(especialista.id)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
}
            }
        }
    }


// ====================================================================
// COMPONENTE: CATEGORÍA
// ====================================================================

@Composable
private fun CategoryChip(
    icon: String,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surface
        ),
        border =
            if (!selected)
                androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant
                )
            else
                null
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 10.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = icon)

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = text,
                color =
                    if (selected)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


// ====================================================================
// COMPONENTE: TARJETA DE SERVICIO
// ====================================================================

@Composable
private fun ServiceCard(
    emoji: String,
    name: String,
    category: String,
    distance: String,
    rating: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.width(250.dp)
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 1.dp
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Color(0xFFE3F2EF)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = emoji,
                    style = MaterialTheme.typography.displaySmall
                )
            }

            Column(
                modifier = Modifier.padding(14.dp)
            ) {

                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$category · $distance",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "⭐ $rating",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


// ====================================================================
// COMPONENTE: RECOMENDACIÓN
// ====================================================================

@Composable
private fun RecommendationCard(
    emoji: String,
    name: String,
    category: String,
    distance: String,
    rating: String,
    price: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = emoji,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "$category · $distance",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = "⭐ $rating",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = price,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


// ====================================================================
// NAVEGACIÓN INFERIOR
// ====================================================================

@Composable
private fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onMapClick: () -> Unit,
    onPetsClick: () -> Unit,
    onAiClick: () -> Unit,
    onProfileClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            NavigationItem(
                icon = "🏠",
                label = "Inicio",
                selected = true,
                onClick = onHomeClick
            )

            NavigationItem(
                icon = "🗺️",
                label = "Mapa",
                selected = false,
                onClick = onMapClick
            )

            NavigationItem(
                icon = "🐾",
                label = "Mascotas",
                selected = false,
                onClick = onPetsClick
            )

            NavigationItem(
                icon = "✨",
                label = "IA",
                selected = false,
                onClick = onAiClick
            )

            NavigationItem(
                icon = "👤",
                label = "Perfil",
                selected = false,
                onClick = onProfileClick
            )
        }
    }
}


// ====================================================================
// COMPONENTE: ITEM DE NAVEGACIÓN
// ====================================================================

@Composable
private fun NavigationItem(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {

            Text(text = icon)

            Text(
                text = label,
                color =
                    if (selected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium,
                fontWeight =
                    if (selected)
                        FontWeight.Bold
                    else
                        FontWeight.Normal
            )
        }
    }
}


/**
 * Calcula la distancia en línea recta
 * desde la dirección activa hasta un negocio.
 */
private fun distanciaDesdeDireccion(
    direccion: UserAddress?,
    negocio: Business
): String {

    if (direccion == null) {
        return "Sin ubicación"
    }

    if (
        !negocio.latitud.isFinite() ||
        !negocio.longitud.isFinite() ||
        negocio.latitud !in -90.0..90.0 ||
        negocio.longitud !in -180.0..180.0 ||
        (negocio.latitud == 0.0 && negocio.longitud == 0.0)
    ) {
        return "Sin distancia"
    }

    val resultado = FloatArray(1)

    Location.distanceBetween(
        direccion.latitud,
        direccion.longitud,
        negocio.latitud,
        negocio.longitud,
        resultado
    )

    val metros = resultado[0]

    return if (metros < 1000f) {
        "${metros.toInt()} m"
    } else {
        String.format(
            Locale("es", "CO"),
            "%.1f km",
            metros / 1000f
        )
    }
}

@Composable
private fun HomeCategoryCard(
    nombre: String,
    imagen: Int,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(132.dp)
            .height(142.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imagen),
                contentDescription = nombre,
                modifier = Modifier.size(88.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = nombre,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

private fun normalizarBusqueda(texto: String): String {
    return Normalizer.normalize(
        texto.trim().lowercase(Locale.ROOT),
        Normalizer.Form.NFD
    ).replace(Regex("\\p{M}+"), "")
}
