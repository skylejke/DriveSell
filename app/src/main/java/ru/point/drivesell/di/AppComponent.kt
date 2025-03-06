package ru.point.drivesell.di

import dagger.Component
import retrofit2.Retrofit
import ru.point.auth.di.FeatureDeps

@[AppScope Component(
    modules = [
        NetworkModule::class,
    ]
)]
interface AppComponent : FeatureDeps {
    override val retrofit: Retrofit
}