package ru.point.common.navigation

interface Navigator {
    fun popBackStack()
    fun fromLoginFragmentToRegisterFragment()
    fun fromRegisterFragmentToLoginFragment()
    fun fromLoginFragmentToHomeFragment()
    fun fromRegisterFragmentToHomeFragment()
    fun fromMenuFragmentToProfileFragment()
    fun fromMenuFragmentToSettingsFragment()
    fun fromMenuFragmentToLogInFragment()
    fun fromProfileFragmentToEditUserDataFragment()
    fun fromProfileFragmentToEditPasswordFragment()
    fun fromProfileFragmentToLoginFragment()
}