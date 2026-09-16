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

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {

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
                .background(
                    color = Color(0xFF227C78),
                    shape = RoundedCornerShape(
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp
                    )
                )
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 24.dp,
                    bottom = 30.dp
                )
        ) {

            // Título + botón de configuración
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "MI CUENTA",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFFB5E3E1),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "Mi perfil",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botón visual de configuración
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(
                            color = Color(0xFF4B9692),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚙️",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            // ========================================================
            // TARJETA DEL USUARIO
            // ========================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Avatar
                Box(
                    modifier = Modifier
                        .size(78.dp)
                        .background(
                            color = Color(0xFFB5E3E1),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👩🏻",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Spacer(
                    modifier = Modifier.width(16.dp)
                )

                // Nombre y correo
                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Manuela",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.width(7.dp)
                        )

                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .background(
                                    color = Color(0xFF3C9360),
                                    shape = CircleShape
                                )
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "manuela@email.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6F6F6F)
                    )
                }

                // Botón editar
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFB5E3E1),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(
                            horizontal = 14.dp,
                            vertical = 10.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Editar",
                        color = Color(0xFF227C78),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
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

            // ========================================================
            // MI CUENTA
            // ========================================================

            Text(
                text = "Mi cuenta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            ProfileSectionCard {

                ProfileOption(
                    icon = "👤",
                    title = "Información personal",
                    description = "Nombre, correo y datos de contacto"
                )

                ProfileDivider()

                ProfileOption(
                    icon = "🐾",
                    title = "Mis mascotas",
                    description = "Administra la información de tus mascotas"
                )

                ProfileDivider()

                ProfileOption(
                    icon = "❤️",
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
                    icon = "🔔",
                    title = "Notificaciones",
                    description = "Gestiona tus avisos y recordatorios"
                )

                ProfileDivider()

                ProfileOption(
                    icon = "🔒",
                    title = "Privacidad y seguridad",
                    description = "Controla la seguridad de tu cuenta"
                )

                ProfileDivider()

                ProfileOption(
                    icon = "❓",
                    title = "Ayuda y soporte",
                    description = "Encuentra respuestas y contacta con nosotros"
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )
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
    icon: String,
    title: String,
    description: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Icono
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = Color(0xFFF0F8F7),
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(
            modifier = Modifier.width(14.dp)
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