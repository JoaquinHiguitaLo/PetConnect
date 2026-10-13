package com.example.petconnect.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petconnect.data.model.Category
import com.example.petconnect.data.model.petConnectCategories
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.petconnect.R

@Composable
fun CategoriesScreen(
    onBack: () -> Unit,
    onCategorySelected: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
    ) {

        // ============================================================
        // ENCABEZADO
        // ============================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver"
                )
            }

            Text(
                text = "Categorías de servicios",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // ============================================================
        // DESCRIPCIÓN
        // ============================================================

        Text(
            text = "Encuentra el servicio que necesitas para tu mascota.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // ============================================================
        // LISTA DE CATEGORÍAS
        // ============================================================

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 2.dp,
                end = 2.dp,
                bottom = 32.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(petConnectCategories) { category ->

                CategoryCard(
                    category = category,
                    onClick = {
                        onCategorySelected(category.id)
                    }
                )
            }
        }
    }
}


/**
 * Tarjeta individual de una categoría.
 */

@Composable
private fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    // Relacionamos cada categoría con su imagen en drawable.
    val imageRes = when (category.id) {
        "veterinaria" -> R.drawable.veterinaria
        "peluqueria" -> R.drawable.peluqueria
        "bano" -> R.drawable.bano
        "guarderia" -> R.drawable.guarderia
        "paseadores" -> R.drawable.paseadores
        "adiestramiento" -> R.drawable.adiestramiento
        "tiendas" -> R.drawable.tienda
        "parques" -> R.drawable.parque

        // Farmacias todavía no tiene una ilustración propia.
        else -> null
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = category.nombre,
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                // Conservamos el emoji para categorías sin imagen.
                Text(
                    text = category.icono,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = category.nombre,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
