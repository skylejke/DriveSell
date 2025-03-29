package ru.point.user.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.point.common.di.FeatureScope
import ru.point.user.storage.TokenStorage
import ru.point.user.storage.TokenStorageImpl

@Module
class TokenStorageModule {

    @[Provides FeatureScope]
    fun provideTokenStorage(context: Context): TokenStorage =
        TokenStorageImpl(context.getSharedPreferences(SHARED_PREFS_USER, Context.MODE_PRIVATE))

    companion object {
        private const val SHARED_PREFS_USER = "user_prefs"
    }
}