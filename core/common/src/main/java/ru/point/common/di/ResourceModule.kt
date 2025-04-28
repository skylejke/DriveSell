package ru.point.common.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.point.common.utils.ResourceProvider
import ru.point.common.utils.ResourceProviderImpl

@Module
class ResourceModule {
    @[Provides FeatureScope]
    fun provideResourceProvider(context: Context): ResourceProvider = ResourceProviderImpl(context)
}
