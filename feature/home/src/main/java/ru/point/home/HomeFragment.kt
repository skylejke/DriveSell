package ru.point.home

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.point.core.ui.BaseFragment
import ru.point.home.databinding.FragmentHomeBinding


class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)
}