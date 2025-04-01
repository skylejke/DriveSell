package ru.point.favourites.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository

internal class FavouritesViewModel(private val carsRepository: CarsRepository) : ViewModel() {

    private val _favourites = MutableStateFlow<List<AdVo>>(emptyList())
    val favourites get() = _favourites.asStateFlow()

    init {
        getFavourites()
    }

    fun getFavourites() {
        viewModelScope.launch {
            carsRepository.getUsersFavourites().onSuccess { favourites ->
                _favourites.value = favourites.map { it.asAdVo }
            }
        }
    }
}