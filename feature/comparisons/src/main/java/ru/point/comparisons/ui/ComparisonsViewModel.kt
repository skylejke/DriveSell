package ru.point.comparisons.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository

internal class ComparisonsViewModel(private val carsRepository: CarsRepository) : ViewModel() {

    private val _comparedCars = MutableStateFlow<List<AdVo>>(emptyList())
    val comparedCars get() = _comparedCars.asStateFlow()

    fun getComparedCars() {
        viewModelScope.launch {
            carsRepository.getUsersComparisons().onSuccess { comparedCars ->
                _comparedCars.value = comparedCars.map { it.asAdVo }
            }
        }
    }

    fun removeCarFromComparisons(adId: String) {
        viewModelScope.launch {
            carsRepository.removeCarFromComparisons(adId).onSuccess {
                getComparedCars()
            }
        }
    }
}