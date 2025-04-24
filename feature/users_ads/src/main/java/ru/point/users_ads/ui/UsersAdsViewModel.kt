package ru.point.users_ads.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository
import ru.point.common.model.Status

internal class UsersAdsViewModel(private val carsRepository: CarsRepository) : ViewModel() {

    private val _usersAds = MutableStateFlow<List<AdVo>>(emptyList())
    val usersAds get() = _usersAds.asStateFlow()

    private val _status = MutableStateFlow<Status>(Status.Loading)
    val status get() = _status.asStateFlow()

    init {
        getUsersAds()
    }

    fun getUsersAds() {
        _status.value = Status.Loading
        viewModelScope.launch {
            carsRepository.getUsersAds()
                .onSuccess { adDtoList ->
                    _status.value = Status.Success
                    _usersAds.value = adDtoList.map { it.asAdVo }
                }
                .onFailure { _status.value = Status.Error }
        }
    }
}