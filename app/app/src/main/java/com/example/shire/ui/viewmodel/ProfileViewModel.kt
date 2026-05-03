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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@HiltViewModel
class ProfileViewModel @Inject constructor(
	private val preferencesRepository: ProfilePreferencesRepository,
	private val authRepository: AuthRepository
) : ViewModel() {

	private val _preferences = MutableStateFlow<Preferences?>(null)
	val preferences: StateFlow<Preferences?> = _preferences.asStateFlow()
	private val _loggedInUser = MutableStateFlow<LoggedInUser?>(null)
	val loggedInUser: StateFlow<LoggedInUser?> = _loggedInUser.asStateFlow()
	private val _fullUser = MutableStateFlow<com.example.shire.domain.model.User?>(null)
	val fullUser: StateFlow<com.example.shire.domain.model.User?> = _fullUser.asStateFlow()

	var login by mutableStateOf("")
	var username by mutableStateOf("")
	var birthdate by mutableStateOf("")
	var address by mutableStateOf("")
	var country by mutableStateOf("")
	var phone by mutableStateOf("")

	private var isInitialized = false
	private val updateJobs = mutableMapOf<String, Job>()

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

		viewModelScope.launch {
			preferencesRepository.userFlow.collect { user ->
				_fullUser.value = user
				if (!isInitialized && user != null) {
					login = user.login
					username = user.username
					birthdate = user.birthdate
					address = user.address
					country = user.country
					phone = user.phone
					isInitialized = true
				}
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

	fun updateUsername(newUsername: String) {
		username = newUsername
		debounceUpdate("username") {
			preferencesRepository.setUsername(newUsername)
		}
	}

	fun updateDateOfBirth(newDateOfBirth: String) {
		birthdate = newDateOfBirth
		debounceUpdate("birthdate") {
			preferencesRepository.setDateOfBirth(newDateOfBirth)
		}
	}

	fun updateLogin(newLogin: String) {
		login = newLogin
		debounceUpdate("login") {
			preferencesRepository.setLogin(newLogin)
		}
	}

	fun updateAddress(newAddress: String) {
		address = newAddress
		debounceUpdate("address") {
			preferencesRepository.setAddress(newAddress)
		}
	}

	fun updateCountry(newCountry: String) {
		country = newCountry
		debounceUpdate("country") {
			preferencesRepository.setCountry(newCountry)
		}
	}

	fun updatePhone(newPhone: String) {
		phone = newPhone
		debounceUpdate("phone") {
			preferencesRepository.setPhone(newPhone)
		}
	}

	private fun debounceUpdate(key: String, action: suspend () -> Unit) {
		updateJobs[key]?.cancel()
		updateJobs[key] = viewModelScope.launch {
			delay(500)
			action()
		}
	}

	fun updateReceiveEmails(enabled: Boolean) {
		viewModelScope.launch {
			preferencesRepository.setReceiveEmails(enabled)
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
