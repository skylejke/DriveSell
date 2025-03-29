package ru.point.search.di

import dagger.Module
import dagger.Provides
import ru.point.common.di.FeatureScope
import ru.point.search.domain.GetBrandsUseCase
import ru.point.search.domain.GetModelsByBrandUseCase
import ru.point.search.ui.SearchViewModelFactory

@Module
internal class SearchViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideSearchViewModelFactory(
        getBrandsUseCase: GetBrandsUseCase,
        getModelsByBrandUseCase: GetModelsByBrandUseCase
    ) =
        SearchViewModelFactory(
            getBrandsUseCase = getBrandsUseCase,
            getModelsByBrandUseCase = getModelsByBrandUseCase
        )
}