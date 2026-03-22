package com.example.shire.domain.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.shire.domain.model.CurrencyOption
import com.example.shire.domain.model.DateFormatOption
import com.example.shire.domain.model.LanguageOption
import com.example.shire.domain.model.Preferences
import com.example.shire.domain.model.TextSizeOption
import com.example.shire.domain.model.ThemeOption
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private val Context.profileDataStore by preferencesDataStore(name = "profile_preferences")

@Singleton
class ProfilePreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ProfilePreferencesRepository {

    private object Keys {
        val language = stringPreferencesKey("profile_language")
        val currency = stringPreferencesKey("profile_currency")
        val dateFormat = stringPreferencesKey("profile_date_format")
        val theme = stringPreferencesKey("profile_theme")
        val textSize = stringPreferencesKey("profile_text_size")
        val tripReminders = booleanPreferencesKey("profile_trip_reminders")
        val weeklySummary = booleanPreferencesKey("profile_weekly_summary")
        val termsAccepted = booleanPreferencesKey("profile_terms_accepted")
    }

    override val profilePreferencesFlow: Flow<Preferences> = context.profileDataStore.data
        .map { preferences ->
            Preferences(
                language = preferences[Keys.language]
                    ?.toLanguageOptionOrDefault()
                    ?: LanguageOption.SPANISH,
                currency = preferences[Keys.currency]
                    ?.toCurrencyOptionOrDefault()
                    ?: CurrencyOption.EUR,
                dateFormat = preferences[Keys.dateFormat]
                    ?.toDateFormatOptionOrDefault()
                    ?: DateFormatOption.DD_MM_YYYY,
                theme = preferences[Keys.theme]
                    ?.toThemeOptionOrDefault()
                    ?: ThemeOption.LIGHT,
                textSize = preferences[Keys.textSize]
                    ?.toTextSizeOptionOrDefault()
                    ?: TextSizeOption.NORMAL,
                tripRemindersEnabled = preferences[Keys.tripReminders] ?: true,
                weeklySummaryEnabled = preferences[Keys.weeklySummary] ?: false,
                termsAccepted = preferences[Keys.termsAccepted]
            )
        }
        .distinctUntilChanged()

    override suspend fun setLanguage(language: LanguageOption) {
        context.profileDataStore.edit { preferences ->
            preferences[Keys.language] = language.id
        }
    }

    override suspend fun setCurrency(currency: CurrencyOption) {
        context.profileDataStore.edit { preferences ->
            preferences[Keys.currency] = currency.id
        }
    }

    override suspend fun setDateFormat(dateFormat: DateFormatOption) {
        context.profileDataStore.edit { preferences ->
            preferences[Keys.dateFormat] = dateFormat.id
        }
    }

    override suspend fun setTheme(theme: ThemeOption) {
        context.profileDataStore.edit { preferences ->
            preferences[Keys.theme] = theme.id
        }
    }

    override suspend fun setTextSize(textSize: TextSizeOption) {
        context.profileDataStore.edit { preferences ->
            preferences[Keys.textSize] = textSize.id
        }
    }

    override suspend fun setTripRemindersEnabled(enabled: Boolean) {
        context.profileDataStore.edit { preferences ->
            preferences[Keys.tripReminders] = enabled
        }
    }

    override suspend fun setWeeklySummaryEnabled(enabled: Boolean) {
        context.profileDataStore.edit { preferences ->
            preferences[Keys.weeklySummary] = enabled
        }
    }

    override suspend fun setTermsAccepted(accepted: Boolean) {
        context.profileDataStore.edit { preferences ->
            preferences[Keys.termsAccepted] = accepted
        }
    }
}

private fun String.toLanguageOptionOrDefault(): LanguageOption {
    return LanguageOption.entries.firstOrNull { it.id == this } ?: LanguageOption.SPANISH
}

private fun String.toCurrencyOptionOrDefault(): CurrencyOption {
    return CurrencyOption.entries.firstOrNull { it.id == this } ?: CurrencyOption.EUR
}

private fun String.toDateFormatOptionOrDefault(): DateFormatOption {
    return DateFormatOption.entries.firstOrNull { it.id == this } ?: DateFormatOption.DD_MM_YYYY
}

private fun String.toThemeOptionOrDefault(): ThemeOption {
    return ThemeOption.entries.firstOrNull { it.id == this } ?: ThemeOption.LIGHT
}

private fun String.toTextSizeOptionOrDefault(): TextSizeOption {
    return TextSizeOption.entries.firstOrNull { it.id == this } ?: TextSizeOption.NORMAL
}
