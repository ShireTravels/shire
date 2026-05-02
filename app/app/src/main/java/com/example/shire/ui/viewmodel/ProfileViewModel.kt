package com.example.shire.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.shire.domain.model.CurrencyOption
import com.example.shire.domain.model.DateFormatOption
import com.example.shire.domain.model.LanguageOption
import com.example.shire.domain.model.LoggedInUser
import com.example.shire.domain.model.Preferences
import com.example.shire.domain.model.TextSizeOption
import com.example.shire.domain.model.ThemeOption
import com.example.shire.domain.repository.AuthRepository
import com.example.shire.domain.repository.ProfilePreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
	private val preferencesRepository: ProfilePreferencesRepository,
	private val authRepository: AuthRepository
) : ViewModel() {

	private val _preferences = MutableStateFlow<Preferences?>(null)
	val preferences: StateFlow<Preferences?> = _preferences.asStateFlow()
	private val _loggedInUser = MutableStateFlow<LoggedInUser?>(null)
	val loggedInUser: StateFlow<LoggedInUser?> = _loggedInUser.asStateFlow()

	val languageOptions: List<LanguageOption> = LanguageOption.entries
	val currencyOptions: List<CurrencyOption> = CurrencyOption.entries
	val dateFormatOptions: List<DateFormatOption> = DateFormatOption.entries
	val textSizeOptions: List<TextSizeOption> = TextSizeOption.entries

	init {
		viewModelScope.launch {
			preferencesRepository.profilePreferencesFlow.collect { prefs ->
				_preferences.value = prefs
			}
		}

		viewModelScope.launch {
			authRepository.loggedInUserFlow.collect { user ->
				_loggedInUser.value = user
			}
		}
	}

	fun logout() {
		viewModelScope.launch {
			authRepository.logout()
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

	fun updateTextSize(textSize: TextSizeOption) {
		viewModelScope.launch {
			preferencesRepository.setTextSize(textSize)
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

	fun updateTermsAccepted(accepted: Boolean) {
		viewModelScope.launch {
			preferencesRepository.setTermsAccepted(accepted)
		}
	}

	fun updateUsername(username: String) {
		viewModelScope.launch {
			preferencesRepository.setUsername(username)
		}
	}

	fun updateDateOfBirth(newDateOfBirth: String) {
		viewModelScope.launch {
			preferencesRepository.setDateOfBirth(newDateOfBirth)
		}
	}

    fun changeLanguage(languageCode: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
    }

    fun getCurrentLanguage(): String {
        return AppCompatDelegate.getApplicationLocales().toLanguageTags().split("-")[0]
            .ifEmpty { java.util.Locale.getDefault().language }
    }
}
