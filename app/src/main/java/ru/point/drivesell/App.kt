package ru.point.drivesell

import android.app.Application
import ru.point.core.di.FeatureDepsStore
import ru.point.drivesell.di.AppComponent
import ru.point.drivesell.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().provideContext(context = applicationContext).build()
        FeatureDepsStore.featureDeps = appComponent
    }
}