package ru.point.common.navigation

import androidx.navigation.NavController

interface NavigatorProvider {
    fun getNavigator(navController: NavController): Navigator
}