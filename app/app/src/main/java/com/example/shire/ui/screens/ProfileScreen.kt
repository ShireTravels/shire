package com.example.shire.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shire.ui.theme.ShireTheme

@Composable
fun ProfileScreen(onNavigate: (String) -> Unit) {
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F172A)) // Dark blue background for header
                        .padding(horizontal = 24.dp, vertical = 40.dp)
                ) {
                    Text(
                        text = "Preferencias",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Personaliza tu experiencia en Travel Planner",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }

            // Idioma y Región
            item {
                PreferenceSectionTitle("IDIOMA Y REGIÓN")
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column {
                        PreferenceChoiceItem(Icons.Default.Language, Color(0xFF2196F3), Color(0xFFE3F2FD), "Idioma", "Idioma de la interfaz", "es Español")
                        Divider(modifier = Modifier.padding(start = 56.dp), color = Color.LightGray.copy(alpha=0.3f))
                        PreferenceChoiceItem(Icons.Default.AttachMoney, Color(0xFF4CAF50), Color(0xFFE8F5E9), "Moneda", "Para presupuestos", "EUR (€)")
                        Divider(modifier = Modifier.padding(start = 56.dp), color = Color.LightGray.copy(alpha=0.3f))
                        PreferenceChoiceItem(Icons.Default.DateRange, Color(0xFF00BCD4), Color(0xFFE0F7FA), "Formato fecha", "", "DD/MM")
                    }
                }
            }

            // Apariencia
            item {
                PreferenceSectionTitle("APARIENCIA")
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    var isDarkMode by remember { mutableStateOf(false) }
                    
                    Column {
                        PreferenceSwitchItem(Icons.Default.DarkMode, Color(0xFF9C27B0), Color(0xFFF3E5F5), "Modo oscuro", "Tema de la aplicación", isDarkMode) { isDarkMode = it }
                        Divider(modifier = Modifier.padding(start = 56.dp), color = Color.LightGray.copy(alpha=0.3f))
                        PreferenceChoiceItem(Icons.Default.FormatSize, Color(0xFF3F51B5), Color(0xFFE8EAF6), "Tamaño de texto", "Ajuste de accesibilidad", "Normal")
                    }
                }
            }

            // Notificaciones
            item {
                PreferenceSectionTitle("NOTIFICACIONES")
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    var notifications1 by remember { mutableStateOf(true) }
                    var notifications2 by remember { mutableStateOf(false) }
                    
                    Column {
                        PreferenceSwitchItem(Icons.Default.Notifications, Color(0xFFFF9800), Color(0xFFFFF3E0), "Recordatorios de viaje", "Aviso 24h antes del vuelo", notifications1) { notifications1 = it }
                        Divider(modifier = Modifier.padding(start = 56.dp), color = Color.LightGray.copy(alpha=0.3f))
                        PreferenceSwitchItem(Icons.Default.Email, Color(0xFFE91E63), Color(0xFFFCE4EC), "Resumen semanal", "Email con próximos viajes", notifications2) { notifications2 = it }
                    }
                }
            }
            
            // Acerca de
            item {
                PreferenceSectionTitle("ACERCA DE")
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column {
                        PreferenceNavItem(Icons.Default.Info, Color(0xFF607D8B), Color(0xFFECEFF1), "Sobre la app", "Información y equipo") {
                            onNavigate("about")
                        }
                        Divider(modifier = Modifier.padding(start = 56.dp), color = Color.LightGray.copy(alpha=0.3f))
                        PreferenceNavItem(Icons.Default.Description, Color(0xFF795548), Color(0xFFEFEBE9), "Términos y Condiciones", "Legal y privacidad") {
                            onNavigate("terms")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp)) // Offset for bottom nav
            }
        }
    }
}

@Composable
fun PreferenceSectionTitle(title: String) {
    Text(
        text = title,
        color = Color.Gray,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun PreferenceChoiceItem(icon: ImageVector, iconTint: Color, iconBg: Color, title: String, subtitle: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreferenceIcon(icon, iconTint, iconBg)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            if (subtitle.isNotEmpty()) {
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, fontSize = 14.sp, color = Color.Gray)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun PreferenceSwitchItem(icon: ImageVector, iconTint: Color, iconBg: Color, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreferenceIcon(icon, iconTint, iconBg)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            if (subtitle.isNotEmpty()) {
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF2196F3)))
    }
}

@Composable
fun PreferenceNavItem(icon: ImageVector, iconTint: Color, iconBg: Color, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreferenceIcon(icon, iconTint, iconBg)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            if (subtitle.isNotEmpty()) {
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}


@Composable
fun PreferenceIcon(icon: ImageVector, tint: Color, bg: Color) {
    Box(
        modifier = Modifier.size(36.dp).background(bg, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ShireTheme {
        ProfileScreen(onNavigate = {})
    }
}
