
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
import com.example.petconnect.ui.components.PetConnectTopBar
import com.example.petconnect.ui.components.PetConnectFormTitle
import com.example.petconnect.ui.components.PetConnectFormStyle
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.font.FontWeight

private val VerdeSelector = Color(0xFF227C78)

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
                .background(Color(0xFFFAF9F6))
        ) {

            // ENCABEZADO
            PetConnectTopBar(
                onBack = onBack
            )

            Spacer(modifier = Modifier.height(8.dp))

            PetConnectFormTitle(
                title = "Seleccionar ubicación"
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                    .padding(
                        horizontal = 24.dp,
                        vertical = 16.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // TARJETA DE COORDENADAS
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    border = BorderStroke(
                        1.dp,
                        Color(0xFFE5E9E7)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = VerdeSelector,
                                modifier = Modifier.size(20.dp)
                            )

                            Text(
                                text = "Coordenadas seleccionadas",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        HorizontalDivider(
                            color = Color(0xFFE8E8E3)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(
                                    text = "Latitud",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFF71817E)
                                )

                                Text(
                                    text = "%.6f".format(
                                        coordenadasSeleccionadas.latitude
                                    ),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(
                                    text = "Longitud",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFF71817E)
                                )

                                Text(
                                    text = "%.6f".format(
                                        coordenadasSeleccionadas.longitude
                                    ),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                // BOTÓN DE CONFIRMACIÓN
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            onLocationSelected(coordenadasSeleccionadas)
                        },
                        enabled = mapaListo,
                        modifier = Modifier
                            .fillMaxWidth(0.72f)
                            .height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PetConnectFormStyle.primary
                        )
                    ) {
                        Text(
                            text = "Confirmar ubicación",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

        }
    }
}


