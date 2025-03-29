package ru.point.home.di

import androidx.lifecycle.ViewModel
import ru.point.common.di.FeatureDepsProvider

internal class HomeComponentHolderVM() : ViewModel() {
    init {
        _homeComponent =
            DaggerHomeComponent.builder().deps(FeatureDepsProvider.featureDeps).build()
    }

    override fun onCleared() {
        super.onCleared()
        _homeComponent = null
    }
}

private var _homeComponent: HomeComponent? = null
    set(value) {
        if (field == null) field = value
    }

internal val homeComponent get() = requireNotNull(_homeComponent)