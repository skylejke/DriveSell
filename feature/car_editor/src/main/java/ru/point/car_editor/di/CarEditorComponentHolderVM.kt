package ru.point.car_editor.di

import androidx.lifecycle.ViewModel
import ru.point.common.di.FeatureDepsProvider

internal class CarEditorComponentHolderVM : ViewModel() {
    init {
        _carEditorComponent =
            DaggerCarEditorComponent.builder().deps(FeatureDepsProvider.featureDeps).build()
    }

    override fun onCleared() {
        super.onCleared()
        _carEditorComponent = null
    }
}

private var _carEditorComponent: CarEditorComponent? = null
    set(value) {
        if (field == null) field = value
    }

internal val carEditorComponent get() = requireNotNull(_carEditorComponent)
