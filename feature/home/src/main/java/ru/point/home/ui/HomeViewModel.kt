package ru.point.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.home.domain.GetCarsUseCase

internal class HomeViewModel(private val getCarsUseCase: GetCarsUseCase) : ViewModel() {

    private val _cars = MutableStateFlow<List<AdVo>>(emptyList())
    val cars get() = _cars.asStateFlow()

    init {
        getCars()
    }

    fun getCars() {
        viewModelScope.launch {
            getCarsUseCase().fold(
                onSuccess = {
                    _cars.value = it

                }, onFailure = {

                }
            )
        }
    }
}