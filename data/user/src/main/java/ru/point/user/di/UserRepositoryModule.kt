package ru.point.user.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import ru.point.common.di.FeatureScope
import ru.point.user.repository.UserRepository
import ru.point.user.repository.UserRepositoryImpl
import ru.point.user.service.UserService
import ru.point.user.storage.TokenStorage

@Module
class UserRepositoryModule {

    @[Provides FeatureScope]
    fun provideUserRepository(userService: UserService, tokenStorage: TokenStorage): UserRepository =
        UserRepositoryImpl(userService = userService, tokenStorage = tokenStorage)

    @[Provides FeatureScope]
    fun provideUserService(retrofit: Retrofit) = retrofit.create<UserService>()
}