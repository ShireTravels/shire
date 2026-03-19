package com.example.shire.domain.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private val Context.termsDataStore by preferencesDataStore(name = "terms_preferences")

@Singleton
class TermsPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TermsPreferencesRepository {
    private object Keys {
        val hasDecided = booleanPreferencesKey("has_decided_terms")
        val acceptedTerms = booleanPreferencesKey("accepted_terms")
    }

    override val termsPreferenceFlow: Flow<TermsPreference> = context.termsDataStore.data
        .map { preferences ->
            val hasDecided = preferences[Keys.hasDecided] ?: false
            val accepted = preferences[Keys.acceptedTerms] ?: false
            TermsPreference(hasDecided = hasDecided, accepted = accepted)
        }
        .distinctUntilChanged()

    override suspend fun setTermsDecision(accepted: Boolean) {
        context.termsDataStore.edit { preferences ->
            preferences[Keys.hasDecided] = true
            preferences[Keys.acceptedTerms] = accepted
        }
    }
}
