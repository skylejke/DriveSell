package ru.point.car_editor.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.cars.repository.CarsRepository
import javax.inject.Inject
import javax.inject.Named

@Suppress("UNCHECKED_CAST")
internal class EditCarViewModelFactory @Inject constructor(
    private val carsRepository: CarsRepository,
    @Named("adId") private val adId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditCarViewModel(carsRepository = carsRepository, adId = adId) as T
}