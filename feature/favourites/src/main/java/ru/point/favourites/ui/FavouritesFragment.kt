package ru.point.favourites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.combine
import ru.point.cars.ui.CarAdapter
import ru.point.cars.ui.CarAdapterDecorator
import ru.point.common.di.FeatureDepsProvider
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment
import ru.point.favourites.databinding.FragmentFavouritesBinding
import ru.point.favourites.di.DaggerFavouritesComponent
import javax.inject.Inject

internal class FavouritesFragment : BaseFragment<FragmentFavouritesBinding>() {

    @Inject
    lateinit var favouritesViewModelFactory: FavouritesViewModelFactory

    private val favouritesViewModel by viewModels<FavouritesViewModel> { favouritesViewModelFactory }

    private var _carAdapter: CarAdapter? = null
    private val carAdapter get() = requireNotNull(_carAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerFavouritesComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .build()
            .inject(this)

        _carAdapter =
            CarAdapter { navigator.fromFavouritesFragmentToCarDetailsFragment(it.id, it.userId) }
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFavouritesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouritesViewModel.getFavourites()

        binding.favouritesRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carAdapter
            addItemDecoration(CarAdapterDecorator())
        }

        repeatOnLifecycleScope {
            combine(favouritesViewModel.status, favouritesViewModel.favouriteCars) { status, cars -> status to cars }
                .collect { (status, cars) ->
                    with(binding) {
                        updatePlaceholder(status)
                        if (status == Status.Success) {
                            favouritesEmptyPlaceholder.root.isVisible = cars.isEmpty()
                            favouritesRv.isVisible = cars.isNotEmpty()
                            if (cars.isNotEmpty()) {
                                carAdapter.submitList(cars)
                            }
                        }
                    }
                }
        }

        binding.favouritesNoConnectionPlaceholder.tryAgainTv.setOnClickListener {
            favouritesViewModel.getFavourites()
        }

        binding.favouritesSwipeRefresh.setOnRefreshListener {
            favouritesViewModel.getFavourites()
        }
    }

    private fun updatePlaceholder(status: Status) {
        with(binding) {
            when (status) {
                is Status.Loading -> {
                    favouritesShimmerLayout.isVisible = true
                    favouritesShimmerLayout.startShimmer()
                    favouritesRv.isVisible = false
                    favouritesNoConnectionPlaceholder.root.isVisible = false
                    favouritesSwipeRefresh.isRefreshing = true
                    favouritesSwipeRefresh.isVisible = true
                }

                is Status.Success -> {
                    favouritesShimmerLayout.stopShimmer()
                    favouritesShimmerLayout.isVisible = false
                    favouritesRv.isVisible = true
                    favouritesNoConnectionPlaceholder.root.isVisible = false
                    favouritesSwipeRefresh.isRefreshing = false
                    favouritesSwipeRefresh.isVisible = true
                }

                is Status.Error -> {
                    favouritesShimmerLayout.stopShimmer()
                    favouritesShimmerLayout.isVisible = false
                    favouritesRv.isVisible = false
                    favouritesNoConnectionPlaceholder.root.isVisible = true
                    favouritesSwipeRefresh.isRefreshing = false
                    favouritesSwipeRefresh.isVisible = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _carAdapter = null
    }
}