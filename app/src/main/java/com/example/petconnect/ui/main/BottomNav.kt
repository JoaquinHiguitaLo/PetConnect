
package com.example.petconnect.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.petconnect.R

data class BottomNavItem(
    val label: String,
    val iconRes: Int
)

private val navItems = listOf(
    BottomNavItem("Inicio", R.drawable.nav_inicio),
    BottomNavItem("Mapa", R.drawable.nav_mapa),
    BottomNavItem("Mascotas", R.drawable.nav_mascotas),
    BottomNavItem("IA", R.drawable.nav_ia),
    BottomNavItem("Perfil", R.drawable.nav_perfil)
)

@Composable
fun BottomNav(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar {

        navItems.forEachIndexed { index, item ->

            NavigationBarItem(
                selected = selectedItem == index,

                onClick = {
                    onItemSelected(index)
                },


                icon = {
                    Image(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        modifier = Modifier.size(34.dp)
                    )
                },

                alwaysShowLabel = false

            )
        }
    }
}
