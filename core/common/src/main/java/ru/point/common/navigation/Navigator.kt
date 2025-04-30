package ru.point.common.navigation

import ru.point.common.model.CarFilterParams

interface Navigator {
    fun popBackStack()
    fun fromLoginFragmentToRegisterFragment()
    fun fromRegisterFragmentToLoginFragment()
    fun fromLoginFragmentToHomeFragment()
    fun fromRegisterFragmentToHomeFragment()
    fun fromMenuFragmentToProfileFragment()
    fun fromMenuFragmentToLogInFragment()
    fun fromProfileFragmentToEditUserDataFragment()
    fun fromProfileFragmentToEditPasswordFragment()
    fun fromProfileFragmentToLoginFragment()
    fun fromHomeFragmentToCarDetailsFragment(adId: String, userId: String)
    fun fromMenuFragmentToUsersAdsFragment()
    fun fromUsersAdsFragmentToCarDetailsFragment(adId: String, userId: String)
    fun fromFavouritesFragmentToCarDetailsFragment(adId: String, userId: String)
    fun fromSearchResultsFragmentToCarDetailsFragment(adId: String, userId: String)
    fun fromHomeFragmentToSearchFragment()
    fun fromSearchFragmentToSearchResultsFragment(query: String)
    fun fromSearchByFiltersFragmentToSearchResultsFragment(filterParams: CarFilterParams)
    fun fromAddCarFragmentToHomeFragment()
    fun fromCarDetailsFragmentToEditCarFragment(adId: String, userId: String)
    fun fromMenuFragmentToComparisonsFragment()
    fun fromComparisonsFragmentToCarDetailsFragment(adId: String, userId: String)
}