
package com.example.petconnect.ui.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.MapLibreMap

private val VerdeSelector = Color(0xFF227A76)

@Composable
fun SelectAddressLocationScreen(
    onBack: () -> Unit,
    onLocationSelected: (LatLng) -> Unit,
    ubicacionInicial: LatLng = LatLng(6.2442, -75.5812)
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var mapaListo by remember {
        mutableStateOf(false)
    }

    var coordenadasSeleccionadas by remember {
        mutableStateOf(ubicacionInicial)
    }

    val mapView = remember(context) {

        MapLibre.getInstance(context.applicationContext)

        MapView(context).apply {

            onCreate(null)

            getMapAsync { map ->

                map.cameraPosition = CameraPosition.Builder()
                    .target(ubicacionInicial)
                    .zoom(16.0)
                    .build()

                map.addOnCameraIdleListener {
                    map.cameraPosition.target?.let { coordenadas ->
                        coordenadasSeleccionadas = coordenadas
                    }
                }

                map.setStyle(
                    "https://tiles.openfreemap.org/styles/liberty"
                ) {
                    mapaListo = true

                    map.cameraPosition.target?.let { coordenadas ->
                        coordenadasSeleccionadas = coordenadas
                    }
                }
            }
        }
    }

    DisposableEffect(lifecycleOwner, mapView) {

        val observer = LifecycleEventObserver { _, event ->

            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        if (
            lifecycleOwner.lifecycle.currentState
                .isAtLeast(Lifecycle.State.STARTED)
        ) {
            mapView.onStart()
        }

        if (
            lifecycleOwner.lifecycle.currentState
                .isAtLeast(Lifecycle.State.RESUMED)
        ) {
            mapView.onResume()
        }

        onDispose {

            lifecycleOwner.lifecycle.removeObserver(observer)

            if (
                lifecycleOwner.lifecycle.currentState
                    .isAtLeast(Lifecycle.State.RESUMED)
            ) {
                mapView.onPause()
            }

            if (
                lifecycleOwner.lifecycle.currentState
                    .isAtLeast(Lifecycle.State.STARTED)
            ) {
                mapView.onStop()
            }

            mapView.onDestroy()
        }
    }

    Dialog(
        onDismissRequest = onBack,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F8F4))
        ) {

            // ENCABEZADO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VerdeSelector)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar selector",
                        tint = Color.White
                    )
                }

                Column {
                    Text(
                        text = "Seleccionar ubicación",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "Mueve el mapa para elegir un punto",
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // MAPA Y MARCADOR FIJO
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                AndroidView(
                    factory = { mapView },
                    modifier = Modifier.fillMaxSize()
                )

                // El indicador permanece fijo en el centro.
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Punto seleccionado",
                    tint = Color(0xFFE0654F),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(42.dp)
                )
            }

            // INFORMACIÓN Y CONFIRMACIÓN
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Coordenadas seleccionadas",
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "Latitud: %.6f".format(
                        coordenadasSeleccionadas.latitude
                    )
                )

                Text(
                    text = "Longitud: %.6f".format(
                        coordenadasSeleccionadas.longitude
                    )
                )

                Button(
                    onClick = {
                        onLocationSelected(
                            coordenadasSeleccionadas
                        )
                    },
                    enabled = mapaListo,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeSelector
                    )
                ) {
                    Text("Confirmar ubicación")
                }
            }
        }
    }
}
