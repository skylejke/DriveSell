package ru.point.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.point.core.ext.bottomBar
import ru.point.core.ui.ComponentHolderFragment
import ru.point.profile.databinding.FragmentProfileBinding
import ru.point.profile.di.ProfileComponentHolderVM
import ru.point.profile.di.profileComponent
import javax.inject.Inject

internal class ProfileFragment : ComponentHolderFragment<FragmentProfileBinding>() {

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val profileViewModel by viewModels<ProfileViewModel> { profileViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHolder<ProfileComponentHolderVM>()
        profileComponent.inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomBar.hide()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.userData.collect { userData ->
                    with(binding.profileCard) {
                        username.text = userData?.username.toString()
                        email.text = userData?.email.toString()
                        phoneNumber.text = userData?.phoneNumber.toString()
                    }
                }
            }
        }

        binding.profileToolBar.backIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }
}