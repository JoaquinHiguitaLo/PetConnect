package com.example.petconnect.ui.main

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

data class BottomNavItem(
    val label: String,
    val icon: String
)

private val navItems = listOf(
    BottomNavItem("Inicio", "🏠"),
    BottomNavItem("Mapa", "🗺️"),
    BottomNavItem("Mascotas", "🐾"),
    BottomNavItem("IA", "✨"),
    BottomNavItem("Perfil", "👤")
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
                    Text(
                        text = item.icon
                    )
                },
                label = {
                    Text(
                        text = item.label
                    )
                }
            )
        }
    }
}