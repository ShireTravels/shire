package com.example.shire.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shire.domain.repository.TermsPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class TermsDecision {
	ACCEPTED,
	REJECTED
}

data class TermsUiState(
	val decision: TermsDecision? = null,
	val isLoading: Boolean = true,
	val isSaving: Boolean = false,
	val errorMessage: String? = null
)

sealed interface TermsUiEvent {
	data object NavigateBack : TermsUiEvent
}

@HiltViewModel
class TermsViewModel @Inject constructor(
	private val termsPreferencesRepository: TermsPreferencesRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(TermsUiState())
	val uiState: StateFlow<TermsUiState> = _uiState.asStateFlow()

	private val _events = MutableSharedFlow<TermsUiEvent>()
	val events: SharedFlow<TermsUiEvent> = _events.asSharedFlow()

	init {
		viewModelScope.launch {
			termsPreferencesRepository.termsPreferenceFlow.collect { preference ->
				_uiState.update {
					it.copy(
						decision = when {
							!preference.hasDecided -> null
							preference.accepted -> TermsDecision.ACCEPTED
							else -> TermsDecision.REJECTED
						},
						isLoading = false,
						isSaving = false
					)
				}
			}
		}
	}

	fun acceptTerms() {
		persistDecision(accepted = true)
	}

	fun rejectTerms() {
		persistDecision(accepted = false)
	}

	fun clearError() {
		_uiState.update { it.copy(errorMessage = null) }
	}

	private fun persistDecision(accepted: Boolean) {
		if (_uiState.value.isSaving) return

		viewModelScope.launch {
			_uiState.update { it.copy(isSaving = true, errorMessage = null) }

			runCatching {
				termsPreferencesRepository.setTermsDecision(accepted)
			}.onSuccess {
				_events.emit(TermsUiEvent.NavigateBack)
			}.onFailure { throwable ->
				_uiState.update {
					it.copy(
						isSaving = false,
						errorMessage = throwable.message ?: "Unable to save terms decision"
					)
				}
			}
		}
	}
}


