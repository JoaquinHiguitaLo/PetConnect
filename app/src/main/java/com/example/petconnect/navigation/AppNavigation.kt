package com.example.petconnect.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petconnect.ui.home.HomeScreen
import com.example.petconnect.ui.login.LoginScreen
import com.example.petconnect.ui.register.RegisterScreen
import com.google.firebase.auth.FirebaseAuth

// Definimos las rutas como constantes en un solo lugar.
// Esto evita errores de tipeo: si escribes mal "home" en un lugar y "hme" en otro,
// el compilador no te avisa (son solo strings), y tu app fallaría en tiempo de ejecución.
// Centralizando los nombres aquí, solo los escribes una vez.
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // preguntamos a Firebase si ya hay una sesión activa ANTES de decidir
    // qué pantalla mostrar primero.
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
        Routes.HOME
    } else {
        Routes.LOGIN
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        // Borra "login" del historial, así el botón "atrás"
                        // no puede regresar a la pantalla de login.
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        // Aquí borramos hasta "login" (no solo "register"),
                        // porque si el usuario llegó a Register PASANDO por Login,
                        // tampoco queremos que pueda volver a Login con "atrás".
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        // Al cerrar sesión, borramos TODO el historial hasta el inicio
                        // (id 0 = la primera pantalla que se mostró). Así el usuario
                        // no puede volver a Home presionando "atrás" después de cerrar sesión.
                        popUpTo(0)
                    }
                }
            )
        }
    }
}