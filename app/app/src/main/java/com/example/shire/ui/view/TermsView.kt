package com.example.shire.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shire.domain.model.CurrencyOption
import com.example.shire.domain.model.DateFormatOption
import com.example.shire.domain.model.LanguageOption
import com.example.shire.domain.model.ThemeOption
import com.example.shire.ui.theme.ShireTheme
import com.example.shire.ui.viewmodel.TermsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(
    onNavigateUp: () -> Unit,
    viewModel: TermsViewModel = hiltViewModel()
) {
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            "Configurar preferencias", 
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Selecciona tus opciones preferidas", 
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
                shadowElevation = 16.dp, 
                tonalElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .windowInsetsPadding(WindowInsets.navigationBars),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onNavigateUp) {
                        Text(
                            "Cancelar", 
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(
                        onClick = onNavigateUp,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar cambios", style = MaterialTheme.typography.labelLarge)
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
                
                // Sección de Preferencias
                SectionTitle(title = "⚙️ Preferencias")
                
                preferences?.let { prefs ->
                    // Idioma
                    PreferenceSelector(
                        title = "Idioma",
                        selectedValue = prefs.language.label,
                        options = viewModel.languageOptions.map { it.label },
                        onOptionSelected = { selectedLabel ->
                            val selectedLanguage = viewModel.languageOptions.find { it.label == selectedLabel }
                            selectedLanguage?.let { viewModel.updateLanguage(it) }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Moneda
                    PreferenceSelector(
                        title = "Moneda",
                        selectedValue = prefs.currency.label,
                        options = viewModel.currencyOptions.map { it.label },
                        onOptionSelected = { selectedLabel ->
                            val selectedCurrency = viewModel.currencyOptions.find { it.label == selectedLabel }
                            selectedCurrency?.let { viewModel.updateCurrency(it) }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Formato de fecha
                    PreferenceSelector(
                        title = "Formato de fecha",
                        selectedValue = prefs.dateFormat.label,
                        options = viewModel.dateFormatOptions.map { it.label },
                        onOptionSelected = { selectedLabel ->
                            val selectedFormat = viewModel.dateFormatOptions.find { it.label == selectedLabel }
                            selectedFormat?.let { viewModel.updateDateFormat(it) }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tema
                    PreferenceSelector(
                        title = "Tema",
                        selectedValue = prefs.theme.label,
                        options = viewModel.themeOptions.map { it.label },
                        onOptionSelected = { selectedLabel ->
                            val selectedTheme = viewModel.themeOptions.find { it.label == selectedLabel }
                            selectedTheme?.let { viewModel.updateTheme(it) }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sección de Notificaciones
                    SectionTitle(title = "🔔 Notificaciones")

                    PreferenceToggle(
                        title = "Recordatorios de viajes",
                        description = "Recibe notificaciones sobre tus viajes próximos",
                        isEnabled = prefs.tripRemindersEnabled,
                        onToggle = { viewModel.updateTripReminders(it) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PreferenceToggle(
                        title = "Resumen semanal",
                        description = "Recibe un resumen de tus actividades cada semana",
                        isEnabled = prefs.weeklySummaryEnabled,
                        onToggle = { viewModel.updateWeeklySummary(it) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sección de Términos
                    SectionTitle(title = "📋 Términos y Condiciones")

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
fun PreferenceSelector(
    title: String,
    selectedValue: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    text = selectedValue,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PreferenceToggle(
    title: String,
    description: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
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
