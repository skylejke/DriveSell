package ru.point.search.ui.searchByFilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ui.ComponentHolderFragment
import ru.point.search.R
import ru.point.search.databinding.FragmentSearchByFiltersBinding
import ru.point.search.di.SearchComponentHolderVM
import ru.point.search.di.searchComponent
import javax.inject.Inject

internal class SearchByFiltersFragment : ComponentHolderFragment<FragmentSearchByFiltersBinding>() {

    private var _brandsAdapter: ArrayAdapter<String>? = null
    private val brandsAdapter get() = requireNotNull(_brandsAdapter)

    private var _modelsAdapter: ArrayAdapter<String>? = null
    private val modelsAdapter get() = requireNotNull(_modelsAdapter)

    private var _fuelTypeAdapter: ArrayAdapter<String>? = null
    private val fuelTypeAdapter get() = requireNotNull(_fuelTypeAdapter)

    private var _bodyTypeAdapter: ArrayAdapter<String>? = null
    private val bodyTypeAdapter get() = requireNotNull(_bodyTypeAdapter)

    private var _colorAdapter: ArrayAdapter<String>? = null
    private val colorAdapter get() = requireNotNull(_colorAdapter)

    private var _drivetrainAdapter: ArrayAdapter<String>? = null
    private val drivetrainAdapter get() = requireNotNull(_drivetrainAdapter)

    private var _wheelAdapter: ArrayAdapter<String>? = null
    private val wheelAdapter get() = requireNotNull(_wheelAdapter)

    private var _conditionAdapter: ArrayAdapter<String>? = null
    private val conditionAdapter get() = requireNotNull(_conditionAdapter)

    private var _ownersAdapter: ArrayAdapter<String>? = null
    private val ownersAdapter get() = requireNotNull(_ownersAdapter)

    private var _transmissionAdapter: ArrayAdapter<String>? = null
    private val transmissionAdapter get() = requireNotNull(_transmissionAdapter)

    @Inject
    lateinit var searchByFiltersViewModelFactory: SearchByFiltersViewModelFactory

    private val searchByFiltersViewModel by viewModels<SearchByFiltersViewModel> { searchByFiltersViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeAdapters()
        initHolder<SearchComponentHolderVM>()
        searchComponent.inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSearchByFiltersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()

        repeatOnLifecycleScope {
            searchByFiltersViewModel.brands.collect { brands ->
                brandsAdapter.clear()
                brandsAdapter.addAll(brands.map { it.name })
                brandsAdapter.notifyDataSetChanged()
            }
        }

        repeatOnLifecycleScope {
            searchByFiltersViewModel.models.collect { models ->
                modelsAdapter.clear()
                modelsAdapter.addAll(models.map { it.name })
                modelsAdapter.notifyDataSetChanged()
            }
        }

        binding.brandListTv.setOnItemClickListener { parent, view, position, id ->
            binding.modelList.isVisible = true
            binding.modelListTv.setText("", false)
            searchByFiltersViewModel.getModels(parent.getItemAtPosition(position) as String)
        }

        binding.searchBtn.setOnClickListener {
            navigator.fromSearchByFiltersFragmentToSearchResultsFragment(
                brand = binding.brandListTv.text.toString().takeIf { it.isNotBlank() },
                model = binding.modelListTv.text.toString().takeIf { it.isNotBlank() },
                yearMin = binding.yearEtMin.text.toString().takeIf { it.isNotBlank() },
                yearMax = binding.yearEtMax.text.toString().takeIf { it.isNotBlank() },
                priceMin = binding.priceEtMin.text.toString().takeIf { it.isNotBlank() },
                priceMax = binding.priceEtMax.text.toString().takeIf { it.isNotBlank() },
                mileageMin = binding.mileageEtMin.text.toString().takeIf { it.isNotBlank() },
                mileageMax = binding.mileageEtMax.text.toString().takeIf { it.isNotBlank() },
                enginePowerMin = binding.enginePowerMin.text.toString().takeIf { it.isNotBlank() },
                enginePowerMax = binding.enginePowerMax.text.toString().takeIf { it.isNotBlank() },
                engineCapacityMin = binding.engineCapacityMin.text.toString().takeIf { it.isNotBlank() },
                engineCapacityMax = binding.engineCapacityMax.text.toString().takeIf { it.isNotBlank() },
                fuelType = binding.fuelTypeListTv.text.toString().takeIf { it.isNotBlank() },
                bodyType = binding.bodyTypeListTv.text.toString().takeIf { it.isNotBlank() },
                color = binding.colorTv.text.toString().takeIf { it.isNotBlank() },
                transmission = binding.transmissionListTv.text.toString().takeIf { it.isNotBlank() },
                drivetrain = binding.drivetrainListTv.text.toString().takeIf { it.isNotBlank() },
                wheel = binding.wheelListTv.text.toString().takeIf { it.isNotBlank() },
                condition = binding.conditionListTv.text.toString().takeIf { it.isNotBlank() },
                owners = binding.ownersTv.text.toString().takeIf { it.isNotBlank() }
            )
        }
    }

    private fun initializeAdapters() {
        _brandsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item
        )

        _modelsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item
        )

        _fuelTypeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.fuel_types)
        )

        _bodyTypeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.body_types)
        )

        _colorAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.colors)
        )

        _drivetrainAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.drivetrains)
        )

        _wheelAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.wheels)
        )

        _conditionAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.conditions)
        )

        _ownersAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.owners)
        )

        _transmissionAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.transmissions)
        )
    }

    private fun setAdapters() = with(binding) {
        brandListTv.setAdapter(brandsAdapter)

        modelListTv.setAdapter(modelsAdapter)

        fuelTypeListTv.setAdapter(fuelTypeAdapter)

        bodyTypeListTv.setAdapter(bodyTypeAdapter)

        colorTv.setAdapter(colorAdapter)

        transmissionListTv.setAdapter(transmissionAdapter)

        drivetrainListTv.setAdapter(drivetrainAdapter)

        wheelListTv.setAdapter(wheelAdapter)

        conditionListTv.setAdapter(conditionAdapter)

        ownersTv.setAdapter(ownersAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearAdapters()
    }

    private fun clearAdapters() {
        _brandsAdapter = null
        _modelsAdapter = null
        _fuelTypeAdapter = null
        _bodyTypeAdapter = null
        _colorAdapter = null
        _drivetrainAdapter = null
        _wheelAdapter = null
        _conditionAdapter = null
        _ownersAdapter = null
        _transmissionAdapter = null
    }
}