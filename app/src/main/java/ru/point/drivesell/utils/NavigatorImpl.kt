package ru.point.drivesell.utils

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import ru.point.car_details.ui.CarDetailsFragmentDirections
import ru.point.common.navigation.Navigator
import ru.point.comparisons.ui.ComparisonsFragmentDirections
import ru.point.drivesell.R
import ru.point.favourites.ui.FavouritesFragmentDirections
import ru.point.home.ui.HomeFragmentDirections
import ru.point.search.ui.search.SearchFragmentDirections
import ru.point.search.ui.searchByFilters.SearchByFiltersFragmentDirections
import ru.point.search.ui.searchResults.SearchResultsFragmentDirections
import ru.point.users_ads.ui.UsersAdsFragmentDirections

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

    override fun fromMenuFragmentToLogInFragment() {
        navController.navigate(R.id.action_menuFragment_to_loginFragment)
    }

    override fun fromProfileFragmentToEditUserDataFragment() {
        navController.navigate(R.id.action_profileFragment_to_editUserDataFragment)
    }

    override fun fromProfileFragmentToEditPasswordFragment() {
        navController.navigate(R.id.action_profileFragment_to_editPasswordFragment)
    }

    override fun fromProfileFragmentToLoginFragment() {
        navController.navigate(R.id.action_profileFragment_to_loginFragment)
    }

    override fun fromHomeFragmentToCarDetailsFragment(adId: String, userId: String) {
        navController.navigate(
            HomeFragmentDirections.actionHomeFragmentToCarDetailsFragment(adId, userId)
        )
    }

    override fun fromMenuFragmentToUsersAdsFragment() {
        navController.navigate(R.id.action_menuFragment_to_usersAdsFragment)
    }

    override fun fromUsersAdsFragmentToCarDetailsFragment(adId: String, userId: String) {
        navController.navigate(
            UsersAdsFragmentDirections.actionUsersAdsFragmentToCarDetailsFragment(adId, userId)
        )
    }

    override fun fromFavouritesFragmentToCarDetailsFragment(adId: String, userId: String) {
        navController.navigate(
            FavouritesFragmentDirections.actionFavouritesFragmentToCarDetailsFragment(adId, userId)
        )
    }

    override fun fromSearchResultsFragmentToCarDetailsFragment(adId: String, userId: String) {
        navController.navigate(
            SearchResultsFragmentDirections.actionSearchResultsFragmentToCarDetailsFragment(
                adId,
                userId
            )
        )
    }

    override fun fromHomeFragmentToSearchFragment() {
        navController.navigate(R.id.action_homeFragment_to_searchFragment)
    }

    override fun fromSearchFragmentToSearchResultsFragment(query: String) {
        navController.navigate(
            SearchFragmentDirections.actionSearchFragmentToSearchResultsFragment(
                query = query,
                brand = null,
                model = null,
                yearMin = null,
                yearMax = null,
                priceMin = null,
                priceMax = null,
                mileageMin = null,
                mileageMax = null,
                enginePowerMin = null,
                enginePowerMax = null,
                engineCapacityMin = null,
                engineCapacityMax = null,
                fuelType = null,
                bodyType = null,
                color = null,
                transmission = null,
                drivetrain = null,
                wheel = null,
                condition = null,
                owners = null,
            )
        )
    }

    override fun fromSearchByFiltersFragmentToSearchResultsFragment(
        brand: String?,
        model: String?,
        yearMin: String?,
        yearMax: String?,
        priceMin: String?,
        priceMax: String?,
        mileageMin: String?,
        mileageMax: String?,
        enginePowerMin: String?,
        enginePowerMax: String?,
        engineCapacityMin: String?,
        engineCapacityMax: String?,
        fuelType: String?,
        bodyType: String?,
        color: String?,
        transmission: String?,
        drivetrain: String?,
        wheel: String?,
        condition: String?,
        owners: String?
    ) {
        navController.navigate(
            SearchByFiltersFragmentDirections.actionSearchByFiltersFragmentToSearchResultsFragment(
                query = null,
                brand = brand,
                model = model,
                yearMin = yearMin,
                yearMax = yearMax,
                priceMin = priceMin,
                priceMax = priceMax,
                mileageMin = mileageMin,
                mileageMax = mileageMax,
                enginePowerMin = enginePowerMin,
                enginePowerMax = enginePowerMax,
                engineCapacityMin = engineCapacityMin,
                engineCapacityMax = engineCapacityMax,
                fuelType = fuelType,
                bodyType = bodyType,
                color = color,
                transmission = transmission,
                drivetrain = drivetrain,
                wheel = wheel,
                condition = condition,
                owners = owners,
            )
        )
    }

    override fun fromAddCarFragmentToHomeFragment() {
        navController.navigate(R.id.action_addCarFragment_to_homeFragment)
    }

    override fun fromCarDetailsFragmentToEditCarFragment(adId: String, userId: String) {
        navController.navigate(
            CarDetailsFragmentDirections.actionCarDetailsFragmentToEditCarFragment(
                adId = adId,
                userId = userId
            )
        )
    }

    override fun fromMenuFragmentToComparisonsFragment() {
        navController.navigate(R.id.action_menuFragment_to_comparisonsFragment)
    }

    override fun fromComparisonsFragmentToCarDetailsFragment(adId: String, userId: String) {
        navController.navigate(
            ComparisonsFragmentDirections.actionComparisonsFragmentToCarDetailsFragment(
                adId = adId,
                userId = userId
            )
        )
    }
}