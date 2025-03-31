package ru.point.users_ads.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository

internal class UsersAdsViewModel(private val carsRepository: CarsRepository) : ViewModel() {

    private val _usersAds = MutableStateFlow<List<AdVo>>(emptyList())
    val usersAds get() = _usersAds.asStateFlow()

    init {
        getUsersAds()
    }

    fun getUsersAds() {
        viewModelScope.launch {
            carsRepository.getUsersAds().onSuccess { adDtoList ->
                _usersAds.value = adDtoList.map { it.asAdVo }
            }
        }
    }
}