package ru.point.add_car

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.point.add_car.databinding.FragmentAddCarBinding
import ru.point.common.ui.BaseFragment


class AddCarFragment : BaseFragment<FragmentAddCarBinding>() {
    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAddCarBinding.inflate(inflater, container, false)
}