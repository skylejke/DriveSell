package ru.point.car_details.di

import androidx.lifecycle.ViewModel
import ru.point.common.di.FeatureDepsProvider

internal class CarDetailsComponentHolderVM() : ViewModel() {
    init {
        _carDetailsComponent =
            DaggerCarDetailsComponent.builder().deps(FeatureDepsProvider.featureDeps).build()
    }

    override fun onCleared() {
        super.onCleared()
        _carDetailsComponent = null
    }
}

private var _carDetailsComponent: CarDetailsComponent? = null
    set(value) {
        if (field == null) field = value
    }

internal val carDetailsComponent get() = requireNotNull(_carDetailsComponent)