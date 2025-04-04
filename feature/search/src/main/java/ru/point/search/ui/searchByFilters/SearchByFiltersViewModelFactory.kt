package ru.point.search.ui.searchByFilters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.cars.repository.CarsRepository

@Suppress("UNCHECKED_CAST")
internal class SearchByFiltersViewModelFactory(
    private val carsRepository: CarsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        SearchByFiltersViewModel(carsRepository = carsRepository) as T
}