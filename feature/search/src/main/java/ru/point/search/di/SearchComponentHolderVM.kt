package ru.point.search.di

import androidx.lifecycle.ViewModel
import ru.point.common.di.FeatureDepsProvider

internal class SearchComponentHolderVM() : ViewModel() {
    init {
        _searchComponent =
            DaggerSearchComponent.builder().deps(FeatureDepsProvider.featureDeps).build()
    }

    override fun onCleared() {
        super.onCleared()
        _searchComponent = null
    }
}

private var _searchComponent: SearchComponent? = null
    set(value) {
        if (field == null) field = value
    }

internal val searchComponent get() = requireNotNull(_searchComponent)