package ru.point.car_editor.di

import dagger.Component
import ru.point.car_editor.ui.create.CreateCarFragment
import ru.point.car_editor.ui.edit.EditCarFragment
import ru.point.cars.di.CarsRepositoryModule
import ru.point.common.di.FeatureDeps
import ru.point.common.di.FeatureScope
import ru.point.common.di.TokenStorageModule
import ru.point.user.di.UserRepositoryModule

@[FeatureScope Component(
    modules = [
        TokenStorageModule::class,
        UserRepositoryModule::class,
        CarsRepositoryModule::class,
        CarEditorViewModelFactoryModule::class
    ],
    dependencies = [FeatureDeps::class]
)]
internal interface CarEditorComponent {
    fun inject(createCarFragment: CreateCarFragment)
    fun inject(editCarFragment: EditCarFragment)

    @Component.Builder
    interface Builder {
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): CarEditorComponent
    }
}