package ru.point.car_details.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository
import ru.point.common.model.Status
import ru.point.user.repository.UserRepository

class CarDetailsViewModel(
    private val carsRepository: CarsRepository,
    private val userRepository: UserRepository,
    private val adId: String,
    private val userId: String
) : ViewModel() {

    private val _carDetails = MutableStateFlow<AdVo?>(null)
    val carDetails get() = _carDetails.asStateFlow()

    private val _isUsersAd = MutableStateFlow<Boolean?>(null)
    val isUsersAd get() = _isUsersAd.asStateFlow()

    private val _isFavourite = MutableStateFlow<Boolean?>(null)
    val isFavourite get() = _isFavourite.asStateFlow()

    private val _isInComparisons = MutableStateFlow<Boolean?>(null)
    val isInComparisons get() = _isInComparisons.asStateFlow()

    private val _isGuest = MutableStateFlow<Boolean?>(null)
    val isGuest get() = _isGuest.asStateFlow()

    private val _status = MutableStateFlow<Status>(Status.Loading)
    val status get() = _status.asStateFlow()

    init {
        getCarAdById(adId = adId)
        checkIsUsersAd(userId = userId)
        checkIsUsersAd(userId = userId)
        checkIsFavourite(adId = adId)
    }

    fun getCarAdById(adId: String) {
        _status.value = Status.Loading
        viewModelScope.launch {
            carsRepository.getCarAdById(adId)
                .onSuccess {
                    _status.value = Status.Success
                    _carDetails.value = it.asAdVo
                }
                .onFailure { _status.value = Status.Error }
        }
    }

    fun checkIsUsersAd(userId: String) {
        viewModelScope.launch {
            _isUsersAd.value = userId == userRepository.getUserId()
            _isGuest.value = !userRepository.isAuthorized()
        }
    }

    fun checkIsFavourite(adId: String) {
        viewModelScope.launch {
            carsRepository.checkIsFavourite(adId).onSuccess {
                _isFavourite.value = it
            }
        }
    }

    fun addCarToFavourites(adId: String) {
        viewModelScope.launch {
            carsRepository.addCarToFavourites(adId).onSuccess {
                _isFavourite.value = true
            }
        }
    }

    fun removeCarFromFavourites(adId: String) {
        viewModelScope.launch {
            carsRepository.removeCarFromFavourites(adId).onSuccess {
                _isFavourite.value = false
            }
        }
    }

    fun checkIsInComparisons(adId: String) {
        viewModelScope.launch {
            carsRepository.checkIsInComparisons(adId).onSuccess {
                _isInComparisons.value = it
            }
        }
    }

    fun addCarToComparisons(adId: String) {
        viewModelScope.launch {
            carsRepository.addCarToComparisons(adId).onSuccess {
                _isInComparisons.value = true
            }
        }
    }

    fun removeCarFromComparisons(adId: String) {
        viewModelScope.launch {
            carsRepository.removeCarFromComparisons(adId).onSuccess {
                _isInComparisons.value = false
            }
        }
    }

    fun deleteAd(adId: String) {
        viewModelScope.launch {
            carsRepository.deleteAd(adId)
        }
    }
}