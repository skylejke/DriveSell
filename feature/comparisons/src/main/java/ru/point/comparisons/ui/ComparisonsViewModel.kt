package ru.point.comparisons.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository
import ru.point.common.model.Status

internal class ComparisonsViewModel(private val carsRepository: CarsRepository) : ViewModel() {

    private val _comparedCars = MutableStateFlow<List<AdVo>>(emptyList())
    val comparedCars get() = _comparedCars.asStateFlow()

    private val _status = MutableStateFlow<Status>(Status.Loading)
    val status get() = _status.asStateFlow()

    fun getComparedCars() {
        _status.value = Status.Loading
        viewModelScope.launch {
            carsRepository.getUsersComparisons()
                .onSuccess { comparedCars ->
                    _status.value = Status.Success
                    _comparedCars.value = comparedCars.map { it.asAdVo }
                }
                .onFailure { _status.value = Status.Error }
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