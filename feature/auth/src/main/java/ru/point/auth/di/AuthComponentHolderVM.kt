package ru.point.auth.di

import androidx.lifecycle.ViewModel
import ru.point.common.di.FeatureDepsProvider

internal class AuthComponentHolderVM() : ViewModel() {
    init {
        _authComponent = DaggerAuthComponent.builder().deps(FeatureDepsProvider.featureDeps).build()
    }

    override fun onCleared() {
        super.onCleared()
        _authComponent = null
    }
}

private var _authComponent: AuthComponent? = null
    set(value) {
        if (field == null) field = value
    }

internal val authComponent get() = requireNotNull(_authComponent)