package ru.point.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.point.cars.ui.CarAdapter
import ru.point.cars.ui.SpacerItemDecorator
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

        binding.carList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carAdapter
            addItemDecoration(SpacerItemDecorator())
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _carAdapter = null
    }
}