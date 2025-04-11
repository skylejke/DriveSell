package ru.point.cars.di

import android.content.Context
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import ru.point.cars.repository.CarsRepository
import ru.point.cars.repository.CarsRepositoryImpl
import ru.point.cars.room.DataBase
import ru.point.cars.service.CarsService
import ru.point.common.di.FeatureScope
import ru.point.common.storage.TokenStorage

@Module
class CarsRepositoryModule {

    @[Provides FeatureScope]
    fun provideCarsRepository(
        carsService: CarsService,
        tokenStorage: TokenStorage,
        dataBase: DataBase,
        context: Context
    ): CarsRepository =
        CarsRepositoryImpl(
            carsService = carsService,
            tokenStorage = tokenStorage,
            dataBase = dataBase,
            context = context
        )

    @[Provides FeatureScope]
    fun provideCarsService(retrofit: Retrofit) = retrofit.create<CarsService>()

    @[Provides FeatureScope]
    fun provideDataBase(context: Context) = DataBase.getDataBase(context = context)
}