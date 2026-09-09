package com.example.petconnect.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {

    // ---------------------------------------------------------
    // ESTADO DE LA PANTALLA
    // ---------------------------------------------------------
    // Observamos el estado que administra LoginViewModel.
    //
    // Cuando el ViewModel modifica uiState, Compose vuelve a
    // dibujar automáticamente las partes de la interfaz que
    // dependen de ese estado.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    // ---------------------------------------------------------
    // NAVEGACIÓN DESPUÉS DEL LOGIN
    // ---------------------------------------------------------
    // loginSuccess cambia a true cuando Firebase confirma
    // correctamente las credenciales.
    //
    // La pantalla no decide hacia dónde navegar.
    // Simplemente informa al componente de navegación:
    // "el login fue exitoso".
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
        }
    }


    // ---------------------------------------------------------
    // CONTENEDOR PRINCIPAL
    // ---------------------------------------------------------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // -----------------------------------------------------
        // NOMBRE DE LA APLICACIÓN
        // -----------------------------------------------------
        Text(
            text = "PetConnect",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))


        // -----------------------------------------------------
        // TÍTULO
        // -----------------------------------------------------
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(10.dp))


        // -----------------------------------------------------
        // TEXTO INTRODUCTORIO
        // -----------------------------------------------------
        Text(
            text = "Ingresa a tu cuenta y continúa cuidando a tu mascota.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(28.dp))


        // -----------------------------------------------------
        // CORREO ELECTRÓNICO
        // -----------------------------------------------------
        Text(
            text = "Correo electrónico",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.email,

            // Cada vez que el usuario escribe, enviamos
            // el nuevo valor al ViewModel.
            onValueChange = viewModel::onEmailChange,

            placeholder = {
                Text("Ej: correo@email.com")
            },

            isError = uiState.emailError != null,

            supportingText = {
                uiState.emailError?.let {
                    Text(it)
                }
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),

            singleLine = true,

            shape = RoundedCornerShape(14.dp),

            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(20.dp))


        // -----------------------------------------------------
        // CONTRASEÑA
        // -----------------------------------------------------
        Text(
            text = "Contraseña",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.password,

            onValueChange = viewModel::onPasswordChange,

            placeholder = {
                Text("Ingresa tu contraseña")
            },

            isError = uiState.passwordError != null,

            supportingText = {
                uiState.passwordError?.let {
                    Text(it)
                }
            },

            visualTransformation = PasswordVisualTransformation(),

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),

            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.onLoginClick()
                }
            ),

            singleLine = true,

            shape = RoundedCornerShape(14.dp),

            modifier = Modifier.fillMaxWidth()
        )


        // -----------------------------------------------------
        // ERROR GENERAL
        // -----------------------------------------------------
        // Este error viene normalmente de Firebase.
        //
        // Ejemplos:
        // - contraseña incorrecta
        // - usuario no encontrado
        // - problemas de conexión
        //
        // Los errores específicos de cada campo se muestran
        // debajo de cada TextField.
        uiState.generalError?.let { error ->

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }


        Spacer(modifier = Modifier.height(28.dp))


        // -----------------------------------------------------
        // BOTÓN INICIAR SESIÓN
        // -----------------------------------------------------
        Button(
            onClick = viewModel::onLoginClick,

            // Mientras Firebase está procesando el login,
            // evitamos que el usuario pulse varias veces.
            enabled = !uiState.isLoading,

            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),

            shape = RoundedCornerShape(14.dp),

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {

            if (uiState.isLoading) {

                // Indicador mostrado mientras esperamos
                // la respuesta de Firebase.
                CircularProgressIndicator(
                    modifier = Modifier.height(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )

            } else {

                Text(
                    text = "Iniciar sesión"
                )
            }
        }


        Spacer(modifier = Modifier.height(18.dp))


        // -----------------------------------------------------
        // IR A REGISTRO
        // -----------------------------------------------------
        TextButton(
            onClick = onNavigateToRegister
        ) {
            Text(
                text = "¿No tienes cuenta? Regístrate",
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}