package ru.point.profile.di

import androidx.lifecycle.ViewModel
import ru.point.common.di.FeatureDepsProvider

internal class ProfileComponentHolderVM() : ViewModel() {
    init {
        _profileComponent = DaggerProfileComponent.builder().deps(FeatureDepsProvider.featureDeps).build()
    }

    override fun onCleared() {
        super.onCleared()
        _profileComponent = null
    }
}

private var _profileComponent: ProfileComponent? = null
    set(value) {
        if (field == null) field = value
    }

internal val profileComponent get() = requireNotNull(_profileComponent)