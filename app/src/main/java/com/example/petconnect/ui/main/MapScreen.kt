package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petconnect.viewmodel.BusinessViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import java.text.Normalizer
import java.util.Locale
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.petconnect.utils.LocationUtils
import org.maplibre.android.geometry.LatLng
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.location.Location
import org.maplibre.android.camera.CameraPosition
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.text.style.TextAlign
import com.example.petconnect.data.model.Category
import androidx.compose.ui.text.style.TextOverflow
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    ubicacionGuardada: LatLng? = null,
    onUbicacionActualizada: (LatLng) -> Unit = {},
    posicionCamaraGuardada: CameraPosition? = null,
    onPosicionCamaraActualizada: (CameraPosition) -> Unit = {},
    onBusinessSelected: (String) -> Unit = {}
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    // Coordenadas a las que queremos mover el mapa.
    var ubicacionObjetivo by remember(ubicacionGuardada) {
        mutableStateOf(ubicacionGuardada)
    }
    var solicitudCentrado by remember {
        mutableIntStateOf(0)
    }
    // Mensaje que aparecerá si el usuario rechaza el permiso.
    var mensajeUbicacion by remember {
        mutableStateOf<String?>(null)
    }


    fun buscarMiUbicacion() {

        // Evitamos solicitar la ubicación sin permiso.
        if (!LocationUtils.tienePermiso(context)) {
            mensajeUbicacion = "No tienes permisos de ubicación."
            return
        }

        mensajeUbicacion = "Obteniendo ubicación actual..."

        scope.launch {

            // Solicitamos una posición actual al dispositivo.
            val ubicacion = LocationUtils.obtenerUbicacionActual(
                context
            )

            if (ubicacion != null) {

                val latitud = ubicacion.latitude
                val longitud = ubicacion.longitude

                // Actualizamos las coordenadas objetivo.
                val nuevaUbicacion = LatLng(
                    latitud,
                    longitud
                )

                ubicacionObjetivo = nuevaUbicacion

                // También la guardamos en MainScreen.
                onUbicacionActualizada(nuevaUbicacion)
                solicitudCentrado++
                // Mostramos las coordenadas para comprobarlas.
                mensajeUbicacion =
                    "Ubicación: $latitud, $longitud"

            } else {

                mensajeUbicacion =
                    "No se pudo obtener una ubicación actual. " +
                            "Verifica el GPS del emulador."
            }
        }
    }




    // Comprueba si tenemos ubicación precisa o aproximada.
    fun permisoUbicacionConcedido(): Boolean {
        val permisoPreciso = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val permisoAproximado = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return permisoPreciso || permisoAproximado
    }

    // Solicitud de permisos mediante el diálogo de Android.
    val solicitarPermisosUbicacion = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->

        val preciso = permisos[
            Manifest.permission.ACCESS_FINE_LOCATION
        ] == true

        val aproximado = permisos[
            Manifest.permission.ACCESS_COARSE_LOCATION
        ] == true

        if (preciso || aproximado) {

            // El usuario autorizó la ubicación.
            buscarMiUbicacion()

        } else {

            mensajeUbicacion =
                "No se concedió el permiso de ubicación."
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F8F4))
    ) {
        // ViewModel exclusivo para los negocios del mapa.
        // No interfiere con las consultas de otras pantallas.
        val mapBusinessViewModel: BusinessViewModel = viewModel(
            key = "map_businesses"
        )

        // Observamos los establecimientos obtenidos de Firestore.
        val mapBusinessUiState by mapBusinessViewModel.uiState.collectAsState()

        // Consultamos los negocios aprobados al entrar al mapa.
        LaunchedEffect(Unit) {
            mapBusinessViewModel.cargarNegociosAprobados()
        }

        // Lista disponible para dibujar marcadores posteriormente.
        val negociosAprobados = mapBusinessUiState.businesses

        // Categoría activa en el mapa.
        var categoriaSeleccionada by remember {
            mutableStateOf("Todos")
        }
        // Texto que escribe el usuario en el buscador.
        var textoBusqueda by remember {
            mutableStateOf("")
        }
        // Controla cómo se organizan las tarjetas.
        var ordenSeleccionado by remember {
            mutableStateOf("Cercanía")
        }


    // Filtramos los establecimientos según la categoría seleccionada.

        val negociosFiltrados = negociosAprobados.filter { business ->

            val categoriaNegocio =
                normalizarCategoria(business.categoria)

            val coincideCategoria = when (categoriaSeleccionada) {

                "Todos" -> true

                "Veterinaria" ->
                    categoriaNegocio in listOf(
                        "veterinaria",
                        "veterinarias"
                    )

                "Peluquería" ->
                    categoriaNegocio in listOf(
                        "peluqueria",
                        "peluquerias",
                        "bano",
                        "bano y peluqueria"
                    )

                "Paseador" ->
                    categoriaNegocio in listOf(
                        "paseador",
                        "paseadores",
                        "paseos"
                    )

                "Guardería" ->
                    categoriaNegocio in listOf(
                        "guarderia",
                        "guarderias"
                    )

                else -> false
            }

            // Normalizamos para ignorar mayúsculas y tildes.
            val busqueda = normalizarCategoria(textoBusqueda)

            val coincideBusqueda =
                busqueda.isBlank() ||
                        normalizarCategoria(business.nombre).contains(busqueda)

            // Debe cumplir ambas condiciones.
            coincideCategoria && coincideBusqueda
        }

        // Ordenamos los negocios sin modificar los resultados
        // originales de Firestore.
        val negociosOrdenados = when (ordenSeleccionado) {

            "Cercanía" -> {

                if (ubicacionObjetivo != null) {

                    negociosFiltrados.sortedBy { business ->
                        obtenerDistanciaEnMetros(
                            ubicacionUsuario = ubicacionObjetivo,
                            latitudNegocio = business.latitud,
                            longitudNegocio = business.longitud
                        )
                    }

                } else {
                    // Sin ubicación no podemos ordenar por distancia.
                    negociosFiltrados
                }
            }

            "Calificación" -> {
                negociosFiltrados.sortedByDescending {
                    it.calificacion
                }
            }

            else -> negociosFiltrados
        }


        // ============================================================
        // BUSCADOR Y FILTROS
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9F8F4))
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 16.dp,
                    bottom = 12.dp
                )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { nuevoTexto ->
                        textoBusqueda = nuevoTexto
                    },
                    placeholder = {
                        Text(
                            text = "Buscar en el mapa...",
                            color = Color(0xFF8A8A8A)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color(0xFF227A76)
                    )
                )

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtros",
                        tint = Color(0xFF227A76)
                    )
                }
            }


            Spacer(modifier = Modifier.height(12.dp))

            // ========================================================
            // CATEGORÍAS
            // ========================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {


                MapCategoryChip(
                    text = "Todos",
                    selected = categoriaSeleccionada == "Todos",
                    onClick = {
                        categoriaSeleccionada = "Todos"
                    }
                )

                MapCategoryChip(
                    text = "🏥 Veterinarias",
                    selected = categoriaSeleccionada == "Veterinaria",
                    onClick = {
                        categoriaSeleccionada = "Veterinaria"
                    }
                )

                MapCategoryChip(
                    text = "✂️ Peluquerías",
                    selected = categoriaSeleccionada == "Peluquería",
                    onClick = {
                        categoriaSeleccionada = "Peluquería"
                    }
                )

                MapCategoryChip(
                    text = "🐕 Paseadores",
                    selected = categoriaSeleccionada == "Paseador",
                    onClick = {
                        categoriaSeleccionada = "Paseador"
                    }
                )

                MapCategoryChip(
                    text = "🏨 Guarderías",
                    selected = categoriaSeleccionada == "Guardería",
                    onClick = {
                        categoriaSeleccionada = "Guardería"
                    }
                )

            }
        }


        // ============================================================
        // MAPA REAL - MAPLIBRE
        // ============================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            // Mapa interactivo centrado inicialmente en Medellín.
            PetConnectMap(
                negocios = negociosFiltrados,
                ubicacionObjetivo = ubicacionObjetivo,
                posicionCamaraGuardada = posicionCamaraGuardada,
                onPosicionCamaraActualizada = onPosicionCamaraActualizada,
                solicitudCentrado = solicitudCentrado,
                modifier = Modifier.fillMaxSize(),
                onBusinessSelected = { businessId ->
                    onBusinessSelected(businessId)
                }
            )

            // Botón visual de ubicación.
            // Conectaremos su funcionalidad posteriormente.
            Box(
                modifier = Modifier
                    .clickable {
                        mensajeUbicacion = null

                        if (permisoUbicacionConcedido()) {

                            buscarMiUbicacion()

                        } else {
                            solicitarPermisosUbicacion.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    }
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),

                contentAlignment = Alignment.Center

            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Mi ubicación",
                    tint = Color(0xFF227A76),
                    modifier = Modifier.size(30.dp)
                )
                mensajeUbicacion?.let { mensaje ->

                    Text(
                        text = mensaje,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(12.dp)
                            .background(
                                Color(0xFF227A76),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            )
                    )
                }
            }
        }


        // ============================================================
        // SERVICIOS CERCANOS
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 14.dp,
                    bottom = 12.dp
                )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Servicios cercanos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = if (mapBusinessUiState.isLoading) {
                        "Cargando..."
                    } else if (mapBusinessUiState.errorMessage != null) {
                        "Error al cargar"
                    } else {
                        "${negociosFiltrados.size} encontrados"
                    },
                    color = Color(0xFF777777),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.weight(1f))


                var mostrarMenuOrden by remember {
                    mutableStateOf(false)
                }

                Box {

                    TextButton(
                        onClick = {
                            mostrarMenuOrden = true
                        }
                    ) {
                        Text(
                            text = "Ordenar▾",
                            color = Color(0xFF227A76),
                            fontSize = 14.sp
                        )
                    }

                    DropdownMenu(
                        expanded = mostrarMenuOrden,
                        onDismissRequest = {
                            mostrarMenuOrden = false
                        }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Más cercanos") },
                            onClick = {
                                ordenSeleccionado = "Cercanía"
                                mostrarMenuOrden = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Mejor calificados") },
                            onClick = {
                                ordenSeleccionado = "Calificación"
                                mostrarMenuOrden = false
                            }
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.height(12.dp))


            if (mapBusinessUiState.isLoading) {

                Text(
                    text = "Cargando establecimientos...",
                    color = Color(0xFF777777),
                    fontSize = 14.sp
                )

            } else if (mapBusinessUiState.errorMessage != null) {

                Text(
                    text = "No se pudieron cargar los establecimientos.",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )


            } else if (negociosFiltrados.isEmpty()) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        text = "🔎",
                        fontSize = 26.sp
                    )

                    Text(
                        text = "No encontramos establecimientos",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF227A76)
                    )

                    Text(
                        text = if (textoBusqueda.isNotBlank()) {
                            "Prueba con otro nombre o cambia la categoría."
                        } else {
                            "Todavía no hay establecimientos en esta categoría."
                        },
                        fontSize = 12.sp,
                        color = Color(0xFF777777),
                        textAlign = TextAlign.Center
                    )
                }

        } else {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    negociosOrdenados.forEach { business ->


                        NearbyServiceCard(
                            emoji = obtenerEmojiNegocio(business.categoria),
                            name = business.nombre,
                            category = business.categoria,
                            // Distancia calculada desde el usuario.
                            distance = calcularDistanciaNegocio(
                                ubicacionUsuario = ubicacionObjetivo,
                                latitudNegocio = business.latitud,
                                longitudNegocio = business.longitud
                            ),

                            rating = business.calificacion.toString(),
                            modifier = Modifier.width(190.dp),

                            onClick = {
                                onBusinessSelected(business.id)
                            }
                        )

                    }
                }
            }



        }
    }
}

/**
 * Calcula la distancia en línea recta entre
 * el usuario y un establecimiento.
 *
 * Devuelve un texto listo para mostrar
 * en las tarjetas de PetConnect.
 */
private fun calcularDistanciaNegocio(
    ubicacionUsuario: LatLng?,
    latitudNegocio: Double,
    longitudNegocio: Double
): String {

    // Todavía no conocemos la ubicación del usuario.
    if (ubicacionUsuario == null) {
        return "Activa tu ubicación"
    }

    // Evitamos calcular distancias con datos inválidos.
    if (
        !latitudNegocio.isFinite() ||
        !longitudNegocio.isFinite() ||
        latitudNegocio !in -90.0..90.0 ||
        longitudNegocio !in -180.0..180.0 ||
        (latitudNegocio == 0.0 && longitudNegocio == 0.0)
    ) {
        return "Distancia no disponible"
    }

    val resultado = FloatArray(1)

    Location.distanceBetween(
        ubicacionUsuario.latitude,
        ubicacionUsuario.longitude,
        latitudNegocio,
        longitudNegocio,
        resultado
    )

    val distanciaMetros = resultado[0]

    return if (distanciaMetros < 1000f) {

        "${distanciaMetros.toInt()} m"

    } else {

        String.format(
            Locale("es", "CO"),
            "%.1f km",
            distanciaMetros / 1000f
        )
    }
}


// ====================================================================
// CHIP DE CATEGORÍA
// ====================================================================

@Composable
private fun MapCategoryChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
){

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (selected) {
            Color(0xFF227A76)
        } else {
            Color.White
        },
        shadowElevation = if (selected) 0.dp else 2.dp
    ) {

        Text(
            text = text,
            color = if (selected) {
                Color.White
            } else {
                Color(0xFF222222)
            },
            fontSize = 14.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 10.dp
            )
        )
    }
}


// ====================================================================
// BLOQUE DEL MAPA
// ====================================================================

@Composable
private fun MapBlock(
    modifier: Modifier
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFCDE8E3))
    )
}


// ====================================================================
// MARCADOR DEL MAPA
// ====================================================================

@Composable
private fun MapMarker(
    emoji: String,
    color: Color,
    modifier: Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = emoji,
                fontSize = 20.sp
            )
        }

        Box(
            modifier = Modifier
                .size(12.dp)
                .offset(y = (-5).dp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 4.dp
                    )
                )
        )
    }
}


// ====================================================================
// TARJETA DE SERVICIO CERCANO
// ====================================================================

@Composable
private fun NearbyServiceCard(
    emoji: String,
    name: String,
    category: String,
    distance: String,
    rating: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFAF9F5)
        )
    ) {


        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // ==========================================
            // ÍCONO, NOMBRE Y CATEGORÍA
            // ==========================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE5F1EE)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 19.sp
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {

                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = category,
                        fontSize = 11.sp,
                        color = Color(0xFF777777),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            HorizontalDivider(
                color = Color(0xFFEAEAE6),
                thickness = 1.dp
            )

            // ==========================================
            // DISTANCIA Y CALIFICACIÓN
            // ==========================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = distance,
                    fontSize = 11.sp,
                    color = Color(0xFF666666),
                    maxLines = 1
                )

                Text(
                    text = "★ $rating",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD58A00)
                )
            }
        }

    }

}

private fun normalizarCategoria(texto: String): String {

    val textoSinTildes = Normalizer.normalize(
        texto.trim().lowercase(Locale.ROOT),
        Normalizer.Form.NFD
    )

    return textoSinTildes
        .replace(Regex("\\p{M}+"), "")
}
private fun obtenerEmojiNegocio(categoria: String): String {

    return when (normalizarCategoria(categoria)) {
        "veterinaria", "veterinarias" -> "🏥"

        "peluqueria", "peluquerias",
        "bano", "bano y peluqueria" -> "✂️"

        "paseador", "paseadores", "paseos" -> "🐕"

        "guarderia", "guarderias" -> "🏨"

        else -> "🐾"
    }
}


private fun obtenerDistanciaEnMetros(
    ubicacionUsuario: LatLng?,
    latitudNegocio: Double,
    longitudNegocio: Double
): Float {

    // Si no hay ubicación, enviamos el negocio al final.
    if (ubicacionUsuario == null) {
        return Float.MAX_VALUE
    }

    // Validación de coordenadas.
    if (
        !latitudNegocio.isFinite() ||
        !longitudNegocio.isFinite() ||
        latitudNegocio !in -90.0..90.0 ||
        longitudNegocio !in -180.0..180.0 ||
        (latitudNegocio == 0.0 && longitudNegocio == 0.0)
    ) {
        return Float.MAX_VALUE
    }

    val resultado = FloatArray(1)

    Location.distanceBetween(
        ubicacionUsuario.latitude,
        ubicacionUsuario.longitude,
        latitudNegocio,
        longitudNegocio,
        resultado
    )

    return resultado[0]
}
