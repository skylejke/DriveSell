package ru.point.comparisons.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.cars.repository.CarsRepository

@Suppress("UNCHECKED_CAST")
internal class ComparisonsViewModelFactory(private val carsRepository: CarsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ComparisonsViewModel(carsRepository = carsRepository) as T
}