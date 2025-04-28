package ru.point.car_details.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.cars.repository.CarsRepository
import ru.point.user.repository.UserRepository

@Suppress("UNCHECKED_CAST")
class CarDetailsViewModelFactory(
    private val carsRepository: CarsRepository,
    private val userRepository: UserRepository,
    private val adId: String,
    private val userId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        CarDetailsViewModel(
            carsRepository = carsRepository,
            userRepository = userRepository,
            adId = adId,
            userId = userId
        ) as T
}