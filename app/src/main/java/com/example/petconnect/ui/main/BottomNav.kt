
package com.example.petconnect.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petconnect.R
import androidx.compose.foundation.clickable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue

private val VerdePetConnect = Color(0xFF227C78)
private val VerdeClaro = Color(0xFFE2F2EE)

data class BottomNavItem(
    val label: String,
    val iconNormal: Int,
    val iconSelected: Int
)

private val navItems = listOf(
    BottomNavItem(
        "Inicio",
        R.drawable.nav_inicio_gris,
        R.drawable.nav_inicio_verde
    ),
    BottomNavItem(
        "Mapa",
        R.drawable.nav_mapa_gris,
        R.drawable.nav_mapa_verde
    ),
    BottomNavItem(
        "Mascotas",
        R.drawable.nav_mascotas_gris,
        R.drawable.nav_mascotas_verde
    ),
    BottomNavItem(
        "IA",
        R.drawable.nav_ia_gris,
        R.drawable.nav_ia_verde
    ),
    BottomNavItem(
        "Perfil",
        R.drawable.nav_perfil_gris,
        R.drawable.nav_perfil_verde
    )
)

@Composable
fun BottomNav(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 14.dp,
                vertical = 8.dp
            ),
        shape = RoundedCornerShape(32.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEachIndexed { index, item ->

                val seleccionado = selectedItem == index

                val fondo by animateColorAsState(
                    targetValue = if (seleccionado) {
                        VerdeClaro
                    } else {
                        Color.Transparent
                    },
                    animationSpec = tween(250),
                    label = "navbar_fondo"
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(fondo)
                        .clickable {
                            onItemSelected(index)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(
                            id = if (seleccionado) {
                                item.iconSelected
                            } else {
                                item.iconNormal
                            }
                        ),
                        contentDescription = item.label,
                        modifier = Modifier.size(30.dp)
                    )

                }
            }
        }
    }
}
