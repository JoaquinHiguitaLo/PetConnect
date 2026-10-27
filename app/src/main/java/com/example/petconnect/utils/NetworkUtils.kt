
package com.example.petconnect.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

object NetworkUtils {

    /**
     * Comprueba si Android detecta una conexión
     * a Internet validada.
     */
    fun tieneInternet(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

        val activeNetwork =
            connectivityManager.activeNetwork ?: return false

        val capabilities =
            connectivityManager.getNetworkCapabilities(
                activeNetwork
            ) ?: return false

        return capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        ) && capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED
        )
    }

    /**
     * Observa los cambios de conectividad.
     *
     * Emite true cuando Android detecta
     * Internet validado y false cuando no.
     *
     * Requiere Android 7.0 (API 24) o superior.
     */
    fun observarConexion(context: Context): Flow<Boolean> =
        callbackFlow {

            val appContext = context.applicationContext

            val connectivityManager =
                appContext.getSystemService(
                    Context.CONNECTIVITY_SERVICE
                ) as ConnectivityManager

            val callback =
                object : ConnectivityManager.NetworkCallback() {

                    override fun onCapabilitiesChanged(
                        network: Network,
                        capabilities: NetworkCapabilities
                    ) {
                        val conectado =
                            capabilities.hasCapability(
                                NetworkCapabilities.NET_CAPABILITY_INTERNET
                            ) && capabilities.hasCapability(
                                NetworkCapabilities.NET_CAPABILITY_VALIDATED
                            )

                        trySend(conectado)
                    }

                    override fun onLost(network: Network) {
                        trySend(false)
                    }
                }

            connectivityManager.registerDefaultNetworkCallback(
                callback
            )

            // Informamos el estado inicial.
            trySend(tieneInternet(appContext))

            // Liberamos el observador cuando
            // deja de utilizarse.
            awaitClose {
                connectivityManager.unregisterNetworkCallback(
                    callback
                )
            }

        }.distinctUntilChanged()
}
