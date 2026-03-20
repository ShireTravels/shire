package com.example.shire.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shire.domain.repository.CurrencyOption
import com.example.shire.domain.repository.DateFormatOption
import com.example.shire.domain.repository.LanguageOption
import com.example.shire.domain.repository.ThemeOption
import com.example.shire.ui.theme.ShireTheme
import com.example.shire.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showDateFormatDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 48.dp)
                ) {
                    Column {
                        Text(
                            text = "Preferencias",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Personaliza tu experiencia en Travel Planner",
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Idioma y Región
            item {
                PreferenceSectionTitle("IDIOMA Y REGIÓN")
                PreferenceCard {
                    PreferenceChoiceItem(
                        icon = Icons.Default.Language,
                        iconTint = MaterialTheme.colorScheme.primary,
                        iconBg = MaterialTheme.colorScheme.primaryContainer,
                        title = "Idioma",
                        subtitle = "Idioma de la interfaz",
                        value = uiState.selectedLanguage.label,
                        onClick = { showLanguageDialog = true }
                    )
                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    PreferenceChoiceItem(
                        icon = Icons.Default.AttachMoney,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        iconBg = MaterialTheme.colorScheme.secondaryContainer,
                        title = "Moneda",
                        subtitle = "Para presupuestos",
                        value = uiState.selectedCurrency.label,
                        onClick = { showCurrencyDialog = true }
                    )
                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    PreferenceChoiceItem(
                        icon = Icons.Default.DateRange,
                        iconTint = MaterialTheme.colorScheme.tertiary,
                        iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                        title = "Formato fecha",
                        subtitle = "",
                        value = uiState.selectedDateFormat.label,
                        onClick = { showDateFormatDialog = true }
                    )
                }
            }

            // Apariencia
            item {
                PreferenceSectionTitle("APARIENCIA")
                PreferenceCard {
                    PreferenceSwitchItem(
                        icon = Icons.Default.DarkMode,
                        iconTint = MaterialTheme.colorScheme.primary,
                        iconBg = MaterialTheme.colorScheme.primaryContainer,
                        title = "Modo oscuro",
                        subtitle = "Tema de la aplicación",
                        checked = uiState.selectedTheme == ThemeOption.DARK,
                        onCheckedChange = viewModel::setDarkModeEnabled
                    )
                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    PreferenceChoiceItem(
                        icon = Icons.Default.FormatSize,
                        iconTint = MaterialTheme.colorScheme.primary,
                        iconBg = MaterialTheme.colorScheme.primaryContainer,
                        title = "Tamaño de texto",
                        subtitle = "Ajuste de accesibilidad",
                        value = "Normal",
                        onClick = {}
                    )
                }
            }

            // Notificaciones
            item {
                PreferenceSectionTitle("NOTIFICACIONES")
                PreferenceCard {
                    PreferenceSwitchItem(
                        icon = Icons.Default.Notifications,
                        iconTint = MaterialTheme.colorScheme.primary,
                        iconBg = MaterialTheme.colorScheme.primaryContainer,
                        title = "Recordatorios de viaje",
                        subtitle = "Aviso 24h antes del vuelo",
                        checked = uiState.tripRemindersEnabled,
                        onCheckedChange = viewModel::setTripRemindersEnabled
                    )
                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    PreferenceSwitchItem(
                        icon = Icons.Default.Email,
                        iconTint = MaterialTheme.colorScheme.primary,
                        iconBg = MaterialTheme.colorScheme.primaryContainer,
                        title = "Resumen semanal",
                        subtitle = "Email con próximos viajes",
                        checked = uiState.weeklySummaryEnabled,
                        onCheckedChange = viewModel::setWeeklySummaryEnabled
                    )
                }
            }

            if (uiState.errorMessage != null) {
                item {
                    Text(
                        text = uiState.errorMessage!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                    LaunchedEffect(uiState.errorMessage) {
                        viewModel.clearError()
                    }
                }
            }
            
            // Acerca de
            item {
                PreferenceSectionTitle("ACERCA DE")
                PreferenceCard {
                    PreferenceNavItem(Icons.Default.Info, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer, "Sobre la app", "Información y equipo") {
                        onNavigate("about")
                    }
                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    PreferenceNavItem(Icons.Default.Description, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer, "Términos y Condiciones", "Legal y privacidad") {
                        onNavigate("terms")
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        if (showLanguageDialog) {
            PreferenceSelectionDialog(
                title = "Selecciona idioma",
                options = uiState.availableLanguages,
                selectedOption = uiState.selectedLanguage,
                optionLabel = { it.label },
                onDismiss = { showLanguageDialog = false },
                onSelect = {
                    viewModel.selectLanguage(it)
                    showLanguageDialog = false
                }
            )
        }

        if (showCurrencyDialog) {
            PreferenceSelectionDialog(
                title = "Selecciona moneda",
                options = uiState.availableCurrencies,
                selectedOption = uiState.selectedCurrency,
                optionLabel = { it.label },
                onDismiss = { showCurrencyDialog = false },
                onSelect = {
                    viewModel.selectCurrency(it)
                    showCurrencyDialog = false
                }
            )
        }

        if (showDateFormatDialog) {
            PreferenceSelectionDialog(
                title = "Selecciona formato de fecha",
                options = uiState.availableDateFormats,
                selectedOption = uiState.selectedDateFormat,
                optionLabel = { it.label },
                onDismiss = { showDateFormatDialog = false },
                onSelect = {
                    viewModel.selectDateFormat(it)
                    showDateFormatDialog = false
                }
            )
        }
    }
}

@Composable
fun PreferenceCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        content = content
    )
}

@Composable
fun PreferenceSectionTitle(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun <T> PreferenceSelectionDialog(
    title: String,
    options: List<T>,
    selectedOption: T,
    optionLabel: (T) -> String,
    onDismiss: () -> Unit,
    onSelect: (T) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            Column {
                options.forEach { option ->
                    val isSelected = option == selectedOption
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(option) }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = optionLabel(option),
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun PreferenceChoiceItem(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreferenceIcon(icon, iconTint, iconBg)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            if (subtitle.isNotEmpty()) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun PreferenceSwitchItem(icon: ImageVector, iconTint: Color, iconBg: Color, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreferenceIcon(icon, iconTint, iconBg)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            if (subtitle.isNotEmpty()) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Switch(
            checked = checked, 
            onCheckedChange = onCheckedChange, 
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary, 
                checkedTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun PreferenceNavItem(icon: ImageVector, iconTint: Color, iconBg: Color, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreferenceIcon(icon, iconTint, iconBg)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            if (subtitle.isNotEmpty()) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}


@Composable
fun PreferenceIcon(icon: ImageVector, tint: Color, bg: Color) {
    Surface(
        modifier = Modifier.size(36.dp),
        shape = CircleShape,
        color = bg
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ShireTheme {
        ProfileScreen(onNavigate = {})
    }
}
