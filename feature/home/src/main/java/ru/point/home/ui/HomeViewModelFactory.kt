package ru.point.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.cars.repository.CarsRepository

@Suppress("UNCHECKED_CAST")
internal class HomeViewModelFactory(private val carsRepository: CarsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        HomeViewModel(carsRepository = carsRepository) as T
}