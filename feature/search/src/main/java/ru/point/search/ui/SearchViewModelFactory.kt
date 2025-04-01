package ru.point.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.cars.repository.CarsRepository

@Suppress("UNCHECKED_CAST")
internal class SearchViewModelFactory(
    private val carsRepository: CarsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        SearchViewModel(carsRepository = carsRepository) as T
}