package com.example.shire.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shire.domain.model.LoggedInUser
import com.example.shire.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loggedInUser = MutableStateFlow<LoggedInUser?>(null)
    val loggedInUser: StateFlow<LoggedInUser?> = _loggedInUser.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.loggedInUserFlow.collect { user ->
                _loggedInUser.value = user
            }
        }
    }

    val greetingName: String
        get() = loggedInUser.value?.name?.ifBlank { "viajero" } ?: "viajero"
}

