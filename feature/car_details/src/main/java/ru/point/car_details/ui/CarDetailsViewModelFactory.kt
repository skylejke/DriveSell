package ru.point.car_details.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.cars.repository.CarsRepository
import ru.point.user.repository.UserRepository
import javax.inject.Inject
import javax.inject.Named

@Suppress("UNCHECKED_CAST")
class CarDetailsViewModelFactory @Inject constructor(
    private val carsRepository: CarsRepository,
    private val userRepository: UserRepository,
    @Named("adId") private val adId: String,
    @Named("userId")private val userId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        CarDetailsViewModel(
            carsRepository = carsRepository,
            userRepository = userRepository,
            adId = adId,
            userId = userId
        ) as T
}