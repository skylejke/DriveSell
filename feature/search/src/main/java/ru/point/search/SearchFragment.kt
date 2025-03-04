package ru.point.search

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.point.core.ui.BaseFragment
import ru.point.search.databinding.FragmentSearchBinding


class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSearchBinding.inflate(inflater, container, false)
}