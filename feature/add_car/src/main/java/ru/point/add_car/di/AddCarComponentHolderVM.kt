package ru.point.add_car.di

import androidx.lifecycle.ViewModel
import ru.point.common.di.FeatureDepsProvider

internal class AddCarComponentHolderVM : ViewModel() {
    init {
        _addCarComponent =
            DaggerAddCarComponent.builder().deps(FeatureDepsProvider.featureDeps).build()
    }

    override fun onCleared() {
        super.onCleared()
        _addCarComponent = null
    }
}

private var _addCarComponent: AddCarComponent? = null
    set(value) {
        if (field == null) field = value
    }

internal val addCarComponent get() = requireNotNull(_addCarComponent)
