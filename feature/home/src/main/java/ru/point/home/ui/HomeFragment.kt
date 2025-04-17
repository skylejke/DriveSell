package ru.point.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.point.cars.model.enums.OrderParams
import ru.point.cars.model.enums.SortParams
import ru.point.cars.ui.CarAdapter
import ru.point.cars.ui.CarAdapterDecorator
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ui.ComponentHolderFragment
import ru.point.home.R
import ru.point.home.databinding.FragmentHomeBinding
import ru.point.home.di.HomeComponentHolderVM
import ru.point.home.di.homeComponent
import javax.inject.Inject


internal class HomeFragment : ComponentHolderFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private val homeViewModel by viewModels<HomeViewModel> { homeViewModelFactory }

    private var _carAdapter: CarAdapter? = null
    private val carAdapter get() = requireNotNull(_carAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _carAdapter = CarAdapter { navigator.fromHomeFragmentToCarDetailsFragment(it.id, it.userId) }
        initHolder<HomeComponentHolderVM>()
        homeComponent.inject(this)
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
            homeViewModel.cars.collect { carList ->
                carAdapter.submitList(carList)
                binding.carAdsCounter.text =
                    resources.getQuantityString(
                        R.plurals.car_ads_counter,
                        carList.size,
                        carList.size
                    )
            }
        }

        binding.homeToolBar.searchIcon.setOnClickListener {
            navigator.fromHomeFragmentToSearchFragment()
        }

        binding.sortBtn.setOnClickListener {
            showSortMenu()
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