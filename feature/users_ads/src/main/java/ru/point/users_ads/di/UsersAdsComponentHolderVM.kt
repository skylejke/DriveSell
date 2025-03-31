package ru.point.users_ads.di

import androidx.lifecycle.ViewModel
import ru.point.common.di.FeatureDepsProvider

internal class UsersAdsComponentHolderVM : ViewModel() {
    init {
        _usersAdsComponent =
            DaggerUsersAdsComponent.builder().deps(FeatureDepsProvider.featureDeps).build()
    }

    override fun onCleared() {
        super.onCleared()
        _usersAdsComponent = null
    }
}

private var _usersAdsComponent: UsersAdsComponent? = null
    set(value) {
        if (field == null) field = value

    }

internal val usersAdsComponent get() = requireNotNull(_usersAdsComponent)