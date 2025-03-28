package ru.point.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.search.domain.GetBrandsUseCase
import ru.point.search.domain.GetModelsByBrandUseCase

@Suppress("UNCHECKED_CAST")
internal class SearchViewModelFactory(
    private val getBrandsUseCase: GetBrandsUseCase,
    private val getModelsByBrandUseCase: GetModelsByBrandUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        SearchViewModel(
            getBrandsUseCase = getBrandsUseCase,
            getModelsByBrandUseCase = getModelsByBrandUseCase
        ) as T
}