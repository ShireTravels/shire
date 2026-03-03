package com.example.shire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shire.ui.theme.ShireTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onNavigateUp: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF0F172A), // Dark blueprint background
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo and Name
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF2196F3), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.FlightTakeoff, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Travel Planner",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "v1.0.0 - Sprint 01",
                    color = Color(0xFF64B5F6),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Plan your adventures, your way. Built\nwith ❤️ at Campus Igualada.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(48.dp))
            }

            // Team Section
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "EQUIPO DE DESARROLLO",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    TeamMember("AJ", "Alex Jordà", "Lead Developer - UI/UX", Color(0xFFFF9800))
                    Spacer(modifier = Modifier.height(16.dp))
                    TeamMember("MG", "Marta Gómez", "Backend - Data Model", Color(0xFF9C27B0))
                    Spacer(modifier = Modifier.height(16.dp))
                    TeamMember("PR", "Pau Roca", "Navigation - Testing", Color(0xFF4CAF50))
                }
                Spacer(modifier = Modifier.height(48.dp))
            }

            // Tech Info
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "INFORMACIÓN TÉCNICA",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Versión", color = Color.White, fontSize = 16.sp)
                        Text("1.0.0", color = Color.Gray, fontSize = 16.sp)
                    }
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.Gray.copy(alpha = 0.3f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Sprint", color = Color.White, fontSize = 16.sp)
                        Text("01", color = Color.Gray, fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun TeamMember(initials: String, name: String, role: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(48.dp).background(color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text(role, color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    ShireTheme {
        AboutScreen(onNavigateUp = {})
    }
}
