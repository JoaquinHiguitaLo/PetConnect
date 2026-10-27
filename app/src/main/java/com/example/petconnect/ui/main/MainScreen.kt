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
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petconnect.viewmodel.PetsViewModel
import com.example.petconnect.viewmodel.BusinessViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.example.petconnect.data.model.Business
import androidx.compose.ui.platform.LocalContext
import com.example.petconnect.utils.NetworkUtils
import androidx.compose.runtime.produceState



@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val petsViewModel: PetsViewModel = viewModel()
    val businessViewModel: BusinessViewModel = viewModel()
    // ViewModel independiente para eliminar negocios.
    // Evita mezclar el resultado con las consultas públicas.
    val deleteBusinessViewModel: BusinessViewModel = viewModel(key = "delete_business")
    val deleteBusinessUiState by deleteBusinessViewModel.uiState.collectAsState()
    val profileViewModel: ProfileViewModel = viewModel()
    val profileUiState by profileViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val tieneConexion by produceState(
        initialValue = NetworkUtils.tieneInternet(context),
        key1 = context
    ) {
        NetworkUtils.observarConexion(context).collect { conectado ->
            value = conectado
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.cargarUsuario()
    }

    val nombreUsuario = profileUiState.user
        ?.nombreCompleto ?: ""
        ?.substringBefore(" ")
        ?.takeIf { it.isNotBlank() }
        ?: "usuario"

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
    var showCategories by remember { mutableStateOf(false) }

    var showServicesByCategory by remember { mutableStateOf(false) }

    var selectedCategoryId by remember { mutableStateOf("") }

    var selectedBusinessId by remember { mutableStateOf("") }

    var showBusinessDetail by remember {
        mutableStateOf(false)
    }
    var showMyBusinesses by remember { mutableStateOf(false) }

    var showAddBusiness by remember { mutableStateOf(false) }

    // Controla la pantalla de detalle del prestador.
    var showManageBusiness by remember {
        mutableStateOf(false)
    }
    // Controla la apertura del formulario de edición.
    var showEditBusiness by remember {
        mutableStateOf(false)
    }

    // Almacena el negocio seleccionado.
    var businessToManage by remember {
        mutableStateOf<Business?>(null)
    }
    // Negocio cuya eliminación fue confirmada.
    var businessPendingDeletion by remember {
        mutableStateOf<Business?>(null)
    }
    var businessNetworkError by remember {
        mutableStateOf<String?>(null)
    }
    // Mensaje temporal después de actualizar un negocio.
    var businessSuccessMessage by remember {
        mutableStateOf<String?>(null)
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


    // Observamos el resultado de la eliminación.

    LaunchedEffect(
        deleteBusinessUiState.isLoading,
        deleteBusinessUiState.operationSuccess,
        deleteBusinessUiState.errorMessage
    ) {
        if (
            businessPendingDeletion != null &&
            !deleteBusinessUiState.isLoading
        ) {
            when {
                deleteBusinessUiState.operationSuccess -> {
                    businessSuccessMessage =
                        "¡Establecimiento eliminado correctamente!"

                    businessPendingDeletion = null
                    businessToManage = null
                    showManageBusiness = false
                    showMyBusinesses = true

                    deleteBusinessViewModel.limpiarResultadoOperacion()
                }

                deleteBusinessUiState.errorMessage != null -> {
                    businessPendingDeletion = null
                    showManageBusiness = true
                    businessSuccessMessage = null
                }
            }
        }
    }

    Scaffold(
        bottomBar = {

            // El BottomNav solo aparece en las pantallas principales.
            // Las pantallas secundarias, como "Agregar mascota",
            // utilizan su propia flecha para regresar.
            if (!showPersonalInfo && !showAddPet && !showEditPet && !showCategories && !showServicesByCategory &&
                !showBusinessDetail && !showMyBusinesses && !showAddBusiness && !showManageBusiness && !showEditBusiness) {
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


        } else if(showBusinessDetail) {
            BusinessDetailScreen(
                businessId = selectedBusinessId,
                onBack = {
                    showBusinessDetail = false
                    showServicesByCategory = true
                },
                viewModel = businessViewModel
            )


        } else if (showServicesByCategory) {
            ServicesByCategoryScreen(
                categoryId = selectedCategoryId,
                onBack = {
                    showServicesByCategory = false
                    showCategories = true
                },
                onBusinessSelected = { businessId ->
                    selectedBusinessId = businessId
                    showServicesByCategory = false
                    showBusinessDetail = true
                },
                viewModel = businessViewModel
            )
        } else if (showCategories) {
            CategoriesScreen(
                onBack = {
                    showCategories = false
                },
                onCategorySelected = { categoryId ->
                    selectedCategoryId = categoryId
                    showCategories = false
                    showServicesByCategory = true
                }
            )
        } else if (showAddBusiness) {

            AddBusinessScreen(
                modifier = Modifier.padding(innerPadding),

                onBack = {
                    showAddBusiness = false
                    showMyBusinesses = true
                },

                onCreated = {
                    showAddBusiness = false
                    showMyBusinesses = true
                }
            )
        } else if (showEditBusiness && businessToManage != null) {

            // ==========================================
            // EDITAR NEGOCIO DEL PRESTADOR
            // ==========================================

            EditBusinessScreen(
                business = businessToManage!!,
                modifier = Modifier.padding(innerPadding),

                onBack = {
                    // Cancela la edición y regresa al detalle.
                    showEditBusiness = false
                    showManageBusiness = true
                },

                onUpdated = {
                    // La actualización fue confirmada por Firestore.
                    businessSuccessMessage =
                        "¡Negocio actualizado correctamente!"
                    // La actualización terminó correctamente.
                    showEditBusiness = false

                    // Regresamos al listado para consultar
                    // nuevamente los datos desde Firestore.
                    businessToManage = null
                    showManageBusiness = false
                    showMyBusinesses = true
                }
            )
        } else if (showManageBusiness && businessToManage != null) {

            // ==========================================
            // DETALLE DEL NEGOCIO DEL PRESTADOR
            // ==========================================

            ManageBusinessScreen(
                business = businessToManage!!,
                modifier = Modifier.padding(innerPadding),

                isDeleting = businessPendingDeletion != null,
                isConnected = tieneConexion,

                deleteError = businessNetworkError
                    ?: deleteBusinessUiState.errorMessage,

                onBack = {
                    showManageBusiness = false
                    businessToManage = null
                    showMyBusinesses = true
                },
                onEdit = { business ->

                    // Guardamos el negocio que queremos editar.
                    businessToManage = business

                    // Cerramos el detalle.
                    showManageBusiness = false

                    // Abrimos el formulario de edición.
                    showEditBusiness = true
                },


                onDelete = { business ->

                    // Solo permitimos una eliminación a la vez.
                    if (
                        businessPendingDeletion == null &&
                        !deleteBusinessUiState.isLoading
                    ) {

                        // Limpiamos el error anterior.
                        businessNetworkError = null

                        // Comprobamos la conectividad antes de eliminar.
                        if (!NetworkUtils.tieneInternet(context)) {

                            businessNetworkError =
                                "No tienes conexión a Internet. " +
                                        "Conéctate para eliminar este establecimiento."

                        } else {

                            // Limpiamos resultados anteriores.
                            deleteBusinessViewModel.limpiarResultadoOperacion()

                            // Iniciamos la eliminación en Firestore.
                            deleteBusinessViewModel.eliminarNegocio(business)

                            // Identificamos el negocio en proceso.
                            businessPendingDeletion = business
                        }
                    }

        }



            )
        } else if (showMyBusinesses) {

            MyBusinessesScreen(
                modifier = Modifier.padding(innerPadding),

                onBack = {
                    showMyBusinesses = false
                },

                onAddBusiness = {
                    showMyBusinesses = false
                    showAddBusiness = true
                },

                onBusinessSelected = { business ->
                    businessToManage = business
                    showMyBusinesses = false
                    showManageBusiness = true
                },

                successMessage = businessSuccessMessage,

                onMessageShown = {
                    businessSuccessMessage = null
                }
            )

        } else  {

            // ====================================================
            // PANTALLAS PRINCIPALES
            // ====================================================

            when (selectedItem) {

                // ------------------------------------------------
                // INICIO
                // ------------------------------------------------

                0 -> HomeScreen(
                    onLogout = onLogout,
                    onViewAllCategories = {
                        showCategories = true
                    },
                    onCategorySelected = { categoryId ->
                        selectedCategoryId = categoryId
                        showCategories = false
                        showServicesByCategory = true
                    },
                    nombreUsuario = nombreUsuario,
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
                    },

                    onBusinesses = {
                        showMyBusinesses = true
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
