package com.example.shire.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ShireCard(
    title: String,
    subtitle: String,
    price: String,
    modifier: Modifier = Modifier,
    priceSuffix: String = "",
    annotation: String? = null,
    icon: (@Composable () -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                
                if (annotation != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = annotation,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "$price$priceSuffix",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1A73E8),
                    fontWeight = FontWeight.Bold
                )
            }
            
            if (icon != null) {
                Box(modifier = Modifier.padding(start = 16.dp)) {
                    icon()
                }
            }
        }
    }
}
