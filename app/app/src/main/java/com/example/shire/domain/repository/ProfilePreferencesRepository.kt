package com.example.shire.domain.repository

import com.example.shire.domain.model.CurrencyOption
import com.example.shire.domain.model.DateFormatOption
import com.example.shire.domain.model.LanguageOption
import com.example.shire.domain.model.Preferences
import com.example.shire.domain.model.TextSizeOption
import com.example.shire.domain.model.ThemeOption
import kotlinx.coroutines.flow.Flow



interface ProfilePreferencesRepository {
    val profilePreferencesFlow: Flow<Preferences>
    val userFlow: Flow<com.example.shire.domain.model.User?>
    suspend fun setLanguage(language: LanguageOption)
    suspend fun setCurrency(currency: CurrencyOption)
    suspend fun setDateFormat(dateFormat: DateFormatOption)
    suspend fun setTheme(theme: ThemeOption)
    suspend fun setTextSize(textSize: TextSizeOption)
    suspend fun setTripRemindersEnabled(enabled: Boolean)
    suspend fun setWeeklySummaryEnabled(enabled: Boolean)
    suspend fun setTermsAccepted(accepted: Boolean)
    suspend fun setUsername(username: String)
    suspend fun setDateOfBirth(dateOfBirth: String)
    suspend fun setLogin(login: String)
    suspend fun setAddress(address: String)
    suspend fun setCountry(country: String)
    suspend fun setPhone(phone: String)
    suspend fun setReceiveEmails(enabled: Boolean)
}
