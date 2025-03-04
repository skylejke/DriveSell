package ru.point.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.point.core.ui.BaseFragment
import ru.point.favourites.databinding.FragmentFavouritesBinding


class FavouritesFragment : BaseFragment<FragmentFavouritesBinding>() {
    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFavouritesBinding.inflate(inflater, container, false)
}