package com.example.petconnect.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {

    // ---------------------------------------------------------
    // 1. OBTENER EL ESTADO DE LA PANTALLA
    // ---------------------------------------------------------

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    // ---------------------------------------------------------
    // 2. NAVEGACIÓN DESPUÉS DE REGISTRO EXITOSO
    // ---------------------------------------------------------

    LaunchedEffect(uiState.registrationSuccess) {

        if (uiState.registrationSuccess) {
            onRegisterSuccess()
        }
    }


    // ---------------------------------------------------------
    // 3. CONTENEDOR PRINCIPAL
    // ---------------------------------------------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                horizontal = 24.dp,
                vertical = 32.dp
            ),
        verticalArrangement = Arrangement.Top
    ) {


        // -----------------------------------------------------
        // 4. NOMBRE DE LA MARCA
        // -----------------------------------------------------

        Spacer(
            modifier = Modifier.height(48.dp)
        )

        Text(
            text = "PetConnect",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        // -----------------------------------------------------
        // 5. TÍTULO DE LA PANTALLA
        // -----------------------------------------------------

        Text(
            text = "Crea tu cuenta",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        // -----------------------------------------------------
        // 6. TEXTO DESCRIPTIVO
        // -----------------------------------------------------

        Text(
            text = "Únete a PetConnect y conecta todo el cuidado de tu mascota.",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )


        Spacer(
            modifier = Modifier.height(32.dp)
        )


        // -----------------------------------------------------
        // 7. CAMPO DE CORREO
        // -----------------------------------------------------

        Text(
            text = "Correo electrónico",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        OutlinedTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,

            placeholder = {
                Text("Ej: correo@email.com")
            },

            isError = uiState.emailError != null,

            supportingText = {
                uiState.emailError?.let { error ->
                    Text(error)
                }
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),

            singleLine = true,

            shape = RoundedCornerShape(14.dp),

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),

            modifier = Modifier.fillMaxWidth()
        )


        Spacer(
            modifier = Modifier.height(14.dp)
        )


        // -----------------------------------------------------
        // 8. CAMPO DE CONTRASEÑA
        // -----------------------------------------------------

        Text(
            text = "Contraseña",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,

            placeholder = {
                Text("Mínimo 6 caracteres")
            },

            isError = uiState.passwordError != null,

            supportingText = {
                uiState.passwordError?.let { error ->
                    Text(error)
                }
            },

            visualTransformation = PasswordVisualTransformation(),

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),

            singleLine = true,

            shape = RoundedCornerShape(14.dp),

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),

            modifier = Modifier.fillMaxWidth()
        )


        Spacer(
            modifier = Modifier.height(14.dp)
        )


        // -----------------------------------------------------
        // 9. CONFIRMAR CONTRASEÑA
        // -----------------------------------------------------

        Text(
            text = "Confirmar contraseña",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,

            placeholder = {
                Text("Repite tu contraseña")
            },

            isError = uiState.confirmPasswordError != null,

            supportingText = {
                uiState.confirmPasswordError?.let { error ->
                    Text(error)
                }
            },

            visualTransformation = PasswordVisualTransformation(),

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),

            singleLine = true,

            shape = RoundedCornerShape(14.dp),

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),

            modifier = Modifier.fillMaxWidth()
        )


        // -----------------------------------------------------
        // 10. ERROR GENERAL DE FIREBASE
        // -----------------------------------------------------

        uiState.generalError?.let { error ->

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // -----------------------------------------------------
        // 11. BOTÓN CREAR CUENTA
        // -----------------------------------------------------

        Button(
            onClick = viewModel::onRegisterClick,

            enabled = !uiState.isLoading,

            shape = RoundedCornerShape(14.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {

            if (uiState.isLoading) {

                CircularProgressIndicator(
                    modifier = Modifier.height(22.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )

            } else {

                Text(
                    text = "Crear cuenta",
                    fontWeight = FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier = Modifier.height(14.dp)
        )


        // -----------------------------------------------------
        // 12. ENLACE PARA INICIAR SESIÓN
        // -----------------------------------------------------

        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "¿Ya tienes cuenta? Inicia sesión",
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}