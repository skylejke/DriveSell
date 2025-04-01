package ru.point.favourites.di

import androidx.lifecycle.ViewModel
import ru.point.common.di.FeatureDepsProvider

internal class FavouritesComponentHolderVM() : ViewModel() {
    init {
        _favouritesComponent =
            DaggerFavouritesComponent.builder().deps(FeatureDepsProvider.featureDeps).build()
    }

    override fun onCleared() {
        super.onCleared()
        _favouritesComponent = null
    }
}

private var _favouritesComponent: FavouritesComponent? = null
    set(value) {
        if (field == null) field = value
    }

internal val favouritesComponent get() = requireNotNull(_favouritesComponent)