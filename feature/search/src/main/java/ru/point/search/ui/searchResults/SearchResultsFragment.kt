package ru.point.search.ui.searchResults

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.combine
import ru.point.cars.model.enums.OrderParams
import ru.point.cars.model.enums.SortParams
import ru.point.cars.ui.CarAdapter
import ru.point.cars.ui.CarAdapterDecorator
import ru.point.common.di.FeatureDepsProvider
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment
import ru.point.search.R
import ru.point.search.databinding.FragmentSearchResultsBinding
import ru.point.search.di.DaggerSearchComponent
import javax.inject.Inject

internal class SearchResultsFragment : BaseFragment<FragmentSearchResultsBinding>() {

    @Inject
    lateinit var searchResultsViewModelFactory: SearchResultsViewModelFactory

    private val searchResultsViewModel by viewModels<SearchResultsViewModel> { searchResultsViewModelFactory }

    private var _carAdapter: CarAdapter? = null
    private val carAdapter get() = requireNotNull(_carAdapter)

    private val args: SearchResultsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSearchComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .build()
            .inject(this)

        _carAdapter = CarAdapter { navigator.fromSearchResultsFragmentToCarDetailsFragment(it.id, it.userId) }
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSearchResultsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSearchedCars()

        binding.searchResultsCarsRv.apply {
            adapter = carAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(CarAdapterDecorator())
        }

        repeatOnLifecycleScope {
            combine(searchResultsViewModel.status, searchResultsViewModel.foundAds) { status, cars -> status to cars }
                .collect { (status, cars) ->
                    with(binding) {
                        updatePlaceholder(status)
                        if (status == Status.Success) {
                            searchResultsNothingFoundPlaceHolder.root.isVisible = cars.isEmpty()
                            searchResultsSortBtn.isVisible = cars.isNotEmpty()
                            searchResultsCarAdsCounter.isVisible = cars.isNotEmpty()
                            searchResultsCarsRv.isVisible = cars.isNotEmpty()
                            if (cars.isNotEmpty()) {
                                carAdapter.submitList(cars) { searchResultsCarsRv.scrollToPosition(0) }
                                searchResultsCarAdsCounter.text =
                                    resources.getQuantityString(R.plurals.car_ads_counter, cars.size, cars.size)
                            }
                        }
                    }
                }
        }

        binding.searchResultsSortBtn.setOnClickListener {
            showSortMenu()
        }

        binding.searchResultsNoConnectionPlaceholder.tryAgainTv.setOnClickListener {
            getSearchedCars()
        }

        binding.searchResultsSwipeRefresh.setOnRefreshListener {
            getSearchedCars()
        }

        binding.searchResultsToolBar.backIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }

    private fun updatePlaceholder(status: Status) {
        with(binding) {
            when (status) {
                is Status.Loading -> {
                    searchResultsShimmerLayout.isVisible = true
                    searchResultsShimmerLayout.startShimmer()
                    searchResultsCarsRv.isVisible = false
                    searchResultsNoConnectionPlaceholder.root.isVisible = false
                    searchResultsNothingFoundPlaceHolder.root.isVisible = false
                    searchResultsSwipeRefresh.isRefreshing = true
                    searchResultsSwipeRefresh.isVisible = true
                }

                is Status.Success -> {
                    searchResultsShimmerLayout.stopShimmer()
                    searchResultsShimmerLayout.isVisible = false
                    searchResultsCarsRv.isVisible = true
                    searchResultsNoConnectionPlaceholder.root.isVisible = false
                    searchResultsSwipeRefresh.isRefreshing = false
                    searchResultsSwipeRefresh.isVisible = true
                }

                is Status.Error -> {
                    searchResultsShimmerLayout.isVisible = false
                    searchResultsShimmerLayout.stopShimmer()
                    searchResultsSortBtn.isVisible = false
                    searchResultsCarAdsCounter.isVisible = false
                    searchResultsCarsRv.isVisible = false
                    searchResultsNoConnectionPlaceholder.root.isVisible = true
                    searchResultsNothingFoundPlaceHolder.root.isVisible = false
                    searchResultsSwipeRefresh.isRefreshing = false
                    searchResultsSwipeRefresh.isVisible = false
                }
            }
        }
    }

    private fun showSortMenu() {
        val sortMenu = PopupMenu(requireContext(), binding.searchResultsSortBtn)
        sortMenu.menuInflater.inflate(R.menu.sort_menu, sortMenu.menu)

        sortMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.lowest_price -> {
                    getSearchedCars(SortParams.PRICE.toString(), OrderParams.ASC.toString())
                    true
                }

                R.id.highest_price -> {
                    getSearchedCars(SortParams.PRICE.toString(), OrderParams.DESC.toString())
                    true
                }

                R.id.lowest_mileage -> {
                    getSearchedCars(SortParams.MILEAGE.toString(), OrderParams.ASC.toString())
                    true
                }

                R.id.highest_mileage -> {
                    getSearchedCars(SortParams.MILEAGE.toString(), OrderParams.DESC.toString())
                    true
                }

                R.id.newest_year -> {
                    getSearchedCars(SortParams.YEAR.toString(), OrderParams.DESC.toString())
                    true
                }

                R.id.oldest_year -> {
                    getSearchedCars(SortParams.YEAR.toString(), OrderParams.ASC.toString())
                    true
                }

                R.id.newest_listed -> {
                    getSearchedCars(SortParams.DATE.toString(), OrderParams.DESC.toString())
                    true
                }

                R.id.oldest_listed -> {
                    getSearchedCars(SortParams.DATE.toString(), OrderParams.ASC.toString())
                    true
                }

                else -> false
            }
        }

        sortMenu.show()
    }

    private fun getSearchedCars(
        sortParam: String = SortParams.DATE.toString(),
        orderParam: String = OrderParams.DESC.toString()
    ) {
        if (args.query != null) {
            searchResultsViewModel.searchCarsByQuery(
                query = args.query!!,
                sortParam = sortParam,
                orderParam = orderParam
            )
        } else {
            searchResultsViewModel.searchCarsByFilters(
                brand = args.brand,
                model = args.model,
                yearMin = args.yearMin?.toShort(),
                yearMax = args.yearMax?.toShort(),
                priceMin = args.priceMin?.toInt(),
                priceMax = args.priceMax?.toInt(),
                mileageMin = args.mileageMin?.toInt(),
                mileageMax = args.mileageMax?.toInt(),
                enginePowerMin = args.enginePowerMin?.toShort(),
                enginePowerMax = args.enginePowerMax?.toShort(),
                engineCapacityMin = args.engineCapacityMin?.toDouble(),
                engineCapacityMax = args.engineCapacityMax?.toDouble(),
                fuelType = args.fuelType,
                bodyType = args.bodyType,
                color = args.color,
                transmission = args.transmission,
                drivetrain = args.drivetrain,
                wheel = args.wheel,
                condition = args.condition,
                owners = args.owners,
                sortParam = sortParam,
                orderParam = orderParam
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _carAdapter = null
    }
}