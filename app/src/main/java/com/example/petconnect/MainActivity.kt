
package com.example.petconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

import com.example.petconnect.navigation.AppNavigation
import com.example.petconnect.ui.theme.PetConnectTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import com.example.petconnect.ui.main.PetConnectSplashScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            PetConnectTheme {

                var mostrarSplash by remember {
                    mutableStateOf(true)
                }

                LaunchedEffect(Unit) {
                    delay(800L)
                    mostrarSplash = false
                }

                if (mostrarSplash) {
                    PetConnectSplashScreen()
                } else {
                    AppNavigation()
                }
            }
        }
    }
}
