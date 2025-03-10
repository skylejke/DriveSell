package ru.point.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.point.core.ext.bottomBar
import ru.point.core.ui.BaseFragment
import ru.point.settings.databinding.FragmentSettingsBinding


class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomBar.hide()

        binding.fragmentSettingsToolbar.backIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }
}