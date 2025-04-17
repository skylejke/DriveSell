package ru.point.comparisons.di

import androidx.lifecycle.ViewModel
import ru.point.common.di.FeatureDepsProvider

internal class ComparisonsComponentHolderVM() : ViewModel() {
    init {
        _comparisonsComponent =
            DaggerComparisonsComponent.builder().deps(FeatureDepsProvider.featureDeps).build()
    }

    override fun onCleared() {
        super.onCleared()
        _comparisonsComponent = null
    }
}

private var _comparisonsComponent: ComparisonsComponent? = null
    set(value) {
        if (field == null) field = value
    }

internal val comparisonsComponent get() = requireNotNull(_comparisonsComponent)