package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import com.example.petconnect.R
import androidx.compose.material.icons.Icons
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onPersonalInfo: () -> Unit,
    onPets: () -> Unit,
    onBusinesses: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    // Observamos el estado del perfil.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var fotoSeleccionada by remember {
        mutableStateOf<Uri?>(null)
    }

    val selectorFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            fotoSeleccionada = uri
        }
    }


    // Cargamos los datos del usuario al entrar a esta pantalla.
    LaunchedEffect(Unit) {
        viewModel.cargarUsuario()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F6))
            .verticalScroll(rememberScrollState())
    ) {

        // ============================================================
        // ENCABEZADO
        // ============================================================


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 28.dp,
                    bottom = 12.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(108.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFB9E7E4))
                    .clickable {
                        selectorFoto.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {

                val bitmap = remember(fotoSeleccionada) {
                    fotoSeleccionada?.let { uri ->
                        try {
                            context.contentResolver
                                .openInputStream(uri)
                                ?.use { stream ->
                                    BitmapFactory.decodeStream(stream)
                                }
                        } catch (e: Exception) {
                            null
                        }
                    }
                }

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Fotografía de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "👤",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = uiState.user?.nombreCompleto
                    ?.takeIf { it.isNotBlank() }
                    ?: "Usuario",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF227C78),
                modifier = Modifier.clickable {
                    onPersonalInfo()
                }
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = uiState.user?.correo ?: "Cargando...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF71817E)
            )
        }



        // ============================================================
        // CONTENIDO
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                )


        ) {
            uiState.errorMessage?.let { error ->

                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 8.dp
                        )
                )
            }

            // ========================================================
            // MI CUENTA
            // ========================================================
            Text(
                text = "Mi espacio",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF303534)
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            ProfileSectionCard {

                ProfileOption(
                    icon = R.drawable.icon_mismascotas_perfil,
                    title = "Mis mascotas",
                    description = "Administra la información de tus mascotas",
                    onClick = onPets
                )
                // Solo los prestadores pueden acceder
                // a la administración de sus negocios.
                if (uiState.user?.rol == "PRESTADOR") {

                    ProfileDivider()

                    ProfileOption(
                        icon = R.drawable.icon_misnegocios_perfil,
                        title = "Mis negocios",
                        description = "Administra tus establecimientos",
                        onClick = onBusinesses
                    )
                }

                ProfileDivider()

                ProfileOption(
                    icon = R.drawable.icon_misfavoritos_perfil,
                    title = "Mis favoritos",
                    description = "Servicios que guardaste para después"
                )
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )


            // ========================================================
            // PREFERENCIAS
            // ========================================================

            Text(
                text = "Preferencias",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            ProfileSectionCard {

                ProfileOption(
                    icon = R.drawable.icon_configuracion_perfil,
                    title = "Configuración",
                    description = "Personaliza tu experiencia en PetConnect"
                )

                ProfileDivider()

                ProfileOption(
                    icon = R.drawable.icon_notificaciones_perfil,
                    title = "Notificaciones",
                    description = "Gestiona tus avisos y recordatorios"
                )

                ProfileDivider()

                ProfileOption(
                    icon = R.drawable.icon_seguridad_perfil,
                    title = "Privacidad y seguridad",
                    description = "Controla la seguridad de tu cuenta"
                )

                ProfileDivider()

                ProfileOption(
                    icon = R.drawable.icon_ayuda_perfil,
                    title = "Ayuda y soporte",
                    description = "Encuentra respuestas y contacta con nosotros"
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = onLogout,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = Color(0xFFE0654F),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Cerrar sesión",
                    color = Color(0xFFE0654F),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(110.dp))
        }
    }

}
// ====================================================================
// TARJETA DE SECCIÓN
// ====================================================================

@Composable
private fun ProfileSectionCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            ),
        content = content
    )
}


// ====================================================================
// OPCIÓN DEL PERFIL
// ====================================================================

@Composable
private fun ProfileOption(
    icon: Int,
    title: String,
    description: String,
    onClick: (() -> Unit)? = null

) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            }
            .padding(
                horizontal = 16.dp,
                vertical = 13.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Icono


        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = Color(0xFFEAF5F2),
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                contentScale = ContentScale.Fit
            )
        }


        Spacer(
            modifier = Modifier.width(12.dp)
        )

        // Texto
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF757575)
            )
        }

        // Flecha
        Text(
            text = "›",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF8A9A9A)
        )
    }
}


// ====================================================================
// DIVISOR
// ====================================================================

@Composable
private fun ProfileDivider() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 80.dp)
            .height(1.dp)
            .background(Color(0xFFE7E7E7))
    )
}
