package ru.point.drivesell

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import ru.point.core.navigation.Navigator

class NavigatorImpl(
    private val navController: NavController
) : Navigator {
    override fun popBackStack() {
        navController.popBackStack()
    }

    override fun fromLoginFragmentToRegisterFragment() {
        navController.navigate(R.id.action_loginFragment_to_registerFragment)
    }

    override fun fromRegisterFragmentToLoginFragment() {
        navController.navigate(R.id.action_registerFragment_to_loginFragment)
    }

    override fun fromLoginFragmentToHomeFragment() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.loginFragment, inclusive = true)
            .build()
        navController.navigate(R.id.action_loginFragment_to_homeFragment, null, navOptions)
    }

    override fun fromRegisterFragmentToHomeFragment() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.registerFragment, inclusive = true)
            .build()
        navController.navigate(R.id.action_registerFragment_to_homeFragment, null, navOptions)
    }

    override fun fromMenuFragmentToProfileFragment() {
        navController.navigate(R.id.action_menuFragment_to_profileFragment)
    }

    override fun fromMenuFragmentToSettingsFragment() {
        navController.navigate(R.id.action_menuFragment_to_settingsFragment)
    }

    override fun fromMenuFragmentToLogInFragment() {
        navController.navigate(R.id.action_menuFragment_to_loginFragment)
    }
}