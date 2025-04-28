package ru.point.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
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
import ru.point.home.R
import ru.point.home.databinding.FragmentHomeBinding
import ru.point.home.di.DaggerHomeComponent
import javax.inject.Inject


internal class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private val homeViewModel by viewModels<HomeViewModel> { homeViewModelFactory }

    private var _carAdapter: CarAdapter? = null
    private val carAdapter get() = requireNotNull(_carAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerHomeComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .build()
            .inject(this)

        _carAdapter = CarAdapter { navigator.fromHomeFragmentToCarDetailsFragment(it.id, it.userId) }
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getCars()

        binding.carList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carAdapter
            addItemDecoration(CarAdapterDecorator())
        }

        repeatOnLifecycleScope {
            combine(homeViewModel.status, homeViewModel.cars) { status, cars -> status to cars }
                .collect { (status, cars) ->
                    with(binding) {
                        updatePlaceholder(status)
                        if (status == Status.Success) {
                            nothingFoundPlaceHolder.root.isVisible = cars.isEmpty()
                            sortBtn.isVisible = cars.isNotEmpty()
                            carAdsCounter.isVisible = cars.isNotEmpty()
                            carList.isVisible = cars.isNotEmpty()
                            if (cars.isNotEmpty()) {
                                carAdapter.submitList(cars) { carList.scrollToPosition(0) }
                                carAdsCounter.text =
                                    resources.getQuantityString(R.plurals.car_ads_counter, cars.size, cars.size)
                            }
                        }
                    }
                }
        }

        binding.noConnectionPlaceholder.tryAgainTv.setOnClickListener {
            homeViewModel.getCars()
        }

        binding.homeToolBar.searchIcon.setOnClickListener {
            navigator.fromHomeFragmentToSearchFragment()
        }

        binding.swipeRefresh.setOnRefreshListener {
            homeViewModel.getCars()
        }

        binding.sortBtn.setOnClickListener {
            showSortMenu()
        }
    }

    private fun updatePlaceholder(status: Status) {
        with(binding) {
            when (status) {
                is Status.Loading -> {
                    shimmerLayout.isVisible = true
                    shimmerLayout.startShimmer()
                    carList.isVisible = false
                    noConnectionPlaceholder.root.isVisible = false
                    nothingFoundPlaceHolder.root.isVisible = false
                    swipeRefresh.isRefreshing = true
                    swipeRefresh.isVisible = true
                }

                is Status.Success -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.isVisible = false
                    carList.isVisible = true
                    noConnectionPlaceholder.root.isVisible = false
                    swipeRefresh.isRefreshing = false
                    swipeRefresh.isVisible = true
                }

                is Status.Error -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.isVisible = false
                    carList.isVisible = false
                    noConnectionPlaceholder.root.isVisible = true
                    nothingFoundPlaceHolder.root.isVisible = false
                    swipeRefresh.isRefreshing = false
                    swipeRefresh.isVisible = false
                }
            }
        }
    }

    private fun showSortMenu() {
        val sortMenu = PopupMenu(requireContext(), binding.sortBtn)
        sortMenu.menuInflater.inflate(R.menu.sort_menu, sortMenu.menu)

        sortMenu.setOnMenuItemClickListener { sortItem: MenuItem ->
            when (sortItem.itemId) {
                R.id.lowest_price -> {
                    homeViewModel.getCars(SortParams.PRICE.toString(), OrderParams.ASC.toString())
                    true
                }

                R.id.highest_price -> {
                    homeViewModel.getCars(SortParams.PRICE.toString(), OrderParams.DESC.toString())
                    true
                }

                R.id.lowest_mileage -> {
                    homeViewModel.getCars(SortParams.MILEAGE.toString(), OrderParams.ASC.toString())
                    true
                }

                R.id.highest_mileage -> {
                    homeViewModel.getCars(SortParams.MILEAGE.toString(), OrderParams.DESC.toString())
                    true
                }

                R.id.newest_year -> {
                    homeViewModel.getCars(SortParams.YEAR.toString(), OrderParams.DESC.toString())
                    true
                }

                R.id.oldest_year -> {
                    homeViewModel.getCars(SortParams.YEAR.toString(), OrderParams.ASC.toString())
                    true
                }

                R.id.newest_listed -> {
                    homeViewModel.getCars(SortParams.DATE.toString(), OrderParams.DESC.toString())
                    true
                }

                R.id.oldest_listed -> {
                    homeViewModel.getCars(SortParams.DATE.toString(), OrderParams.ASC.toString())
                    true
                }

                else -> false
            }
        }

        sortMenu.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _carAdapter = null
    }
}