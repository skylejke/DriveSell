package ru.point.drivesell.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {
    @[Provides AppScope]
    fun provideRetrofit() = Retrofit.Builder()
        .baseUrl("http://192.168.1.23:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}