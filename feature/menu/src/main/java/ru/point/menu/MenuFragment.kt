package ru.point.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.point.core.ui.BaseFragment
import ru.point.menu.databinding.FragmentMenuBinding


class MenuFragment : BaseFragment<FragmentMenuBinding>() {
    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMenuBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileBtn.setOnClickListener {
            navigator.fromMenuFragmentToProfileFragment()
        }

        binding.settingsBtn.setOnClickListener {
            navigator.fromMenuFragmentToSettingsFragment()
        }
    }
}