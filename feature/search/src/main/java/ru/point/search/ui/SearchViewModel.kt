package ru.point.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.repository.CarsRepository
import ru.point.search.domain.model.BrandVo
import ru.point.search.domain.model.ModelVo
import ru.point.search.domain.model.asBrandVo
import ru.point.search.domain.model.asModelVo

internal class SearchViewModel(
    private val carsRepository: CarsRepository
) : ViewModel() {

    private val _brands = MutableStateFlow<List<BrandVo>>(emptyList())
    val brands get() = _brands.asStateFlow()

    private val _models = MutableStateFlow<List<ModelVo>>(emptyList())
    val models get() = _models.asStateFlow()

    init {
        getBrands()
    }

    fun getBrands() {
        viewModelScope.launch {
            carsRepository.getBrands().onSuccess { brandList ->
                _brands.value = brandList.map { it.asBrandVo }
            }
        }
    }

    fun getModels(brandName: String) {
        viewModelScope.launch {
            carsRepository.getModelsByBrand(brandName).onSuccess { modelList ->
                _models.value = modelList.map { it.asModelVo }
            }
        }
    }
}