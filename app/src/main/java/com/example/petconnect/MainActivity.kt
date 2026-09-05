package com.example.petconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.petconnect.navigation.AppNavigation
import com.example.petconnect.ui.home.HomeScreen
import com.example.petconnect.ui.login.LoginScreen
import com.example.petconnect.ui.register.RegisterScreen
import com.example.petconnect.ui.theme.PetConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetConnectTheme {
                AppNavigation()
            }
        }
    }
}
