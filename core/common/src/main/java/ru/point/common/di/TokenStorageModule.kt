package ru.point.common.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.point.common.storage.TokenStorage
import ru.point.common.storage.TokenStorageImpl

@Module
class TokenStorageModule {

    @[Provides FeatureScope]
    fun provideTokenStorage(context: Context): TokenStorage =
        TokenStorageImpl(context = context)
}