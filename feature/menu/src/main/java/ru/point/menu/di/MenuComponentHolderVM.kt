package ru.point.menu.di

import androidx.lifecycle.ViewModel
import ru.point.core.di.FeatureDepsProvider

internal class MenuComponentHolderVM() : ViewModel() {
    init {
        _menuComponent = DaggerMenuComponent.builder().deps(FeatureDepsProvider.featureDeps).build()
    }

    override fun onCleared() {
        super.onCleared()
        _menuComponent = null
    }
}

private var _menuComponent: MenuComponent? = null
    set(value) {
        if (field == null) field = value

    }

internal val menuComponent get() = requireNotNull(_menuComponent)