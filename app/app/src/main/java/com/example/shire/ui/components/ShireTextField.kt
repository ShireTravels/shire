package com.example.shire.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ShireTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    // NUEVOS PARÁMETROS
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    // Si pasamos un onClick, usamos un Box para capturar el toque por encima
    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, style = MaterialTheme.typography.labelLarge) },
            placeholder = placeholder?.let { { Text(it, style = MaterialTheme.typography.bodyMedium) } },
            modifier = Modifier.fillMaxWidth(), // El modifier del Box ya maneja el tamaño externo
            leadingIcon = leadingIcon?.let {
                { Icon(it, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
            },
            shape = RoundedCornerShape(12.dp),
            enabled = enabled,
            // AJUSTE DE COLORES: Para que cuando enabled = false se vea con colores normales
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = MaterialTheme.colorScheme.primary,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        // Capa invisible que detecta el clic si onClick no es nulo
        if (onClick != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0f) // Invisible
                    .clickable { onClick() }
            )
        }
    }
}