package ru.point.search.ui.searchByFilters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.BrandVo
import ru.point.cars.model.ModelVo
import ru.point.cars.model.asBrandVo
import ru.point.cars.model.asModelVo
import ru.point.cars.repository.CarsRepository
import ru.point.common.model.Status

internal class SearchByFiltersViewModel(
    private val carsRepository: CarsRepository
) : ViewModel() {

    private val _brands = MutableStateFlow<List<BrandVo>>(emptyList())
    val brands get() = _brands.asStateFlow()

    private val _models = MutableStateFlow<List<ModelVo>>(emptyList())
    val models get() = _models.asStateFlow()

    private val _status = MutableStateFlow<Status>(Status.Loading)
    val status get() = _status.asStateFlow()

    fun getBrands() {
        _status.value = Status.Loading
        viewModelScope.launch {
            carsRepository.getBrands()
                .onSuccess { brandList ->
                    _status.value = Status.Success
                    _brands.value = emptyList()
                    _brands.value = brandList.map { it.asBrandVo }
                }
                .onFailure {
                    _status.value = Status.Error
                }
        }
    }

    fun getModels(brandName: String) {
        _status.value = Status.Loading
        viewModelScope.launch {
            carsRepository.getModelsByBrand(brandName)
                .onSuccess { modelList ->
                    _status.value = Status.Success
                    _models.value = modelList.map { it.asModelVo }
                }
                .onFailure {
                    _status.value = Status.Error
                }
        }
    }
}