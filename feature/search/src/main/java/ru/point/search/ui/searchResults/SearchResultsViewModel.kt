package ru.point.search.ui.searchResults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository

internal class SearchResultsViewModel(private val carsRepository: CarsRepository) : ViewModel() {

    private val _foundAds = MutableStateFlow<List<AdVo>>(emptyList())
    val foundAds get() = _foundAds.asStateFlow()

    fun searchCarsByQuery(query: String) {
        viewModelScope.launch {
            carsRepository.getCars(query = query).onSuccess { foundAds ->
                _foundAds.value = foundAds.map { it.asAdVo }
            }
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
        owners: String? = null
    ) {
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
            ).onSuccess { foundAds ->
                _foundAds.value = foundAds.map { it.asAdVo }
            }
        }
    }
}