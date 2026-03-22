package com.example.shire.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shire.ui.theme.ShireTheme
import com.example.shire.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(
    onNavigateUp: () -> Unit,
    onAcceptTerms: () -> Unit = {},
    onRejectTerms: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val preferences = viewModel.preferences.collectAsStateWithLifecycle().value
    val termsStateText = when (preferences?.termsAccepted) {
        true -> "Aceptados"
        false -> "Rechazados"
        null -> "Pendientes"
    }

    val termsStateColor = when (preferences?.termsAccepted) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.error
        null -> MaterialTheme.colorScheme.tertiary
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            "Términos y condiciones", 
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Información legal y privacidad", 
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, 
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                tonalElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .windowInsetsPadding(WindowInsets.navigationBars),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.updateTermsAccepted(false)
                            onRejectTerms()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Rechazar")
                    }

                    Button(
                        onClick = {
                            viewModel.updateTermsAccepted(true)
                            onAcceptTerms()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Aceptar")
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

                SectionTitle(title = "📋 Términos y condiciones")

                Surface(
                    color = termsStateColor.copy(alpha = 0.12f),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Estado actual: $termsStateText",
                        color = termsStateColor,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TermsSection(
                    title = "1. 📄 Aceptación de los términos",
                    content = "Al utilizar Travel Planner aceptas estos términos y condiciones. Si no estás de acuerdo con alguna de estas condiciones, no debes usar la aplicación."
                )
                TermsSection(
                    title = "2. 🔐 Privacidad y datos",
                    content = "Travel Planner recopila únicamente los datos necesarios para el funcionamiento de la app: nombre de usuario, destinos guardados e itinerarios. No compartimos tus datos con terceros sin tu consentimiento explícito."
                )
                TermsSection(
                    title = "3. 🛠️ Uso de la aplicación",
                    content = "Travel Planner es una herramienta de planificación personal. La información mostrada es orientativa. Verifica siempre los datos con los proveedores oficiales antes de realizar reservas."
                )
                TermsSection(
                    title = "4. 🌐 Servicios de terceros",
                    content = "La aplicación puede integrar servicios externos como Google Maps o APIs de vuelos. El uso de estos servicios está sujeto a sus propias políticas de privacidad y términos de uso."
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun TermsSection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
