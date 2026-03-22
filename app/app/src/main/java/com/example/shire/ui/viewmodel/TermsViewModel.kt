package com.example.shire.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shire.domain.model.CurrencyOption
import com.example.shire.domain.model.DateFormatOption
import com.example.shire.domain.model.LanguageOption
import com.example.shire.domain.model.Preferences
import com.example.shire.domain.model.ThemeOption
import com.example.shire.domain.repository.ProfilePreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val preferencesRepository: ProfilePreferencesRepository
) : ViewModel() {

    private val _preferences = MutableStateFlow<Preferences?>(null)
    val preferences: StateFlow<Preferences?> = _preferences.asStateFlow()

    val languageOptions: List<LanguageOption> = LanguageOption.entries
    val currencyOptions: List<CurrencyOption> = CurrencyOption.entries
    val dateFormatOptions: List<DateFormatOption> = DateFormatOption.entries
    val themeOptions: List<ThemeOption> = ThemeOption.entries

    init {
        viewModelScope.launch {
            preferencesRepository.profilePreferencesFlow.collect { prefs ->
                _preferences.value = prefs
            }
        }
    }

    fun updateLanguage(language: LanguageOption) {
        viewModelScope.launch {
            preferencesRepository.setLanguage(language)
        }
    }

    fun updateCurrency(currency: CurrencyOption) {
        viewModelScope.launch {
            preferencesRepository.setCurrency(currency)
        }
    }

    fun updateDateFormat(dateFormat: DateFormatOption) {
        viewModelScope.launch {
            preferencesRepository.setDateFormat(dateFormat)
        }
    }

    fun updateTheme(theme: ThemeOption) {
        viewModelScope.launch {
            preferencesRepository.setTheme(theme)
        }
    }

    fun updateTripReminders(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setTripRemindersEnabled(enabled)
        }
    }

    fun updateWeeklySummary(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setWeeklySummaryEnabled(enabled)
        }
    }
}

