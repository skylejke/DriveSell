package ru.point.favourites.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.cars.repository.CarsRepository

@Suppress("UNCHECKED_CAST")
internal class FavouritesViewModelFactory(private val carsRepository: CarsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        FavouritesViewModel(carsRepository = carsRepository) as T
}