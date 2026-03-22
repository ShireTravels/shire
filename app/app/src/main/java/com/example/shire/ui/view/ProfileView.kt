package com.example.shire.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.layout.onGloballyPositioned
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.res.stringResource
import com.example.shire.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shire.domain.model.TextSizeOption
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
                            text = stringResource(id = R.string.preferences_title),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.preferences_subtitle),
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Información Personal
            item {
                PreferenceSectionTitle(stringResource(id = R.string.personal_info_section))
                PreferenceCard {
                    val prefs = preferences
                    if (prefs != null) {
                        OutlinedTextField(
                            value = prefs.username,
                            onValueChange = viewModel::updateUsername,
                            label = { Text(stringResource(id = R.string.username_label)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = prefs.dateOfBirth,
                            onValueChange = viewModel::updateDateOfBirth,
                            label = { Text(stringResource(id = R.string.dob_label)) },
                            placeholder = { Text(stringResource(id = R.string.dob_placeholder)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            item {
                PreferenceSectionTitle(stringResource(id = R.string.app_language_section))
                LanguageSelectorRow(
                    currentLanguage = viewModel.getCurrentLanguage(),
                    onLanguageChange = { code -> viewModel.changeLanguage(code) }
                )
            }

            // Idioma y Región
            item {
                PreferenceSectionTitle(stringResource(id = R.string.language_region_section))
                PreferenceCard {
                    val prefs = preferences
                    if (prefs != null) {
                        PreferenceChoiceItem(
                            icon = Icons.Default.Language,
                            iconTint = MaterialTheme.colorScheme.primary,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            title = stringResource(id = R.string.language_label),
                            subtitle = stringResource(id = R.string.interface_language_desc),
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
                            title = stringResource(id = R.string.currency_label),
                            subtitle = stringResource(id = R.string.budget_currency_desc),
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
                            title = stringResource(id = R.string.date_format_label),
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
                PreferenceSectionTitle(stringResource(id = R.string.appearance_section))
                PreferenceCard {
                    val prefs = preferences
                    if (prefs != null) {
                        PreferenceSwitchItem(
                            icon = Icons.Default.DarkMode,
                            iconTint = MaterialTheme.colorScheme.primary,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            title = stringResource(id = R.string.dark_mode_label),
                            subtitle = stringResource(id = R.string.app_theme_desc),
                            checked = prefs.theme == ThemeOption.DARK,
                            onCheckedChange = { enabled ->
                                viewModel.updateTheme(
                                    if (enabled) ThemeOption.DARK else ThemeOption.LIGHT
                                )
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        PreferenceChoiceItem(
                            icon = Icons.Default.FormatSize,
                            iconTint = MaterialTheme.colorScheme.primary,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            title = stringResource(id = R.string.text_size_label),
                            subtitle = stringResource(id = R.string.accessibility_adjustment_desc),
                            value = prefs.textSize.label,
                            options = viewModel.textSizeOptions.map { it.label },
                            onOptionSelected = { label ->
                                viewModel.textSizeOptions.find { it.label == label }
                                    ?.let(viewModel::updateTextSize)
                            }
                        )
                    }
                }
            }

            // Notificaciones
            item {
                PreferenceSectionTitle(stringResource(id = R.string.notifications_section))
                PreferenceCard {
                    val prefs = preferences
                    if (prefs != null) {
                        PreferenceSwitchItem(
                            icon = Icons.Default.Notifications,
                            iconTint = MaterialTheme.colorScheme.primary,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            title = stringResource(id = R.string.trip_reminders_label),
                            subtitle = stringResource(id = R.string.flight_reminder_desc),
                            checked = prefs.tripRemindersEnabled,
                            onCheckedChange = viewModel::updateTripReminders
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        PreferenceSwitchItem(
                            icon = Icons.Default.Email,
                            iconTint = MaterialTheme.colorScheme.primary,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            title = stringResource(id = R.string.weekly_summary_label),
                            subtitle = stringResource(id = R.string.upcoming_trips_email_desc),
                            checked = prefs.weeklySummaryEnabled,
                            onCheckedChange = viewModel::updateWeeklySummary
                        )
                    }
                }
            }
            
            // Acerca de
            item {
                PreferenceSectionTitle(stringResource(id = R.string.about_section))
                PreferenceCard {
                    PreferenceNavItem(Icons.Default.Info, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer, stringResource(id = R.string.about_app_label), stringResource(id = R.string.info_team_desc)) {
                        onNavigate("about")
                    }
                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    PreferenceNavItem(Icons.Default.Description, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer, stringResource(id = R.string.terms_conditions_label), stringResource(id = R.string.legal_privacy_desc)) {
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
    var anchorWidthPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .onGloballyPositioned { coordinates ->
                    anchorWidthPx = coordinates.size.width
                }
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
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .width(with(density) { anchorWidthPx.toDp() })
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

@Composable
fun LanguageSelectorRow(
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    val languages = listOf(
        "es" to "Español",
        "en" to "English",
        "ca" to "Català"
    )

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            languages.forEach { (code, name) ->
                val isSelected = currentLanguage == code
                Surface(
                    modifier = Modifier.weight(1f).padding(4.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    onClick = { onLanguageChange(code) }
                ) {
                    Text(
                        text = name,
                        modifier = Modifier.padding(vertical = 12.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
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
