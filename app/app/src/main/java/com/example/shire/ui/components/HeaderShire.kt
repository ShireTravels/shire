package com.example.shire.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HeaderShire(
    selectedCategory: String,
    onCategoryClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Crea un nuevo viaje",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
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
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(
            text = label, 
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1
        )
    }
}
