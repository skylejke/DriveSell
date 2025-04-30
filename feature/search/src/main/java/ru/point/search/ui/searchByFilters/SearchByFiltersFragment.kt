package ru.point.search.ui.searchByFilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import ru.point.common.di.FeatureDepsProvider
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.model.CarFilterParams
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment
import ru.point.search.R
import ru.point.search.databinding.FragmentSearchByFiltersBinding
import ru.point.search.di.DaggerSearchComponent
import javax.inject.Inject

internal class SearchByFiltersFragment : BaseFragment<FragmentSearchByFiltersBinding>() {

    @Inject
    lateinit var searchByFiltersViewModelFactory: SearchByFiltersViewModelFactory

    private val searchByFiltersViewModel by viewModels<SearchByFiltersViewModel> { searchByFiltersViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSearchComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .build()
            .inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSearchByFiltersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.filtersFields) {
            filterFuelTypeAtv.setSimpleItems(resources.getStringArray(R.array.fuel_types))
            filterBodyTypeAtv.setSimpleItems(resources.getStringArray(R.array.body_types))
            filterColorAtv.setSimpleItems(resources.getStringArray(R.array.colors))
            filterTransmissionAtv.setSimpleItems(resources.getStringArray(R.array.transmissions))
            filterDrivetrainAtv.setSimpleItems(resources.getStringArray(R.array.drivetrains))
            filterWheelAtv.setSimpleItems(resources.getStringArray(R.array.wheels))
            filterConditionAtv.setSimpleItems(resources.getStringArray(R.array.conditions))
            filterOwnersAtv.setSimpleItems(resources.getStringArray(R.array.owners))
        }

        repeatOnLifecycleScope {
            searchByFiltersViewModel.status.collect { updatePlaceholder(it) }
        }

        repeatOnLifecycleScope {
            searchByFiltersViewModel.brands.collect { brands ->
                binding.filtersFields.filterBrandAtv.setSimpleItems(
                    brands.map { it.name }.toTypedArray()
                )
            }
        }

        repeatOnLifecycleScope {
            searchByFiltersViewModel.models.collect { models ->
                binding.filtersFields.filterModelAtv.setSimpleItems(
                    models.map { it.name }.toTypedArray()
                )
                binding.filtersFields.filterModelTil.isVisible = models.isNotEmpty()
            }
        }

        binding.filtersFields.filterBrandAtv.setOnItemClickListener { parent, _, position, _ ->
            binding.filtersFields.filterModelTil.isVisible = true
            binding.filtersFields.filterModelAtv.setText("", false)
            searchByFiltersViewModel.getModels(parent.getItemAtPosition(position) as String)
        }

        binding.filtersFields.filterSearchBtn.setOnClickListener {
            with(binding.filtersFields) {
                navigator.fromSearchByFiltersFragmentToSearchResultsFragment(
                    CarFilterParams(
                        brand = filterBrandAtv.text.toString().takeIf { it.isNotBlank() },
                        model = filterModelAtv.text.toString().takeIf { it.isNotBlank() },
                        yearMin = filterYearEtMin.text.toString().toShortOrNull(),
                        yearMax = filterYearEtMax.text.toString().toShortOrNull(),
                        priceMin = filterPriceEtMin.text.toString().toIntOrNull(),
                        priceMax = filterPriceEtMax.text.toString().toIntOrNull(),
                        mileageMin = filterMileageEtMin.text.toString().toIntOrNull(),
                        mileageMax = filterMileageEtMax.text.toString().toIntOrNull(),
                        enginePowerMin = filterEnginePowerMin.text.toString().toShortOrNull(),
                        enginePowerMax = filterEnginePowerMax.text.toString().toShortOrNull(),
                        engineCapacityMin = filterEngineCapacityMin.text.toString().toDoubleOrNull(),
                        engineCapacityMax = filterEngineCapacityMax.text.toString().toDoubleOrNull(),
                        fuelType = filterFuelTypeAtv.text.toString().takeIf { it.isNotBlank() },
                        bodyType = filterBodyTypeAtv.text.toString().takeIf { it.isNotBlank() },
                        color = filterColorAtv.text.toString().takeIf { it.isNotBlank() },
                        transmission = filterTransmissionAtv.text.toString().takeIf { it.isNotBlank() },
                        drivetrain = filterDrivetrainAtv.text.toString().takeIf { it.isNotBlank() },
                        wheel = filterWheelAtv.text.toString().takeIf { it.isNotBlank() },
                        condition = filterConditionAtv.text.toString().takeIf { it.isNotBlank() },
                        owners = filterOwnersAtv.text.toString().takeIf { it.isNotBlank() }
                    )
                )
            }
        }

        binding.filtersNoConnectionPlaceholder.tryAgainTv.setOnClickListener {
            clearFields()
            searchByFiltersViewModel.getBrands()
        }
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                filtersShimmerLayout.isVisible = true
                filtersShimmerLayout.startShimmer()
                filtersFields.root.isVisible = false
                filtersNoConnectionPlaceholder.root.isVisible = false
            }

            is Status.Success -> {
                filtersShimmerLayout.isVisible = false
                filtersShimmerLayout.stopShimmer()
                filtersFields.root.isVisible = true
                filtersNoConnectionPlaceholder.root.isVisible = false
            }

            is Status.Error -> {
                filtersShimmerLayout.isVisible = false
                filtersShimmerLayout.stopShimmer()
                filtersFields.root.isVisible = false
                filtersNoConnectionPlaceholder.root.isVisible = true
            }
        }
    }

    private fun clearFields() {
        with(binding.filtersFields) {
            filterBrandAtv.setText("", false)
            filterModelAtv.setText("", false)
            filterModelAtv.isVisible = false
            filterYearEtMin.text?.clear()
            filterYearEtMax.text?.clear()
            filterPriceEtMin.text?.clear()
            filterPriceEtMax.text?.clear()
            filterMileageEtMin.text?.clear()
            filterMileageEtMax.text?.clear()
            filterEnginePowerMin.text?.clear()
            filterEnginePowerMax.text?.clear()
            filterEngineCapacityMin.text?.clear()
            filterEngineCapacityMax.text?.clear()
            filterFuelTypeAtv.setText("", false)
            filterBodyTypeAtv.setText("", false)
            filterColorAtv.setText("", false)
            filterTransmissionAtv.setText("", false)
            filterDrivetrainAtv.setText("", false)
            filterWheelAtv.setText("", false)
            filterConditionAtv.setText("", false)
            filterOwnersAtv.setText("", false)
        }
    }
}
