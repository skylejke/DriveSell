package ru.point.drivesell.app

import android.app.Application
import ru.point.common.di.FeatureDepsStore
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