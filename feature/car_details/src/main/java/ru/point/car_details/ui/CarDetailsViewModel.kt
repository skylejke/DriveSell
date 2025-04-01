package ru.point.car_details.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository
import ru.point.user.repository.UserRepository

class CarDetailsViewModel(
    private val carsRepository: CarsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _carDetails = MutableStateFlow<AdVo?>(null)
    val carDetails get() = _carDetails.asStateFlow()

    private val _isUsersAd = MutableStateFlow<Boolean?>(null)
    val isUsersAd get() = _isUsersAd.asStateFlow()

    private val _isFavourite = MutableStateFlow<Boolean?>(null)
    val isFavourite get() = _isFavourite.asStateFlow()

    fun getCarAdById(adId: String) {
        viewModelScope.launch {
            carsRepository.getCarAdById(adId).onSuccess {
                _carDetails.value = it.asAdVo
            }
        }
    }

    fun checkIsUsersAd(userId: String) {
        viewModelScope.launch {
            _isUsersAd.value = userId == userRepository.getUserId()
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

    fun deleteAd(adId: String) {
        viewModelScope.launch {
            carsRepository.deleteAd(adId)
        }
    }
}