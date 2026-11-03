
package com.example.petconnect.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.petconnect.R

@Composable
fun PetConnectSplashScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F6)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo_oscuro),
            contentDescription = "Logo de PetConnect",
            modifier = Modifier.size(180.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(12.dp))

        Image(
            painter = painterResource(R.drawable.petconnect),
            contentDescription = "PetConnect",
            modifier = Modifier
                .width(240.dp)
                .height(65.dp),
            contentScale = ContentScale.Fit
        )
    }
}
