package com.example.shire.ui.screens

import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.shire.ui.theme.ShireTheme

@Composable
fun HotelsSearch() {
    println("Hola")
}


@Preview(showBackground = true , showSystemUi = true)
@Composable
fun HotelsSearchPreview() {
    ShireTheme() {
        HotelsSearch()
    }
}