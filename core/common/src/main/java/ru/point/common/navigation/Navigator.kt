package ru.point.common.navigation

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
    fun fromSearchByFiltersFragmentToSearchResultsFragment(
        brand: String? = null,
        model: String? = null,
        yearMin: String? = null,
        yearMax: String? = null,
        priceMin: String? = null,
        priceMax: String? = null,
        mileageMin: String? = null,
        mileageMax: String? = null,
        enginePowerMin: String? = null,
        enginePowerMax: String? = null,
        engineCapacityMin: String? = null,
        engineCapacityMax: String? = null,
        fuelType: String? = null,
        bodyType: String? = null,
        color: String? = null,
        transmission: String? = null,
        drivetrain: String? = null,
        wheel: String? = null,
        condition: String? = null,
        owners: String? = null
    )
    fun fromAddCarFragmentToHomeFragment()
    fun fromCarDetailsFragmentToEditCarFragment(adId: String, userId: String)
    fun fromMenuFragmentToComparisonsFragment()
    fun fromComparisonsFragmentToCarDetailsFragment(adId: String, userId: String)
}