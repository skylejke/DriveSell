package ru.point.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import ru.point.core.ext.repeatOnLifecycleScope
import ru.point.core.ui.ComponentHolderFragment
import ru.point.search.R
import ru.point.search.databinding.FragmentSearchBinding
import ru.point.search.di.SearchComponentHolderVM
import ru.point.search.di.searchComponent
import javax.inject.Inject

internal class SearchFragment : ComponentHolderFragment<FragmentSearchBinding>() {

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
    lateinit var searchViewModelFactory: SearchViewModelFactory

    private val searchViewModel by viewModels<SearchViewModel> { searchViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeAdapters()
        initHolder<SearchComponentHolderVM>()
        searchComponent.inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()

        repeatOnLifecycleScope {
            searchViewModel.brands.collect { brands ->
                brandsAdapter.clear()
                brandsAdapter.addAll(brands.map { it.name })
                brandsAdapter.notifyDataSetChanged()
            }
        }

        repeatOnLifecycleScope {
            searchViewModel.models.collect { models ->
                modelsAdapter.clear()
                modelsAdapter.addAll(models.map { it.name })
                modelsAdapter.notifyDataSetChanged()
            }
        }

        binding.brandListTv.setOnItemClickListener { parent, view, position, id ->
            val selectedBrand = parent.getItemAtPosition(position) as String
            binding.modelList.isVisible = true
            searchViewModel.getModels(selectedBrand)
        }
    }

    private fun initializeAdapters() {
        _brandsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item
        )

        _modelsAdapter = ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item)

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

    private fun setAdapters() {
        binding.brandListTv.setAdapter(brandsAdapter)

        binding.modelListTv.setAdapter(modelsAdapter)

        binding.fuelTypeListTv.setAdapter(fuelTypeAdapter)

        binding.bodyTypeListTv.setAdapter(bodyTypeAdapter)

        binding.colorTv.setAdapter(colorAdapter)

        binding.transmissionListTv.setAdapter(transmissionAdapter)

        binding.drivetrainListTv.setAdapter(drivetrainAdapter)

        binding.wheelListTv.setAdapter(wheelAdapter)

        binding.conditionListTv.setAdapter(conditionAdapter)

        binding.ownersTv.setAdapter(ownersAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
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