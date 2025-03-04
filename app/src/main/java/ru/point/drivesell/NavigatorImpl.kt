package ru.point.drivesell

import androidx.navigation.NavController
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
}