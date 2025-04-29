package ru.point.car_editor.di

import dagger.BindsInstance
import dagger.Component
import ru.point.car_editor.ui.create.CreateCarFragment
import ru.point.car_editor.ui.edit.EditCarFragment
import ru.point.cars.di.CarsRepositoryModule
import ru.point.common.di.FeatureDeps
import ru.point.common.di.FeatureScope
import ru.point.common.di.ResourceModule
import ru.point.common.di.TokenStorageModule
import ru.point.user.di.UserRepositoryModule
import javax.inject.Named

@[FeatureScope Component(
    modules = [
        ResourceModule::class,
        TokenStorageModule::class,
        UserRepositoryModule::class,
        CarsRepositoryModule::class
    ],
    dependencies = [FeatureDeps::class]
)]
internal interface CarEditorComponent {
    fun inject(createCarFragment: CreateCarFragment)
    fun inject(editCarFragment: EditCarFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun adId(@Named("adId") adId: String = ""): Builder
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): CarEditorComponent
    }
}