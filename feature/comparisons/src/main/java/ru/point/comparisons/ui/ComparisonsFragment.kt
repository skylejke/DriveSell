package ru.point.comparisons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.combine
import ru.point.common.di.FeatureDepsProvider
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment
import ru.point.comparisons.databinding.FragmentComparisonsBinding
import ru.point.comparisons.di.DaggerComparisonsComponent
import ru.point.comparisons.ui.stateholder.ComparedCarsSpecsAdapter
import ru.point.comparisons.ui.stateholder.ComparedCarsTitleAdapter
import ru.point.comparisons.ui.stateholder.ComparisonsDecorator
import javax.inject.Inject

internal class ComparisonsFragment : BaseFragment<FragmentComparisonsBinding>() {

    @Inject
    lateinit var comparisonsViewModelFactory: ComparisonsViewModelFactory

    private val comparisonsViewModel by viewModels<ComparisonsViewModel> { comparisonsViewModelFactory }

    private var _comparedCarsSpecsAdapter: ComparedCarsSpecsAdapter? = null
    private val comparedCarsSpecsAdapter get() = requireNotNull(_comparedCarsSpecsAdapter)

    private var _comparedCarsTitlesAdapter: ComparedCarsTitleAdapter? = null
    private val comparedCarsTitleAdapter get() = requireNotNull(_comparedCarsTitlesAdapter)

    private var isSyncedScroll = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerComparisonsComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .build()
            .inject(this)

        _comparedCarsSpecsAdapter = ComparedCarsSpecsAdapter()
        _comparedCarsTitlesAdapter =
            ComparedCarsTitleAdapter(
                onCarPhotoClick = {
                    navigator.fromComparisonsFragmentToCarDetailsFragment(
                        adId = it.id,
                        userId = it.userId
                    )
                },
                onDeleteCarClick = { comparisonsViewModel.removeCarFromComparisons(adId = it.id) }
            )
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentComparisonsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        comparisonsViewModel.getComparedCars()

        setAdapters()

        repeatOnLifecycleScope {
            comparisonsViewModel.status.collect { status ->
                updatePlaceholder(status)
            }
        }

        repeatOnLifecycleScope {
            combine(comparisonsViewModel.status, comparisonsViewModel.comparedCars) { status, cars -> status to cars }
                .collect { (status, cars) ->
                    with(binding) {
                        updatePlaceholder(status)
                        if (status == Status.Success) {
                            comparisonsEmptyPlaceholder.root.isVisible = cars.isEmpty()
                            comparisonsSpecsRv.isVisible = cars.isNotEmpty()
                            comparisonsTitlesRv.isVisible = cars.isNotEmpty()
                            if (cars.isNotEmpty()) {
                                comparedCarsTitleAdapter.submitList(cars)
                                comparedCarsSpecsAdapter.submitList(cars)
                            }
                        }
                    }
                }
        }

        binding.comparisonsNoConnectionPlaceholder.tryAgainTv.setOnClickListener {
            comparisonsViewModel.getComparedCars()
        }

        binding.comparisonsToolBar.backIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                comparisonsShimmerLayoutTitles.isVisible = true
                comparisonsShimmerLayoutTitles.startShimmer()

                comparisonsShimmerLayoutSpecs.isVisible = true
                comparisonsShimmerLayoutSpecs.startShimmer()

                comparisonsTitlesRv.isVisible = false
                comparisonsSpecsRv.isVisible = false

                comparisonsNoConnectionPlaceholder.root.isVisible = false
            }

            is Status.Success -> {
                comparisonsShimmerLayoutTitles.isVisible = false
                comparisonsShimmerLayoutTitles.stopShimmer()

                comparisonsShimmerLayoutSpecs.isVisible = false
                comparisonsShimmerLayoutSpecs.stopShimmer()

                comparisonsTitlesRv.isVisible = true
                comparisonsSpecsRv.isVisible = true

                comparisonsNoConnectionPlaceholder.root.isVisible = false
            }

            is Status.Error -> {
                comparisonsShimmerLayoutTitles.isVisible = false
                comparisonsShimmerLayoutTitles.stopShimmer()

                comparisonsShimmerLayoutSpecs.isVisible = false
                comparisonsShimmerLayoutSpecs.stopShimmer()

                comparisonsTitlesRv.isVisible = false
                comparisonsSpecsRv.isVisible = false

                comparisonsNoConnectionPlaceholder.root.isVisible = true
            }
        }
    }

    fun setAdapters() {
        binding.comparisonsTitlesRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = comparedCarsTitleAdapter
            addItemDecoration(ComparisonsDecorator())
        }

        binding.comparisonsSpecsRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = comparedCarsSpecsAdapter
            addItemDecoration(ComparisonsDecorator())
        }

        val syncer = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (isSyncedScroll) return
                isSyncedScroll = true
                if (rv === binding.comparisonsTitlesRv) {
                    binding.comparisonsSpecsRv.scrollBy(dx, dy)
                } else {
                    binding.comparisonsTitlesRv.scrollBy(dx, dy)
                }
                isSyncedScroll = false
            }
        }

        binding.comparisonsTitlesRv.addOnScrollListener(syncer)
        binding.comparisonsSpecsRv.addOnScrollListener(syncer)
    }

    override fun onDestroy() {
        super.onDestroy()
        _comparedCarsSpecsAdapter = null
        _comparedCarsTitlesAdapter = null
    }
}