package ru.point.search.ui.searchByFilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.model.Status
import ru.point.common.ui.ComponentHolderFragment
import ru.point.search.R
import ru.point.search.databinding.FragmentSearchByFiltersBinding
import ru.point.search.di.SearchComponentHolderVM
import ru.point.search.di.searchComponent
import javax.inject.Inject

internal class SearchByFiltersFragment : ComponentHolderFragment<FragmentSearchByFiltersBinding>() {

    @Inject
    lateinit var searchByFiltersViewModelFactory: SearchByFiltersViewModelFactory

    private val searchByFiltersViewModel by viewModels<SearchByFiltersViewModel> { searchByFiltersViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHolder<SearchComponentHolderVM>()
        searchComponent.inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSearchByFiltersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clearFields()

        with(binding.filtersFields) {
            fuelTypeListTv.setSimpleItems(resources.getStringArray(R.array.fuel_types))
            bodyTypeListTv.setSimpleItems(resources.getStringArray(R.array.body_types))
            colorTv.setSimpleItems(resources.getStringArray(R.array.colors))
            transmissionListTv.setSimpleItems(resources.getStringArray(R.array.transmissions))
            drivetrainListTv.setSimpleItems(resources.getStringArray(R.array.drivetrains))
            wheelListTv.setSimpleItems(resources.getStringArray(R.array.wheels))
            conditionListTv.setSimpleItems(resources.getStringArray(R.array.conditions))
            ownersTv.setSimpleItems(resources.getStringArray(R.array.owners))
        }

        searchByFiltersViewModel.getBrands()

        repeatOnLifecycleScope {
            searchByFiltersViewModel.status.collect { updatePlaceholder(it) }
        }

        repeatOnLifecycleScope {
            searchByFiltersViewModel.brands.collect { brands ->
                binding.filtersFields.brandListTv.setSimpleItems(
                    brands.map { it.name }.toTypedArray()
                )
            }
        }

        repeatOnLifecycleScope {
            searchByFiltersViewModel.models.collect { models ->
                binding.filtersFields.modelListTv.setSimpleItems(
                    models.map { it.name }.toTypedArray()
                )
                binding.filtersFields.modelList.isVisible = models.isNotEmpty()
            }
        }

        binding.filtersFields.brandListTv.setOnItemClickListener { parent, _, position, _ ->
            binding.filtersFields.modelList.isVisible = true
            binding.filtersFields.modelListTv.setText("", false)
            searchByFiltersViewModel.getModels(parent.getItemAtPosition(position) as String)
        }

        binding.filtersFields.searchBtn.setOnClickListener {
            with(binding.filtersFields) {
                navigator.fromSearchByFiltersFragmentToSearchResultsFragment(
                    brand = brandListTv.text.toString().takeIf { it.isNotBlank() },
                    model = modelListTv.text.toString().takeIf { it.isNotBlank() },
                    yearMin = yearEtMin.text.toString().takeIf { it.isNotBlank() },
                    yearMax = yearEtMax.text.toString().takeIf { it.isNotBlank() },
                    priceMin = priceEtMin.text.toString().takeIf { it.isNotBlank() },
                    priceMax = priceEtMax.text.toString().takeIf { it.isNotBlank() },
                    mileageMin = mileageEtMin.text.toString().takeIf { it.isNotBlank() },
                    mileageMax = mileageEtMax.text.toString().takeIf { it.isNotBlank() },
                    enginePowerMin = enginePowerMin.text.toString().takeIf { it.isNotBlank() },
                    enginePowerMax = enginePowerMax.text.toString().takeIf { it.isNotBlank() },
                    engineCapacityMin = engineCapacityMin.text.toString().takeIf { it.isNotBlank() },
                    engineCapacityMax = engineCapacityMax.text.toString().takeIf { it.isNotBlank() },
                    fuelType = fuelTypeListTv.text.toString().takeIf { it.isNotBlank() },
                    bodyType = bodyTypeListTv.text.toString().takeIf { it.isNotBlank() },
                    color = colorTv.text.toString().takeIf { it.isNotBlank() },
                    transmission = transmissionListTv.text.toString().takeIf { it.isNotBlank() },
                    drivetrain = drivetrainListTv.text.toString().takeIf { it.isNotBlank() },
                    wheel = wheelListTv.text.toString().takeIf { it.isNotBlank() },
                    condition = conditionListTv.text.toString().takeIf { it.isNotBlank() },
                    owners = ownersTv.text.toString().takeIf { it.isNotBlank() }
                )
            }
        }

        binding.noConnectionPlaceholder.tryAgainTv.setOnClickListener {
            clearFields()
            searchByFiltersViewModel.getBrands()
        }
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                shimmerLayout.isVisible = true
                shimmerLayout.startShimmer()
                filtersFields.root.isVisible = false
                noConnectionPlaceholder.root.isVisible = false
            }

            is Status.Success -> {
                shimmerLayout.isVisible = false
                shimmerLayout.stopShimmer()
                filtersFields.root.isVisible = true
                noConnectionPlaceholder.root.isVisible = false
            }

            is Status.Error -> {
                shimmerLayout.isVisible = false
                shimmerLayout.stopShimmer()
                filtersFields.root.isVisible = false
                noConnectionPlaceholder.root.isVisible = true
            }
        }
    }

    private fun clearFields() {
        with(binding.filtersFields) {
            brandListTv.setText("", false)
            modelListTv.setText("", false)
            modelList.isVisible = false

            yearEtMin.text?.clear()
            yearEtMax.text?.clear()

            priceEtMin.text?.clear()
            priceEtMax.text?.clear()

            mileageEtMin.text?.clear()
            mileageEtMax.text?.clear()

            enginePowerMin.text?.clear()
            enginePowerMax.text?.clear()

            engineCapacityMin.text?.clear()
            engineCapacityMax.text?.clear()

            fuelTypeListTv.setText("", false)
            bodyTypeListTv.setText("", false)
            colorTv.setText("", false)
            transmissionListTv.setText("", false)
            drivetrainListTv.setText("", false)
            wheelListTv.setText("", false)
            conditionListTv.setText("", false)
            ownersTv.setText("", false)
        }
    }
}
