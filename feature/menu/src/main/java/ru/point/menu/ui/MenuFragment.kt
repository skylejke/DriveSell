package ru.point.menu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.point.core.ui.ComponentHolderFragment
import ru.point.menu.databinding.FragmentMenuBinding
import ru.point.menu.di.MenuComponentHolderVM
import ru.point.menu.di.menuComponent
import javax.inject.Inject

internal class MenuFragment : ComponentHolderFragment<FragmentMenuBinding>() {

    @Inject
    lateinit var menuViewModelFactory: MenuViewModelFactory

    private val menuViewModel by viewModels<MenuViewModel> { menuViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHolder<MenuComponentHolderVM>()
        menuComponent.inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMenuBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                menuViewModel.isAuthorized.filterNotNull().collect { isAuthorized ->
                    if (isAuthorized) {
                        binding.logOutBtn.isVisible = true
                        binding.logInBtn.isVisible = false
                    } else {
                        binding.logInBtn.isVisible = true
                        binding.logOutBtn.isVisible = false
                        binding.profileBtn.isVisible = false
                        binding.myAdsBtn.isVisible = false
                        binding.comparisonsBtn.isVisible = false
                    }
                }
            }
        }

        binding.logInBtn.setOnClickListener {
            navigator.fromMenuFragmentToLogInFragment()
        }

        binding.profileBtn.setOnClickListener {
            navigator.fromMenuFragmentToProfileFragment()
        }

        binding.settingsBtn.setOnClickListener {
            navigator.fromMenuFragmentToSettingsFragment()
        }

        binding.logOutBtn.setOnClickListener {
            menuViewModel.logOut()
            navigator.fromMenuFragmentToLogInFragment()
            Toast.makeText(requireContext(), "Log out", Toast.LENGTH_SHORT).show()
        }
    }
}