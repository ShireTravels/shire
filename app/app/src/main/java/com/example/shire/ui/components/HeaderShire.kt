package com.example.shire.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderShire(
    selectedCategory: String,
    onCategoryClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Shire",
            color = Color(0xFF0052CC),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CategoryButton("Hoteles", selectedCategory == "Hoteles", Modifier.weight(1f)) {
                onCategoryClick("Hoteles")
            }
            CategoryButton("Vuelos", selectedCategory == "Vuelos", Modifier.weight(1f)) {
                onCategoryClick("Vuelos")
            }
            CategoryButton("Alquiler", selectedCategory == "Alquiler", Modifier.weight(1f)) {
                onCategoryClick("Alquiler")
            }
        }
    }
}

@Composable
fun CategoryButton(
    label: String,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF1A73E8) else Color(0xFFF1F3F4),
            contentColor = if (isSelected) Color.White else Color.Gray
        )
    ) {
        Text(text = label, maxLines = 1, fontSize = 11.sp)
    }
}
