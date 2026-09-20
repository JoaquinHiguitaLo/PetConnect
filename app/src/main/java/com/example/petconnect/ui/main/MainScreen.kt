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
import com.example.petconnect.data.model.Pet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petconnect.viewmodel.PetsViewModel

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val petsViewModel: PetsViewModel = viewModel()

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

    // ============================================================
    // NAVEGACIÓN: EDITAR MASCOTA
    // ============================================================
    // showEditPet indica si debemos mostrar la pantalla de edición.
    //
    // selectedPet almacena la mascota que el usuario seleccionó.
    // Es nullable (?) porque inicialmente no hay ninguna mascota
    // seleccionada.
    //
    // Flujo:
    //
    // PetCard
    //    ↓
    // onEditPet(pet)
    //    ↓
    // MainScreen
    //    ↓
    // selectedPet = pet
    //    ↓
    // showEditPet = true
    // ============================================================

    var showEditPet by remember {
        mutableStateOf(false)
    }

    var selectedPet by remember {
        mutableStateOf<Pet?>(null)
    }

    // ============================================================
    // MASCOTA SELECCIONADA PARA ELIMINAR
    // ============================================================
    // Guarda temporalmente la mascota que el usuario eligió
    // eliminar mientras mostramos la confirmación.
    //
    // Es nullable porque inicialmente no hay ninguna mascota
    // seleccionada para eliminar.
    // ============================================================

    var petToDelete by remember {
        mutableStateOf<Pet?>(null)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        bottomBar = {

            // El BottomNav solo aparece en las pantallas principales.
            // Las pantallas secundarias, como "Agregar mascota",
            // utilizan su propia flecha para regresar.
            if (!showPersonalInfo && !showAddPet && !showEditPet) {
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
        } else if (showEditPet && selectedPet != null) {
            EditPetScreen(
                modifier = Modifier.padding(innerPadding),
                pet = selectedPet!!,
                onBack = {
                    showEditPet = false
                    selectedPet = null
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
                    },
                    onEditPet = { pet ->
                        // Guarda la mascota seleccionada.
                        selectedPet = pet
                        // Indica que debe abrir la pantalla de edición.
                        showEditPet = true
                    },
                    onDeletePet = { pet ->

                        // Guardamos cuál mascota quiere eliminar el usuario.
                        petToDelete = pet

                        // Mostramos posteriormente el diálogo de confirmación.
                        showDeleteDialog = true

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
    // Diálogo de confirmación
    // ============================================================
    // DIÁLOGO DE CONFIRMACIÓN PARA ELIMINAR
    // ============================================================
    // Solo mostramos el diálogo cuando:
    // 1. El usuario seleccionó una mascota para eliminar.
    // 2. showDeleteDialog es true.
    //
    // Todavía NO eliminamos nada aquí.
    // Primero le damos al usuario la posibilidad de cancelar.
    // ============================================================

    if (showDeleteDialog && petToDelete != null) {

        AlertDialog(

            // ----------------------------------------------------
            // Se ejecuta cuando el usuario cierra el diálogo.
            // En este caso simplemente cancelamos la operación.
            // ----------------------------------------------------
            onDismissRequest = {
                showDeleteDialog = false
                petToDelete = null
            },

            // ----------------------------------------------------
            // TÍTULO DEL DIÁLOGO
            // ----------------------------------------------------
            title = {
                Text("¿Eliminar mascota?")
            },

            // ----------------------------------------------------
            // MENSAJE
            // ----------------------------------------------------
            text = {
                Text(
                    "¿Estás seguro de que deseas eliminar " +
                            "${petToDelete?.nombre}?"
                )
            },

            // ----------------------------------------------------
            // BOTÓN CANCELAR
            // ----------------------------------------------------
            dismissButton = {

                TextButton(
                    onClick = {

                        // Cerramos el diálogo.
                        showDeleteDialog = false

                        // Ya no hay una mascota pendiente
                        // de eliminación.
                        petToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            },

            // ----------------------------------------------------
            // BOTÓN ELIMINAR
            // ----------------------------------------------------
            confirmButton = {

                TextButton(
                    onClick = {

                        petToDelete?.let { pet ->

                            // Enviamos la mascota seleccionada al ViewModel.
                            // El ViewModel se encarga de ejecutar la eliminación.
                            petsViewModel.eliminarMascota(pet)
                        }

                        // Cerramos el diálogo.
                        showDeleteDialog = false

                        // Ya no necesitamos conservar la mascota seleccionada.
                        petToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            }
        )
    }
}
