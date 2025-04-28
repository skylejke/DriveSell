package ru.point.search.ui.searchResults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository
import ru.point.common.model.Status

internal class SearchResultsViewModel(private val carsRepository: CarsRepository) : ViewModel() {

    private val _foundAds = MutableStateFlow<List<AdVo>>(emptyList())
    val foundAds get() = _foundAds.asStateFlow()

    private val _status = MutableStateFlow<Status>(Status.Loading)
    val status get() = _status.asStateFlow()

    fun searchCarsByQuery(
        query: String,
        sortParam: String,
        orderParam: String
    ) {
        _status.value = Status.Loading
        viewModelScope.launch {
            carsRepository.getCars(query = query, sortParam = sortParam, orderParam = orderParam)
                .onSuccess { foundAds ->
                    _status.value = Status.Success
                    _foundAds.value = foundAds.map { it.asAdVo }
                }
                .onFailure { _status.value = Status.Error }
        }
    }

    fun searchCarsByFilters(
        brand: String? = null,
        model: String? = null,
        yearMin: Short? = null,
        yearMax: Short? = null,
        priceMin: Int? = null,
        priceMax: Int? = null,
        mileageMin: Int? = null,
        mileageMax: Int? = null,
        enginePowerMin: Short? = null,
        enginePowerMax: Short? = null,
        engineCapacityMin: Double? = null,
        engineCapacityMax: Double? = null,
        fuelType: String? = null,
        bodyType: String? = null,
        color: String? = null,
        transmission: String? = null,
        drivetrain: String? = null,
        wheel: String? = null,
        condition: String? = null,
        owners: String? = null,
        sortParam: String,
        orderParam: String
    ) {
        _status.value = Status.Loading
        viewModelScope.launch {
            carsRepository.searchCarsByFilters(
                brand = brand,
                model = model,
                yearMin = yearMin,
                yearMax = yearMax,
                priceMin = priceMin,
                priceMax = priceMax,
                mileageMin = mileageMin,
                mileageMax = mileageMax,
                enginePowerMin = enginePowerMin,
                enginePowerMax = enginePowerMax,
                engineCapacityMin = engineCapacityMin,
                engineCapacityMax = engineCapacityMax,
                fuelType = fuelType,
                bodyType = bodyType,
                color = color,
                transmission = transmission,
                drivetrain = drivetrain,
                wheel = wheel,
                condition = condition,
                owners = owners,
                sortParam = sortParam,
                orderParam = orderParam
            )
                .onSuccess { foundAds ->
                    _status.value = Status.Success
                    _foundAds.value = foundAds.map { it.asAdVo }
                }
                .onFailure { _status.value = Status.Error }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}