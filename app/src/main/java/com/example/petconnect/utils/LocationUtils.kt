
package com.example.petconnect.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.os.CancellationSignal
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

object LocationUtils {

    fun tienePermiso(context: Context): Boolean {

        val preciso = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val aproximado = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return preciso || aproximado
    }

    /**
     * Obtiene la última ubicación conocida del dispositivo.
     */
    @Suppress("MissingPermission")
    fun obtenerUltimaUbicacion(context: Context): Location? {

        if (!tienePermiso(context)) return null

        val manager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as? LocationManager ?: return null

        val tienePermisoPreciso = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val proveedores = mutableListOf<String>()

        if (tienePermisoPreciso && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            proveedores.add(LocationManager.GPS_PROVIDER)
        }

        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            proveedores.add(LocationManager.NETWORK_PROVIDER)
        }

        if (manager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            proveedores.add(LocationManager.PASSIVE_PROVIDER)
        }

        var mejorUbicacion: Location? = null

        for (proveedor in proveedores) {
            try {
                val loc = manager.getLastKnownLocation(proveedor) ?: continue
                if (mejorUbicacion == null || loc.time > mejorUbicacion.time) {
                    mejorUbicacion = loc
                }
            } catch (e: SecurityException) {
                // Sin permisos
            } catch (e: IllegalArgumentException) {
                // Proveedor no existente
            }
        }

        return mejorUbicacion
    }

    /**
     * Solicita una ubicación actual del dispositivo.
     *
     * - Prefiere GPS cuando existe permiso preciso.
     * - Permite ubicación aproximada.
     * - No utiliza posiciones antiguas como alternativa.
     * - Espera como máximo 12 segundos.
     */

    @Suppress("MissingPermission")
    suspend fun obtenerUbicacionActual(
        context: Context
    ): Location? {

        if (!tienePermiso(context)) return null

        val manager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as? LocationManager ?: return null

        val permisoPreciso = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Verificamos si hay algún proveedor disponible.
        val proveedores = mutableListOf<String>()

        if (
            permisoPreciso &&
            manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        ) {
            proveedores.add(LocationManager.GPS_PROVIDER)
        }

        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            proveedores.add(LocationManager.NETWORK_PROVIDER)
        }

        if (proveedores.isEmpty()) return null

        // Intentamos obtener una ubicación actual.
        // Si el GPS no responde, probamos la red.
        for (proveedor in proveedores) {

            val ubicacion = try {

                withTimeoutOrNull(15_000L) {

                    suspendCancellableCoroutine<Location?> { continuation ->

                        val cancelacion = CancellationSignal()

                        continuation.invokeOnCancellation {
                            cancelacion.cancel()
                        }

                        LocationManagerCompat.getCurrentLocation(
                            manager,
                            proveedor,
                            cancelacion,
                            ContextCompat.getMainExecutor(context)
                        ) { resultado ->

                            if (continuation.isActive) {
                                continuation.resume(resultado)
                            }
                        }
                    }
                }

            } catch (e: SecurityException) {
                null
            } catch (e: IllegalArgumentException) {
                null
            }

            if (ubicacion != null) {
                return ubicacion
            }
        }

        // Si ningún proveedor obtiene una posición actual,
        // no inventamos coordenadas.
        return null
    }

}
