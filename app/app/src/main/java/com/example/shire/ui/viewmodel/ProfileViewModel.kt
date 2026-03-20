package com.example.shire.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shire.domain.repository.CurrencyOption
import com.example.shire.domain.repository.DateFormatOption
import com.example.shire.domain.repository.LanguageOption
import com.example.shire.domain.repository.ProfilePreferencesRepository
import com.example.shire.domain.repository.ThemeOption
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
	val isLoading: Boolean = true,
	val isSaving: Boolean = false,
	val errorMessage: String? = null,
	val availableLanguages: List<LanguageOption> = LanguageOption.entries,
	val selectedLanguage: LanguageOption = LanguageOption.SPANISH,
	val availableCurrencies: List<CurrencyOption> = CurrencyOption.entries,
	val selectedCurrency: CurrencyOption = CurrencyOption.EUR,
	val availableDateFormats: List<DateFormatOption> = DateFormatOption.entries,
	val selectedDateFormat: DateFormatOption = DateFormatOption.DD_MM_YYYY,
	val selectedTheme: ThemeOption = ThemeOption.LIGHT,
	val tripRemindersEnabled: Boolean = true,
	val weeklySummaryEnabled: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
	private val profilePreferencesRepository: ProfilePreferencesRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(ProfileUiState())
	val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

	init {
		viewModelScope.launch {
			profilePreferencesRepository.profilePreferencesFlow.collect { preferences ->
				_uiState.update {
					it.copy(
						isLoading = false,
						isSaving = false,
						errorMessage = null,
						selectedLanguage = preferences.language,
						selectedCurrency = preferences.currency,
						selectedDateFormat = preferences.dateFormat,
						selectedTheme = preferences.theme,
						tripRemindersEnabled = preferences.tripRemindersEnabled,
						weeklySummaryEnabled = preferences.weeklySummaryEnabled
					)
				}
			}
		}
	}

	fun selectLanguage(language: LanguageOption) {
		persistChange { profilePreferencesRepository.setLanguage(language) }
	}

	fun selectCurrency(currency: CurrencyOption) {
		persistChange { profilePreferencesRepository.setCurrency(currency) }
	}

	fun selectDateFormat(dateFormat: DateFormatOption) {
		persistChange { profilePreferencesRepository.setDateFormat(dateFormat) }
	}

	fun selectTheme(theme: ThemeOption) {
		persistChange { profilePreferencesRepository.setTheme(theme) }
	}

	fun setDarkModeEnabled(enabled: Boolean) {
		selectTheme(if (enabled) ThemeOption.DARK else ThemeOption.LIGHT)
	}

	fun setTripRemindersEnabled(enabled: Boolean) {
		persistChange { profilePreferencesRepository.setTripRemindersEnabled(enabled) }
	}

	fun setWeeklySummaryEnabled(enabled: Boolean) {
		persistChange { profilePreferencesRepository.setWeeklySummaryEnabled(enabled) }
	}

	fun clearError() {
		_uiState.update { it.copy(errorMessage = null) }
	}

	private fun persistChange(action: suspend ProfilePreferencesRepository.() -> Unit) {
		if (_uiState.value.isSaving) return

		viewModelScope.launch {
			_uiState.update { it.copy(isSaving = true, errorMessage = null) }

			runCatching {
				profilePreferencesRepository.action()
			}.onFailure { throwable ->
				_uiState.update {
					it.copy(
						isSaving = false,
						errorMessage = throwable.message ?: "No se pudieron guardar las preferencias"
					)
				}
			}
		}
	}
}

