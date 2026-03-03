package com.example.shire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shire.ui.theme.ShireTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(onNavigateUp: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Términos y Condiciones", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Última actualización: 1 de marzo de 2026", fontSize = 12.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 16.dp, 
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onNavigateUp) {
                        Text("Rechazar", color = Color.Gray, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = onNavigateUp,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006CE4)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Aceptar y continuar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                TermsSection(
                    title = "1. \uD83D\uDCDC Aceptación de los términos",
                    content = "Al utilizar Travel Planner aceptas estos términos y condiciones. Si no estás de acuerdo con alguna de estas condiciones, no debes usar la aplicación."
                )
                TermsSection(
                    title = "2. \uD83D\uDD12 Privacidad y datos",
                    content = "Travel Planner recopila únicamente los datos necesarios para el funcionamiento de la app: nombre de usuario, destinos guardados e itinerarios. No compartimos tus datos con terceros sin tu consentimiento explícito."
                )
                TermsSection(
                    title = "3. \uD83D\uDEE0️ Uso de la aplicación",
                    content = "Travel Planner es una herramienta de planificación personal. La información mostrada es orientativa. Verifica siempre los datos con los proveedores oficiales antes de realizar reservas."
                )
                TermsSection(
                    title = "4. \uD83C\uDF10 Servicios de terceros",
                    content = "La aplicación puede integrar servicios externos como Google Maps o APIs de vuelos. El uso de estos servicios está sujeto a sus propias políticas de privacidad y términos de uso."
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun TermsSection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            fontSize = 14.sp,
            color = Color.Gray,
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermsScreenPreview() {
    ShireTheme {
        TermsScreen(onNavigateUp = {})
    }
}
