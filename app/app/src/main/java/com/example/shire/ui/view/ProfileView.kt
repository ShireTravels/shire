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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shire.domain.model.ThemeOption
import com.example.shire.ui.theme.ShireTheme
import com.example.shire.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()

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
                    val prefs = preferences
                    if (prefs != null) {
                        PreferenceChoiceItem(
                            icon = Icons.Default.Language,
                            iconTint = MaterialTheme.colorScheme.primary,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            title = "Idioma",
                            subtitle = "Idioma de la interfaz",
                            value = prefs.language.label,
                            options = viewModel.languageOptions.map { it.label },
                            onOptionSelected = { label ->
                                viewModel.languageOptions.find { it.label == label }
                                    ?.let(viewModel::updateLanguage)
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        PreferenceChoiceItem(
                            icon = Icons.Default.AttachMoney,
                            iconTint = MaterialTheme.colorScheme.secondary,
                            iconBg = MaterialTheme.colorScheme.secondaryContainer,
                            title = "Moneda",
                            subtitle = "Para presupuestos",
                            value = prefs.currency.label,
                            options = viewModel.currencyOptions.map { it.label },
                            onOptionSelected = { label ->
                                viewModel.currencyOptions.find { it.label == label }
                                    ?.let(viewModel::updateCurrency)
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        PreferenceChoiceItem(
                            icon = Icons.Default.DateRange,
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                            title = "Formato fecha",
                            subtitle = "",
                            value = prefs.dateFormat.label,
                            options = viewModel.dateFormatOptions.map { it.label },
                            onOptionSelected = { label ->
                                viewModel.dateFormatOptions.find { it.label == label }
                                    ?.let(viewModel::updateDateFormat)
                            }
                        )
                    }
                }
            }

            // Apariencia
            item {
                PreferenceSectionTitle("APARIENCIA")
                PreferenceCard {
                    val prefs = preferences
                    if (prefs != null) {
                        PreferenceSwitchItem(
                            icon = Icons.Default.DarkMode,
                            iconTint = MaterialTheme.colorScheme.primary,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            title = "Modo oscuro",
                            subtitle = "Tema de la aplicación",
                            checked = prefs.theme == ThemeOption.DARK,
                            onCheckedChange = { enabled ->
                                viewModel.updateTheme(
                                    if (enabled) ThemeOption.DARK else ThemeOption.LIGHT
                                )
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        PreferenceValueItem(
                            icon = Icons.Default.FormatSize,
                            iconTint = MaterialTheme.colorScheme.primary,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            title = "Tamaño de texto",
                            subtitle = "Ajuste de accesibilidad",
                            value = "Normal"
                        )
                    }
                }
            }

            // Notificaciones
            item {
                PreferenceSectionTitle("NOTIFICACIONES")
                PreferenceCard {
                    val prefs = preferences
                    if (prefs != null) {
                        PreferenceSwitchItem(
                            icon = Icons.Default.Notifications,
                            iconTint = MaterialTheme.colorScheme.primary,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            title = "Recordatorios de viaje",
                            subtitle = "Aviso 24h antes del vuelo",
                            checked = prefs.tripRemindersEnabled,
                            onCheckedChange = viewModel::updateTripReminders
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        PreferenceSwitchItem(
                            icon = Icons.Default.Email,
                            iconTint = MaterialTheme.colorScheme.primary,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            title = "Resumen semanal",
                            subtitle = "Email con próximos viajes",
                            checked = prefs.weeklySummaryEnabled,
                            onCheckedChange = viewModel::updateWeeklySummary
                        )
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
fun PreferenceChoiceItem(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    value: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
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

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.align(Alignment.BottomEnd)
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

@Composable
fun PreferenceValueItem(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
        Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
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
