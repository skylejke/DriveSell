package ru.point.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.point.core.ui.BaseFragment
import ru.point.profile.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)
}