package com.example.shire.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationStepContent(
    destination: String,
    availableDestinations: List<String>,
    onDestinationChange: (String) -> Unit,
    startDate: String, onStartDateChange: (String) -> Unit,
    endDate: String, onEndDateChange: (String) -> Unit,
    minStartDateMillis: Long?,
    minEndDateMillis: Long?,
    adults: Int, onAdultsChange: (Int) -> Unit,
    children: Int, onChildrenChange: (Int) -> Unit
) {

    // Estados para controlar la visibilidad de los calendarios
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = destination,
                    onValueChange = {
                        onDestinationChange(it)
                        expanded = true
                    },
                    label = { Text("Destino", style = MaterialTheme.typography.bodyMedium) },
                    placeholder = { Text("País, ciudad o región", style = MaterialTheme.typography.bodyMedium) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                val filteredDestinations = availableDestinations.filter {
                    it.contains(destination, ignoreCase = true)
                }

                ExposedDropdownMenu(
                    expanded = expanded && filteredDestinations.isNotEmpty(),
                    onDismissRequest = { expanded = false }
                ) {
                    filteredDestinations.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                onDestinationChange(selectionOption)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Text(
                text = "Fechas del viaje",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ShireTextField(
                    value = startDate,
                    onValueChange = {},
                    label = "Ida",
                    placeholder = "dd/mm/aaaa",
                    modifier = Modifier.weight(1f),
                    leadingIcon = Icons.Default.DateRange,
                    enabled = false,
                    onClick = { showStartDatePicker = true }
                )
                ShireTextField(
                    value = endDate,
                    onValueChange = { },
                    label = "Vuelta",
                    placeholder = "dd/mm/aaaa",
                    modifier = Modifier.weight(1f),
                    leadingIcon = Icons.Default.DateRange,
                    enabled = false,
                    onClick = { showEndDatePicker = true }
                )

                if (showStartDatePicker) {
                    ShireDatePickerDialog(
                        minDateMillis = minStartDateMillis,
                        onDateSelected = { fecha ->
                            onStartDateChange(fecha)
                            showStartDatePicker = false
                        },
                        onDismiss = { showStartDatePicker = false }
                    )
                }

                if (showEndDatePicker) {
                    ShireDatePickerDialog(
                        minDateMillis = minEndDateMillis,
                        onDateSelected = { fecha ->
                            onEndDateChange(fecha)
                            showEndDatePicker = false
                        },
                        onDismiss = { showEndDatePicker = false }
                    )
                }
            }



            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Text(
                text = "Viajeros",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            TravelerCounter(
                label = "Adultos",
                subtitle = "Mayores de 12 años",
                count = adults,
                onCountChange = onAdultsChange,
                minCount = 1
            )
            TravelerCounter(
                label = "Niños",
                subtitle = "De 0 a 12 años",
                count = children,
                onCountChange = onChildrenChange,
                minCount = 0
            )
        }
    }
}