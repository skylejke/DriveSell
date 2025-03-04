package ru.point.core.navigation

interface Navigator {
    fun popBackStack()
    fun fromLoginFragmentToRegisterFragment()
    fun fromRegisterFragmentToLoginFragment()
}