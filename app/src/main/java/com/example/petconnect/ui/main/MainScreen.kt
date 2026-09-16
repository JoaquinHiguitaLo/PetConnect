package com.example.petconnect.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.petconnect.ui.home.HomeScreen

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {

    // ============================================================
    // NAVEGACIÓN PRINCIPAL
    // ============================================================
    // 0 = Inicio
    // 1 = Mapa
    // 2 = Mascotas
    // 3 = IA
    // 4 = Perfil
    //
    // Esta variable controla qué opción del BottomNav está activa.
    // ============================================================

    var selectedItem by remember {
        mutableStateOf(0)
    }

    // ============================================================
    // NAVEGACIÓN INTERNA DEL PERFIL
    // ============================================================
    // Esta variable permite abrir una pantalla secundaria
    // sin convertirla en una opción del BottomNav.
    //
    // false = estamos en una pantalla principal
    // true  = estamos viendo Información personal
    // ============================================================

    var showPersonalInfo by remember {
        mutableStateOf(false)
    }

    var showAddPet by remember {
        mutableStateOf(false)
    }


    Scaffold(
        bottomBar = {

            // El BottomNav solo aparece en las pantallas principales.
            // Las pantallas secundarias, como "Agregar mascota",
            // utilizan su propia flecha para regresar.
            if (!showPersonalInfo && !showAddPet) {
                BottomNav(
                    selectedItem = selectedItem,
                    onItemSelected = { selectedItem = it }
                )
            }
        }
    ) { innerPadding ->

        // ========================================================
        // PANTALLA SECUNDARIA: INFORMACIÓN PERSONAL
        // ========================================================

        if (showPersonalInfo) {

            PersonalInfoScreen(
                modifier = Modifier.padding(innerPadding),
                onLogout = onLogout,
                onBack = {
                    // Al presionar ← volvemos al Perfil.
                    showPersonalInfo = false
                }
            )

        } else if (showAddPet) {

            AddPetScreen(
                modifier = Modifier.padding(innerPadding),
                onBack = {
                    showAddPet = false
                }
            )
        } else {

            // ====================================================
            // PANTALLAS PRINCIPALES
            // ====================================================

            when (selectedItem) {

                // ------------------------------------------------
                // INICIO
                // ------------------------------------------------

                0 -> HomeScreen(
                    onLogout = onLogout,
                    modifier = Modifier.padding(innerPadding)
                )

                // ------------------------------------------------
                // MAPA
                // ------------------------------------------------

                1 -> MapScreen(
                    modifier = Modifier.padding(innerPadding)
                )

                // ------------------------------------------------
                // MASCOTAS
                // ------------------------------------------------

                2 -> PetsScreen(
                    modifier = Modifier.padding(innerPadding),
                    onAddPet = {
                      showAddPet = true
                    }
                )

                // ------------------------------------------------
                // INTELIGENCIA ARTIFICIAL
                // ------------------------------------------------

                3 -> AIScreen(
                    modifier = Modifier.padding(innerPadding)
                )

                // ------------------------------------------------
                // PERFIL
                // ------------------------------------------------

                4 -> ProfileScreen(

                    modifier = Modifier.padding(innerPadding),

                    onLogout = onLogout,

                    // Al tocar "Información personal"
                    // abrimos PersonalInfoScreen.
                    onPersonalInfo = {
                        showPersonalInfo = true
                    },

                    // Al tocar "Mis mascotas"
                    // cambiamos a la sección Mascotas
                    // del BottomNav.
                    onPets = {
                        selectedItem = 2
                    }
                )
            }
        }
    }
}