package ru.point.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.home.domain.GetCarsUseCase

@Suppress("UNCHECKED_CAST")
internal class HomeViewModelFactory(private val getCarsUseCase: GetCarsUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        HomeViewModel(getCarsUseCase = getCarsUseCase) as T
}