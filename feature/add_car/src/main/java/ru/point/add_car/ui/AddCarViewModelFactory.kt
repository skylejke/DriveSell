package ru.point.add_car.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.cars.repository.CarsRepository
import ru.point.user.repository.UserRepository

@Suppress("UNCHECKED_CAST")
internal class AddCarViewModelFactory(
    private val carsRepository: CarsRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        AddCarViewModel(
            carsRepository = carsRepository,
            userRepository = userRepository
        ) as T
}