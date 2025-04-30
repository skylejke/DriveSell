package ru.point.search.ui.searchResults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository
import ru.point.common.model.CarFilterParams
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
        filterParams: CarFilterParams,
        sortParam: String,
        orderParam: String
    ) {
        _status.value = Status.Loading
        viewModelScope.launch {
            carsRepository.searchCarsByFilters(
                filterParams = filterParams,
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