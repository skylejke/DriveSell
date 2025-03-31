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
        TokenStorageImpl(context.getSharedPreferences(SHARED_PREFS_USER, Context.MODE_PRIVATE))

    companion object {
        private const val SHARED_PREFS_USER = "user_prefs"
    }
}