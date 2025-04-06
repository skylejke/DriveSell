package ru.point.favourites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.point.cars.ui.CarAdapter
import ru.point.cars.ui.CarAdapterDecorator
import ru.point.common.ext.repeatOnLifecycleScope
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

        binding.favouriteList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carAdapter
            addItemDecoration(CarAdapterDecorator())
        }

        repeatOnLifecycleScope {
            favouritesViewModel.favourites.collect { favourites ->
                carAdapter.submitList(favourites)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _carAdapter = null
    }
}