package com.example.shire.domain.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.shire.domain.model.CurrencyOption
import com.example.shire.domain.model.DateFormatOption
import com.example.shire.domain.model.LanguageOption
import com.example.shire.domain.model.Preferences
import com.example.shire.domain.model.TextSizeOption
import com.example.shire.domain.model.ThemeOption
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Singleton
class ProfilePreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ProfilePreferencesRepository {

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("profile_preferences", Context.MODE_PRIVATE)

    private object Keys {
        const val language = "profile_language"
        const val currency = "profile_currency"
        const val dateFormat = "profile_date_format"
        const val theme = "profile_theme"
        const val textSize = "profile_text_size"
        const val tripReminders = "profile_trip_reminders"
        const val weeklySummary = "profile_weekly_summary"
        const val termsAccepted = "profile_terms_accepted"
        const val username = "profile_username"
        const val dateOfBirth = "profile_date_of_birth"
    }

    private fun getCurrentPreferences(): Preferences {
        return Preferences(
            language = sharedPrefs.getString(Keys.language, null)?.toLanguageOptionOrDefault() ?: LanguageOption.SPANISH,
            currency = sharedPrefs.getString(Keys.currency, null)?.toCurrencyOptionOrDefault() ?: CurrencyOption.EUR,
            dateFormat = sharedPrefs.getString(Keys.dateFormat, null)?.toDateFormatOptionOrDefault() ?: DateFormatOption.DD_MM_YYYY,
            theme = sharedPrefs.getString(Keys.theme, null)?.toThemeOptionOrDefault() ?: ThemeOption.LIGHT,
            textSize = sharedPrefs.getString(Keys.textSize, null)?.toTextSizeOptionOrDefault() ?: TextSizeOption.NORMAL,
            tripRemindersEnabled = sharedPrefs.getBoolean(Keys.tripReminders, true),
            weeklySummaryEnabled = sharedPrefs.getBoolean(Keys.weeklySummary, false),
            termsAccepted = if (sharedPrefs.contains(Keys.termsAccepted)) sharedPrefs.getBoolean(Keys.termsAccepted, false) else null,
            username = sharedPrefs.getString(Keys.username, null) ?: "",
            dateOfBirth = sharedPrefs.getString(Keys.dateOfBirth, null) ?: ""
        )
    }

    override val profilePreferencesFlow: Flow<Preferences> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            trySend(getCurrentPreferences())
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(getCurrentPreferences()) // Emit initial value
        awaitClose {
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.distinctUntilChanged()

    override suspend fun setLanguage(language: LanguageOption) {
        sharedPrefs.edit().putString(Keys.language, language.id).apply()
    }

    override suspend fun setCurrency(currency: CurrencyOption) {
        sharedPrefs.edit().putString(Keys.currency, currency.id).apply()
    }

    override suspend fun setDateFormat(dateFormat: DateFormatOption) {
        sharedPrefs.edit().putString(Keys.dateFormat, dateFormat.id).apply()
    }

    override suspend fun setTheme(theme: ThemeOption) {
        sharedPrefs.edit().putString(Keys.theme, theme.id).apply()
    }

    override suspend fun setTextSize(textSize: TextSizeOption) {
        sharedPrefs.edit().putString(Keys.textSize, textSize.id).apply()
    }

    override suspend fun setTripRemindersEnabled(enabled: Boolean) {
        sharedPrefs.edit().putBoolean(Keys.tripReminders, enabled).apply()
    }

    override suspend fun setWeeklySummaryEnabled(enabled: Boolean) {
        sharedPrefs.edit().putBoolean(Keys.weeklySummary, enabled).apply()
    }

    override suspend fun setTermsAccepted(accepted: Boolean) {
        sharedPrefs.edit().putBoolean(Keys.termsAccepted, accepted).apply()
    }

    override suspend fun setUsername(username: String) {
        sharedPrefs.edit().putString(Keys.username, username).apply()
    }

    override suspend fun setDateOfBirth(dateOfBirth: String) {
        sharedPrefs.edit().putString(Keys.dateOfBirth, dateOfBirth).apply()
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
