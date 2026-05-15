package com.example.shire.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shire.domain.repository.HotelRemoteRepository
import com.example.shire.network.HotelDto
import com.example.shire.network.ReserveRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HotelDetailsUiState(
	val isLoading: Boolean = false,
	val hotel: HotelDto? = null,
	val errorMessage: String? = null
)

@HiltViewModel
class HotelDetailsViewModel @Inject constructor(
	private val hotelRemoteRepository: HotelRemoteRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(HotelDetailsUiState(isLoading = true))
	val uiState: StateFlow<HotelDetailsUiState> = _uiState.asStateFlow()

	fun loadHotel(groupId: String = DEFAULT_GROUP_ID, hotelId: String? = null, city: String? = null) {
		viewModelScope.launch {
			_uiState.value = HotelDetailsUiState(isLoading = true)
			runCatching {
				hotelRemoteRepository.checkAvailability(
					groupId = groupId,
					startDate = DEFAULT_START_DATE,
					endDate = DEFAULT_END_DATE,
					hotelId = hotelId,
					city = city
				).firstOrNull()
					?: hotelRemoteRepository.listHotels(groupId).firstOrNull()
			}.onSuccess { hotel ->
				_uiState.value = HotelDetailsUiState(hotel = hotel)
			}.onFailure { throwable ->
				_uiState.value = HotelDetailsUiState(errorMessage = throwable.message ?: "Unable to load hotel")
			}
		}
	}

	fun reserveHotel(groupId: String, request: ReserveRequest) {
		viewModelScope.launch {
			runCatching {
				hotelRemoteRepository.reserveRoom(groupId = groupId, request = request)
			}.onFailure { throwable ->
				_uiState.value = _uiState.value.copy(errorMessage = throwable.message ?: "Unable to reserve hotel")
			}
		}
	}

	companion object {
		private const val DEFAULT_GROUP_ID = "G01"
		private const val DEFAULT_START_DATE = "2026-05-14"
		private const val DEFAULT_END_DATE = "2026-05-15"
	}
}

