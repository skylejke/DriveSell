package ru.point.menu.di

import dagger.Component
import ru.point.common.di.FeatureDeps
import ru.point.common.di.FeatureScope
import ru.point.common.di.TokenStorageModule
import ru.point.menu.ui.MenuFragment
import ru.point.user.di.UserRepositoryModule

@[FeatureScope Component(
    modules = [
        TokenStorageModule::class,
        UserRepositoryModule::class
    ],
    dependencies = [FeatureDeps::class]
)]
internal interface MenuComponent {
    fun inject(menuFragment: MenuFragment)

    @Component.Builder
    interface Builder {
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): MenuComponent
    }
}