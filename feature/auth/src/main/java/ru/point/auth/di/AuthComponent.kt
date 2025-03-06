package ru.point.auth.di

import dagger.Component
import ru.point.auth.ui.login.LoginFragment
import ru.point.auth.ui.register.RegisterFragment
import ru.point.core.di.FeatureScope

@[FeatureScope Component(
    modules = [
        AuthRepositoryModule::class,
        AuthUseCaseModule::class,
        AuthViewModelFactoryModule::class],
    dependencies = [FeatureDeps::class]
)]
internal interface AuthComponent {
    fun inject(loginFragment: LoginFragment)
    fun inject(registerFragment: RegisterFragment)

    @Component.Builder
    interface Builder {
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): AuthComponent
    }
}