package com.example.shire.domain.repository

import com.example.shire.domain.model.CurrencyOption
import com.example.shire.domain.model.DateFormatOption
import com.example.shire.domain.model.LanguageOption
import com.example.shire.domain.model.Preferences
import com.example.shire.domain.model.ThemeOption
import kotlinx.coroutines.flow.Flow



interface ProfilePreferencesRepository {
    val profilePreferencesFlow: Flow<Preferences>
    suspend fun setLanguage(language: LanguageOption)
    suspend fun setCurrency(currency: CurrencyOption)
    suspend fun setDateFormat(dateFormat: DateFormatOption)
    suspend fun setTheme(theme: ThemeOption)
    suspend fun setTripRemindersEnabled(enabled: Boolean)
    suspend fun setWeeklySummaryEnabled(enabled: Boolean)
    suspend fun setTermsAccepted(accepted: Boolean)
}
