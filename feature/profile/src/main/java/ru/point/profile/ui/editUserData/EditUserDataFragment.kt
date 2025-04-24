package ru.point.profile.ui.editUserData

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import kotlinx.coroutines.flow.filterNotNull
import ru.point.common.ext.bottomBar
import ru.point.common.ext.clearErrorOnTextChanged
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment
import ru.point.profile.databinding.FragmentEditUserDataBinding
import ru.point.profile.di.profileComponent
import javax.inject.Inject

internal class EditUserDataFragment : BaseFragment<FragmentEditUserDataBinding>() {

    @Inject
    lateinit var editUserDataViewModelFactory: EditUserDataViewModelFactory

    private val editUserDataViewModel by viewModels<EditUserDataViewModel> { editUserDataViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileComponent.inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentEditUserDataBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomBar.hide()

        repeatOnLifecycleScope {
            editUserDataViewModel.status.collect { status ->
                updatePlaceholder(status)
            }
        }

        repeatOnLifecycleScope {
            editUserDataViewModel.userData.filterNotNull().collect { userData ->
                with(binding) {
                    usernameEt.setText(userData.username.toString())
                    emailEt.setText(userData.email.toString())
                    phoneNumberEt.setText(userData.phoneNumber.toString())
                }
            }
        }

        repeatOnLifecycleScope {
            editUserDataViewModel.usernameError.filterNotNull().collect {
                binding.usernameTil.error = it
            }
        }

        repeatOnLifecycleScope {
            editUserDataViewModel.emailError.filterNotNull().collect {
                binding.emailTil.error = it
            }
        }

        repeatOnLifecycleScope {
            editUserDataViewModel.phoneNumberError.filterNotNull().collect {
                binding.phoneNumberTil.error = it
            }
        }

        repeatOnLifecycleScope {
            editUserDataViewModel.userDataChangedEvent.collect {
                navigator.popBackStack()
            }
        }

        binding.usernameEt.clearErrorOnTextChanged(binding.usernameTil)

        binding.emailEt.clearErrorOnTextChanged(binding.emailTil)

        binding.phoneNumberEt.clearErrorOnTextChanged(binding.phoneNumberTil)

        binding.fragmentEditUserDataToolBar.checkIcon.setOnClickListener {
            editUserDataViewModel.editUserData(
                username = binding.usernameEt.text.toString().takeIf { it.isNotBlank() },
                email = binding.emailEt.text.toString().takeIf { it.isNotBlank() },
                phoneNumber = binding.phoneNumberEt.text.toString().takeIf { it.isNotBlank() }
            )
        }

        binding.noConnectionPlaceholder.tryAgainTv.setOnClickListener {
            editUserDataViewModel.getUserData()
        }

        binding.fragmentEditUserDataToolBar.closeIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                shimmerLayout.isVisible = true
                shimmerLayout.startShimmer()
                fragmentEditUserDataToolBar.checkIcon.isVisible = false
                usernameTil.isVisible = false
                emailTil.isVisible = false
                phoneNumberTil.isVisible = false
                noConnectionPlaceholder.root.isVisible = false
            }

            is Status.Success -> {
                shimmerLayout.isVisible = false
                shimmerLayout.stopShimmer()
                fragmentEditUserDataToolBar.checkIcon.isVisible = true
                usernameTil.isVisible = true
                emailTil.isVisible = true
                phoneNumberTil.isVisible = true
                noConnectionPlaceholder.root.isVisible = false
            }

            is Status.Error -> {
                shimmerLayout.isVisible = false
                shimmerLayout.stopShimmer()
                fragmentEditUserDataToolBar.checkIcon.isVisible = false
                usernameTil.isVisible = false
                emailTil.isVisible = false
                phoneNumberTil.isVisible = false
                noConnectionPlaceholder.root.isVisible = true
            }
        }
    }
}