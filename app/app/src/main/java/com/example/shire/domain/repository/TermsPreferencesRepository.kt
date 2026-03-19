package com.example.shire.domain.repository

import kotlinx.coroutines.flow.Flow

data class TermsPreference(
    val hasDecided: Boolean,
    val accepted: Boolean
)

interface TermsPreferencesRepository {
    val termsPreferenceFlow: Flow<TermsPreference>

    suspend fun setTermsDecision(accepted: Boolean)
}