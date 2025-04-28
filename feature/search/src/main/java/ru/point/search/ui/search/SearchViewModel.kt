package ru.point.search.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.SearchHistory
import ru.point.cars.repository.CarsRepository

internal class SearchViewModel(private val carsRepository: CarsRepository) : ViewModel() {

    private val _searchHistoryList = MutableStateFlow<List<SearchHistory>>(emptyList())
    val searchHistory get() = _searchHistoryList.asStateFlow()

    init {
        getSearchHistory()
    }

    fun insertSearchHistoryItem(query: String) {
        viewModelScope.launch {
            carsRepository.insertSearchHistoryItem(query)
        }
    }

    fun getSearchHistory() {
        viewModelScope.launch {
            carsRepository.getSearchHistory().collect {
                _searchHistoryList.value = it
            }
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            carsRepository.clearSearchHistory()
        }
    }
}