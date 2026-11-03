
package com.example.petconnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petconnect.data.model.Business
import androidx.compose.runtime.*
import com.example.petconnect.ui.components.PetConnectTopBar
import com.example.petconnect.data.model.petConnectCategories
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.petconnect.R

private val VerdeGestion = Color(0xFF227C78)
private val FondoGestion = Color(0xFFFAF9F6)


@Composable
fun ManageBusinessScreen(
    business: Business,
    onBack: () -> Unit,
    onEdit: (Business) -> Unit,
    onDelete: (Business) -> Unit = {},
    isDeleting: Boolean = false,
    isConnected: Boolean = true,
    deleteError: String? = null,
    modifier: Modifier = Modifier
) {
    // Controla la confirmación de eliminación.
    var mostrarDialogoEliminar by remember {
        mutableStateOf(false)
    }
    val imagenCategoria = when (business.categoria) {
        "veterinaria" -> R.drawable.cat_veterinaria
        "peluqueria" -> R.drawable.cat_peluqueria
        "bano" -> R.drawable.cat_bano
        "guarderia" -> R.drawable.cat_guarderia
        "paseadores" -> R.drawable.cat_paseadores
        "adiestramiento" -> R.drawable.cat_adiestramiento
        "parques" -> R.drawable.cat_parques
        "tiendas" -> R.drawable.cat_tienda
        "especialistas" -> R.drawable.cat_especialistas
        "hoteles_petfriendly" -> R.drawable.cat_hotelpet
        else -> R.drawable.cat_tienda
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoGestion)
    ) {


        PetConnectTopBar(
            onBack = {
                if (!isDeleting) {
                    onBack()
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Mi establecimiento",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = VerdeGestion,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 8.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // INFORMACIÓN PRINCIPAL
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(
                            Color(0xFFE4F2EF),
                            RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = imagenCategoria),
                        contentDescription = business.categoria,
                        modifier = Modifier.size(54.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Column {
                    Text(
                        text = business.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = petConnectCategories
                            .firstOrNull { it.id == business.categoria }
                            ?.nombre
                            ?: business.categoria,
                        color = Color(0xFF71817E),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = when (business.estadoVerificacion) {
                            "APROBADO" -> "✓ Aprobado"
                            "PENDIENTE" -> "Pendiente de aprobación"
                            "RECHAZADO" -> "Rechazado"
                            else -> business.estadoVerificacion
                        },
                        color = when (business.estadoVerificacion) {
                            "APROBADO" -> Color(0xFF2E7D46)
                            "PENDIENTE" -> Color(0xFFC9871F)
                            else -> Color(0xFFB3261E)
                        },
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // DETALLE
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Text(
                        text = "Información del establecimiento",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = VerdeGestion
                    )

                    HorizontalDivider()

                    DetalleCampo(
                        etiqueta = "Descripción",
                        valor = business.descripcion
                    )

                    DetalleCampo(
                        etiqueta = "Dirección",
                        valor = business.direccion
                    )

                    DetalleCampo(
                        etiqueta = "Teléfono",
                        valor = business.telefono
                    )

                    DetalleCampo(
                        etiqueta = "Latitud",
                        valor = business.latitud.toString()
                    )

                    DetalleCampo(
                        etiqueta = "Longitud",
                        valor = business.longitud.toString()
                    )
                }
            }
        }

        // ==========================================
        // ACCIONES FIJAS DEL ESTABLECIMIENTO
        // ==========================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(FondoGestion)
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 12.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = {
                    onEdit(business)
                },
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(48.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeGestion
                ),
                enabled = !isDeleting
            ) {
                Text(
                    text = "Editar establecimiento",
                    fontWeight = FontWeight.Bold
                )
            }


            HorizontalDivider(
                modifier = Modifier.padding(top = 4.dp),
                color = Color(0xFFE8E8E3)
            )

            TextButton(
                onClick = {
                    mostrarDialogoEliminar = true
                },
                enabled = !isDeleting,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                if (isDeleting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFFE0654F)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = if (isConnected) {
                            "Eliminando..."
                        } else {
                            "Esperando conexión..."
                        },
                        color = Color(0xFFE0654F)
                    )
                } else {
                    Text(
                        text = "Eliminar establecimiento",
                        color = Color(0xFFE0654F),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            deleteError?.let { error ->

                Text(
                    text = "No se pudo eliminar el establecimiento: $error",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (mostrarDialogoEliminar) {

        AlertDialog(
            onDismissRequest = {
                mostrarDialogoEliminar = false
            },

            title = {
                Text("Eliminar")
            },

            text = {
                Text(
                    "¿Estás seguro de que deseas eliminar " +
                            "\"${business.nombre}\"?\n\n" +
                            "Esta acción no se podrá deshacer."
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEliminar = false

                        // Solicitamos la eliminación a MainScreen.
                        // Todavía no ejecutamos Firestore.
                        onDelete(business)
                    }
                ) {
                    Text(
                        text = "Eliminar",
                        color = Color(0xFFE0654F),
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                    }
                ) {
                    Text("Cancelar")
                }
            },

            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

}


@Composable
private fun DetalleCampo(
    etiqueta: String,
    valor: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )

        Text(
            text = valor.ifBlank { "No registrado" },
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF253B38)
        )
    }
}
