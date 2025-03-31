package ru.point.users_ads.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.cars.repository.CarsRepository

@Suppress("UNCHECKED_CAST")
internal class UsersAdsViewModelFactory(private val carsRepository: CarsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) = UsersAdsViewModel(carsRepository = carsRepository) as T
}