
package com.example.petconnect.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.petconnect.data.model.Business
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import androidx.compose.runtime.rememberUpdatedState
import org.maplibre.android.camera.CameraUpdateFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color as AndroidColor
import org.maplibre.android.annotations.IconFactory


@Composable
fun PetConnectMap(
    negocios: List<Business>,
    ubicacionObjetivo: LatLng? = null,
    onBusinessSelected: (String) -> Unit = {},
    posicionCamaraGuardada: CameraPosition? = null,
    onPosicionCamaraActualizada: (CameraPosition) -> Unit = {},
    solicitudCentrado: Int = 0,
    modifier: Modifier = Modifier
) {
    val onPosicionCamaraActualizadaActual by rememberUpdatedState(
        onPosicionCamaraActualizada
    )
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val onBusinessSelectedActual by rememberUpdatedState(
        onBusinessSelected
    )
    // Guardamos el mapa cuando termina de cargar.
    var mapaListo by remember {
        mutableStateOf<MapLibreMap?>(null)
    }

    // Creamos el mapa una sola vez.
    val mapView = remember(context) {

        MapLibre.getInstance(context.applicationContext)

        MapView(context).apply {

            onCreate(null)

            getMapAsync { map ->

                // Ubicación inicial: Medellín.
                map.cameraPosition = posicionCamaraGuardada
                    ?: CameraPosition.Builder()
                        .target(LatLng(6.2442, -75.5812))
                        .zoom(12.0)
                        .build()
                // Guardamos la posición cuando termina
                // cualquier movimiento de cámara.
                map.addOnCameraIdleListener {
                    onPosicionCamaraActualizadaActual(map.cameraPosition)
                }
                // Cargamos el estilo del mapa.
                map.setStyle(
                    "https://tiles.openfreemap.org/styles/liberty"
                ) {
                    // El mapa está preparado
                    // para recibir marcadores.
                    mapaListo = map
                }
            }
        }
    }

    // =====================================================
    // MARCADORES DE NEGOCIOS APROBADOS
    // =====================================================


    LaunchedEffect(mapaListo, negocios, ubicacionObjetivo) {

        val map = mapaListo ?: return@LaunchedEffect

        // Retiramos los marcadores anteriores.
        map.removeAnnotations()

        // Relacionamos cada marcador con su negocio.
        val negociosPorMarcador = mutableMapOf<Long, String>()

        negocios
            .filter { business ->
                business.estadoVerificacion == "APROBADO" &&
                        business.latitud.isFinite() &&
                        business.longitud.isFinite() &&
                        business.latitud in -90.0..90.0 &&
                        business.longitud in -180.0..180.0 &&
                        !(business.latitud == 0.0 &&
                                business.longitud == 0.0)
            }
            .forEach { business ->

                val marker = map.addMarker(
                    MarkerOptions()
                        .position(
                            LatLng(
                                business.latitud,
                                business.longitud
                            )
                        )
                        .title(business.nombre)
                        .snippet(business.categoria)
                )

                negociosPorMarcador[marker.id] = business.id
            }

        // =====================================================
        // MARCADOR AZUL DE UBICACIÓN DEL USUARIO
        // =====================================================

        ubicacionObjetivo?.let { coordenadas ->

            val bitmapUbicacion = crearIconoUbicacion()

            val iconoAzul = IconFactory
                .getInstance(context)
                .fromBitmap(bitmapUbicacion)

            map.addMarker(
                MarkerOptions()
                    .position(coordenadas)
                    .title("Mi ubicación")
                    .icon(iconoAzul)
            )
        }

        // Al tocar la ventana informativa, abrimos el detalle.
        map.setOnInfoWindowClickListener { marker ->

            val businessId = negociosPorMarcador[marker.id]

            if (businessId != null) {
                onBusinessSelectedActual(businessId)
                true
            } else {
                false
            }
        }
    }


    // =====================================================
    // CENTRAR EL MAPA EN LA UBICACIÓN DEL USUARIO
    // =====================================================



    LaunchedEffect(
        mapaListo,
        solicitudCentrado
    ) {

        val map = mapaListo ?: return@LaunchedEffect

        // Al entrar o regresar al mapa, no animamos
        // automáticamente hacia la ubicación del usuario.
        if (solicitudCentrado == 0) {
            return@LaunchedEffect
        }

        val coordenadas = ubicacionObjetivo
            ?: return@LaunchedEffect

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                coordenadas,
                15.0
            ),
            1200
        )
    }



    // =====================================================
    // CICLO DE VIDA DEL MAPA
    // =====================================================

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

    // =====================================================
    // MAPA NATIVO DENTRO DE JETPACK COMPOSE
    // =====================================================

    AndroidView(
        factory = { mapView },
        modifier = modifier.fillMaxSize()
    )
}

/**
 * Crea un indicador azul con borde blanco
 * para representar la ubicación del usuario.
 */
private fun crearIconoUbicacion(): Bitmap {

    val tamaño = 64

    val bitmap = Bitmap.createBitmap(
        tamaño,
        tamaño,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)

    val pintura = Paint(Paint.ANTI_ALIAS_FLAG)

    // Círculo exterior blanco.
    pintura.color = AndroidColor.WHITE

    canvas.drawCircle(
        32f,
        32f,
        23f,
        pintura
    )

    // Círculo interior azul.
    pintura.color = AndroidColor.rgb(
        22,
        133,
        233
    )

    canvas.drawCircle(
        32f,
        32f,
        17f,
        pintura
    )

    return bitmap
}
