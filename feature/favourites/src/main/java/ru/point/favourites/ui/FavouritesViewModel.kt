package ru.point.favourites.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository
import ru.point.common.model.Status

internal class FavouritesViewModel(private val carsRepository: CarsRepository) : ViewModel() {

    private val _favouriteCars = MutableStateFlow<List<AdVo>>(emptyList())
    val favouriteCars get() = _favouriteCars.asStateFlow()

    private val _status = MutableStateFlow<Status>(Status.Loading)
    val status get() = _status.asStateFlow()

    init {
        getFavourites()
    }

    fun getFavourites() {
        _status.value = Status.Loading
        viewModelScope.launch {
            carsRepository.getUsersFavourites()
                .onSuccess { favourites ->
                    _status.value = Status.Success
                    _favouriteCars.value = favourites.map { it.asAdVo }
                }
                .onFailure { _status.value = Status.Error }
        }
    }
}