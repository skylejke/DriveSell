package ru.point.comparisons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ui.ComponentHolderFragment
import ru.point.comparisons.databinding.FragmentComparisonsBinding
import ru.point.comparisons.di.ComparisonsComponentHolderVM
import ru.point.comparisons.di.comparisonsComponent
import javax.inject.Inject

internal class ComparisonsFragment : ComponentHolderFragment<FragmentComparisonsBinding>() {

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
        initHolder<ComparisonsComponentHolderVM>()
        comparisonsComponent.inject(this)
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
            comparisonsViewModel.comparedCars.collect {
                comparedCarsTitleAdapter.submitList(it)
                comparedCarsSpecsAdapter.submitList(it)
            }
        }

        binding.comparisonsToolBar.backIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }

    fun setAdapters() {
        binding.titlesRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = comparedCarsTitleAdapter
            addItemDecoration(ComparisonsDecorator())
        }

        binding.specsRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = comparedCarsSpecsAdapter
            addItemDecoration(ComparisonsDecorator())
        }

        val syncer = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (isSyncedScroll) return
                isSyncedScroll = true
                if (rv === binding.titlesRv) {
                    binding.specsRv.scrollBy(dx, dy)
                } else {
                    binding.titlesRv.scrollBy(dx, dy)
                }
                isSyncedScroll = false
            }
        }

        binding.titlesRv.addOnScrollListener(syncer)
        binding.specsRv.addOnScrollListener(syncer)
    }

    override fun onDestroy() {
        super.onDestroy()
        _comparedCarsSpecsAdapter = null
        _comparedCarsTitlesAdapter = null
    }
}