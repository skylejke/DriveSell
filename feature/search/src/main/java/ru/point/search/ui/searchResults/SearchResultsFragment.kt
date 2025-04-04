package ru.point.search.ui.searchResults

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import ru.point.cars.ui.CarAdapter
import ru.point.cars.ui.SpacerItemDecorator
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ui.BaseFragment
import ru.point.search.R
import ru.point.search.databinding.FragmentSearchResultsBinding
import ru.point.search.di.searchComponent
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

        searchComponent.inject(this)

        _carAdapter =
            CarAdapter { navigator.fromSearchResultsFragmentToCarDetailsFragment(it.id, it.userId) }

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
            owners = args.owners
        )
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSearchResultsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.carList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carAdapter
            addItemDecoration(SpacerItemDecorator())
        }

        repeatOnLifecycleScope {
            searchResultsViewModel.foundAds.collect {
                carAdapter.submitList(it)
                binding.carAdsCounter.text =
                    resources.getQuantityString(
                        R.plurals.car_ads_counter,
                        it.size,
                        it.size
                    )
            }
        }

        binding.searchResultsToolBar.backIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _carAdapter = null
    }
}