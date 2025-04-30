package ru.point.drivesell.utils

import androidx.navigation.NavController
import ru.point.car_details.ui.CarDetailsFragmentDirections
import ru.point.common.model.CarFilterParams
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
        navController.navigate(R.id.action_loginFragment_to_homeFragment)
    }

    override fun fromRegisterFragmentToHomeFragment() {
        navController.navigate(R.id.action_registerFragment_to_homeFragment)
    }

    override fun fromMenuFragmentToProfileFragment() {
        navController.navigate(R.id.action_menuFragment_to_profileFragment)
    }

    override fun fromMenuFragmentToLogInFragment() {
        arrayOf(
            R.id.homeFragment,
            R.id.searchByFiltersFragment,
            R.id.addCarFragment,
            R.id.favouritesFragment,
            R.id.menuFragment
        ).forEach { id ->
            navController.clearBackStack(id)
        }
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
            HomeFragmentDirections.actionHomeFragmentToCarDetailsFragment(
                adId = adId,
                userId = userId
            )
        )
    }

    override fun fromMenuFragmentToUsersAdsFragment() {
        navController.navigate(R.id.action_menuFragment_to_usersAdsFragment)
    }

    override fun fromUsersAdsFragmentToCarDetailsFragment(adId: String, userId: String) {
        navController.navigate(
            UsersAdsFragmentDirections.actionUsersAdsFragmentToCarDetailsFragment(
                adId = adId,
                userId = userId
            )
        )
    }

    override fun fromFavouritesFragmentToCarDetailsFragment(adId: String, userId: String) {
        navController.navigate(
            FavouritesFragmentDirections.actionFavouritesFragmentToCarDetailsFragment(
                adId = adId,
                userId = userId
            )
        )
    }

    override fun fromSearchResultsFragmentToCarDetailsFragment(adId: String, userId: String) {
        navController.navigate(
            SearchResultsFragmentDirections.actionSearchResultsFragmentToCarDetailsFragment(
                adId = adId,
                userId = userId
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
               filterParams = CarFilterParams()
            )
        )
    }

    override fun fromSearchByFiltersFragmentToSearchResultsFragment(filterParams: CarFilterParams) {
        navController.navigate(
            SearchByFiltersFragmentDirections.actionSearchByFiltersFragmentToSearchResultsFragment(
                query = null,
                filterParams = filterParams,
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