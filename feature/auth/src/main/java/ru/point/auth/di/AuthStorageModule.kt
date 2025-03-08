package ru.point.auth.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.point.auth.storage.AuthStorage
import ru.point.auth.storage.AuthStorageImpl
import ru.point.core.di.FeatureScope

@Module
internal class AuthStorageModule {

    @[Provides FeatureScope]
    fun provideAuthStorage(context: Context): AuthStorage =
        AuthStorageImpl(context.getSharedPreferences(SHARED_PREFS_USER, Context.MODE_PRIVATE))

    companion object {
        private const val SHARED_PREFS_USER = "user_prefs"
    }
}