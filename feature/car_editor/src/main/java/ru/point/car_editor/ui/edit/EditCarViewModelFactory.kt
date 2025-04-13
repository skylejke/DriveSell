package ru.point.car_editor.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.cars.repository.CarsRepository

@Suppress("UNCHECKED_CAST")
internal class EditCarViewModelFactory(private val carsRepository: CarsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) = EditCarViewModel(carsRepository = carsRepository) as T
}