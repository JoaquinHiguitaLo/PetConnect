package com.example.petconnect.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.petconnect.ui.home.HomeScreen

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    var selectedItem by remember {
        mutableStateOf(0)
    }

    Scaffold(
        bottomBar = {
            BottomNav(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) { innerPadding ->

        when (selectedItem) {

            0 -> HomeScreen(
                onLogout = onLogout,
                modifier = Modifier.padding(innerPadding)
            )

            1 -> MapScreen(
                modifier = Modifier.padding(innerPadding)
            )

            2 -> PetsScreen(
                modifier = Modifier.padding(innerPadding),
                onAddPet = {
                    // Por ahora no navegamos.
                }
            )

            3 -> AIScreen(
                modifier = Modifier.padding(innerPadding)
            )

            4 -> ProfileScreen(
                modifier = Modifier.padding(innerPadding),
                onLogout = onLogout
            )
        }
    }
}