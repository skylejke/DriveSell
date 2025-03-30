package ru.point.car_details.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository

class CarDetailsViewModel(private val carsRepository: CarsRepository) : ViewModel() {

    private val _carDetails = MutableStateFlow<AdVo?>(null)
    val carDetails get() = _carDetails.asStateFlow()

    fun getCarAdById(adId: String) {
        viewModelScope.launch {
            carsRepository.getCarAdById(adId).onSuccess {
                _carDetails.value = it.asAdVo
            }
        }
    }
}