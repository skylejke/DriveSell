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
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.model.Status
import ru.point.common.ui.ComponentHolderFragment
import ru.point.favourites.databinding.FragmentFavouritesBinding
import ru.point.favourites.di.FavouritesComponentHolderVM
import ru.point.favourites.di.favouritesComponent
import javax.inject.Inject

internal class FavouritesFragment : ComponentHolderFragment<FragmentFavouritesBinding>() {

    @Inject
    lateinit var favouritesViewModelFactory: FavouritesViewModelFactory

    private val favouritesViewModel by viewModels<FavouritesViewModel> { favouritesViewModelFactory }

    private var _carAdapter: CarAdapter? = null
    private val carAdapter get() = requireNotNull(_carAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHolder<FavouritesComponentHolderVM>()
        favouritesComponent.inject(this)
        _carAdapter =
            CarAdapter { navigator.fromFavouritesFragmentToCarDetailsFragment(it.id, it.userId) }
        favouritesViewModel.getFavourites()
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFavouritesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouritesViewModel.getFavourites()

        binding.favouriteList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carAdapter
            addItemDecoration(CarAdapterDecorator())
        }

        repeatOnLifecycleScope {
            combine(favouritesViewModel.status, favouritesViewModel.favourites) { status, cars -> status to cars }
                .collect { (status, cars) ->
                    with(binding) {
                        updatePlaceholder(status)
                        if (status == Status.Success) {
                            emptyPlaceholder.root.isVisible = cars.isEmpty()
                            favouriteList.isVisible = cars.isNotEmpty()
                            if (cars.isNotEmpty()) {
                                carAdapter.submitList(cars)
                            }
                        }
                    }
                }
        }

        binding.noConnectionPlaceholder.tryAgainTv.setOnClickListener {
            favouritesViewModel.getFavourites()
        }

        binding.swipeRefresh.setOnRefreshListener {
            favouritesViewModel.getFavourites()
        }
    }

    private fun updatePlaceholder(status: Status) {
        with(binding) {
            when (status) {
                is Status.Loading -> {
                    shimmerLayout.isVisible = true
                    shimmerLayout.startShimmer()
                    favouriteList.isVisible = false
                    noConnectionPlaceholder.root.isVisible = false
                    swipeRefresh.isRefreshing = true
                }

                is Status.Success -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.isVisible = false
                    favouriteList.isVisible = true
                    noConnectionPlaceholder.root.isVisible = false
                    swipeRefresh.isRefreshing = false
                }

                is Status.Error -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.isVisible = false
                    favouriteList.isVisible = false
                    noConnectionPlaceholder.root.isVisible = true
                    swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _carAdapter = null
    }
}