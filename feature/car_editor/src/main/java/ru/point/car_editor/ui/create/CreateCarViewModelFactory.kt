package ru.point.car_editor.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.cars.repository.CarsRepository
import ru.point.common.utils.ResourceProvider
import ru.point.user.repository.UserRepository

@Suppress("UNCHECKED_CAST")
internal class CreateCarViewModelFactory(
    private val carsRepository: CarsRepository,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        CreateCarViewModel(
            carsRepository = carsRepository,
            userRepository = userRepository,
            resourceProvider = resourceProvider
        ) as T
}