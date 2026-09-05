package com.example.petconnect.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterScreen(
    // Estas dos son funciones que la pantalla "padre" (la navegación) nos pasa,
    // para saber qué hacer cuando el registro sale bien, o cuando el usuario
    // quiere ir a la pantalla de login. La RegisterScreen no decide A DÓNDE navegar,
    // solo avisa "ya me registré" o "quiero ir a login".
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    // Compose sabe crear el ViewModel automáticamente con esta función especial.
    viewModel: RegisterViewModel = viewModel()
) {
    // Aquí "escuchamos" el estado del ViewModel. Cada vez que uiState cambia,
    // este Composable se vuelve a ejecutar automáticamente (esto se llama "recomposición").
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // LaunchedEffect ejecuta código cuando algo cambia — en este caso, cuando
    // registrationSuccess pasa de false a true, disparamos la navegación.
    // Esto cubre el último criterio de aceptación de la HU-01.
    LaunchedEffect(uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(24.dp))

        // Campo de correo electrónico
        OutlinedTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange, // cada letra que escribe, avisa al ViewModel
            label = { Text("Correo electrónico") },
            isError = uiState.emailError != null,       // pinta el borde rojo si hay error
            supportingText = { uiState.emailError?.let { Text(it) } }, // texto de error debajo
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Contraseña") },
            isError = uiState.passwordError != null,
            supportingText = { uiState.passwordError?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(), // oculta el texto con puntos
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        // Campo de confirmar contraseña
        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            label = { Text("Confirmar contraseña") },
            isError = uiState.confirmPasswordError != null,
            supportingText = { uiState.confirmPasswordError?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Error general (por ejemplo: "correo ya registrado", que viene de Firebase)
        uiState.generalError?.let { error ->
            Spacer(Modifier.height(8.dp))
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = viewModel::onRegisterClick,
            enabled = !uiState.isLoading, // deshabilitado mientras carga
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Registrarse")
            }
        }

        Spacer(Modifier.height(16.dp))

        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}