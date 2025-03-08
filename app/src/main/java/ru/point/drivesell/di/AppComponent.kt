package ru.point.drivesell.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import ru.point.core.di.FeatureDeps

@[AppScope Component(
    modules = [
        NetworkModule::class,
    ]
)]
interface AppComponent : FeatureDeps {
    override val retrofit: Retrofit
    override val context: Context

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun provideContext(context: Context): Builder
        fun build(): AppComponent
    }
}