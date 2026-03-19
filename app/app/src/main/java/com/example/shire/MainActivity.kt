package com.example.shire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.shire.ui.theme.ShireTheme


import com.example.shire.ui.view.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShireTheme {
                // Surface es el contenedor base que aplica el color de fondo del tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    // Llamamos al orquestador de la navegación
                    AppNavigation()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppNAvigationPreview() {
    AppNavigation()
}