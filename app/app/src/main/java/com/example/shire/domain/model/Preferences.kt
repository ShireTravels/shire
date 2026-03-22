package com.example.shire.domain.model

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import java.util.Date

//Definim els tipus de preferencies que hi pot haver
enum class LanguageOption(val id: String, val label: String) {
    SPANISH("es", "Español"),
    ENGLISH("en", "English"),
    CATALAN("ca", "Català"),
}

enum class CurrencyOption(val id: String, val label: String) {
    EUR("EUR", "EUR (€)"),
    USD("USD", "USD ($)"),
    GBP("GBP", "GBP (£)"),
    JPY("JPY", "JPY (¥)")
}

enum class DateFormatOption(val id: String, val label: String) {
    DD_MM_YYYY("dd_mm_yyyy", "DD/MM/YYYY"),
    MM_DD_YYYY("mm_dd_yyyy", "MM/DD/YYYY"),
    YYYY_MM_DD("yyyy_mm_dd", "YYYY-MM-DD")
}

enum class ThemeOption(val id: String, val label: String) {
    LIGHT("light", "Claro"),
    DARK("dark", "Oscuro")
}


data class Preferences(
    val language: LanguageOption,
    val currency: CurrencyOption,
    val dateFormat: DateFormatOption,
    val theme: ThemeOption,
    val tripRemindersEnabled: Boolean,
    val weeklySummaryEnabled: Boolean,
    val termsAccepted: Boolean?
)