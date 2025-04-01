package ru.point.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository

internal class HomeViewModel(private val carsRepository: CarsRepository) : ViewModel() {

    private val _cars = MutableStateFlow<List<AdVo>>(emptyList())
    val cars get() = _cars.asStateFlow()

    init {
        getCars()
    }

    fun getCars() {
        viewModelScope.launch {
            carsRepository.getCars().onSuccess { carList ->
                _cars.value = carList.map { it.asAdVo }
            }
        }
    }
}