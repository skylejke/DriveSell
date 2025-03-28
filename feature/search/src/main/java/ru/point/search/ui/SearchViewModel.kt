package ru.point.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.search.domain.GetBrandsUseCase
import ru.point.search.domain.GetModelsByBrandUseCase
import ru.point.search.domain.model.BrandVo
import ru.point.search.domain.model.ModelVo

internal class SearchViewModel(
    private val getBrandsUseCase: GetBrandsUseCase,
    private val getModelsByBrandUseCase: GetModelsByBrandUseCase
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
            getBrandsUseCase().onSuccess {
                _brands.value = it
            }
        }
    }

    fun getModels(brandName: String) {
        viewModelScope.launch {
            getModelsByBrandUseCase(brandName).onSuccess {
                _models.value = it
            }
        }
    }
}