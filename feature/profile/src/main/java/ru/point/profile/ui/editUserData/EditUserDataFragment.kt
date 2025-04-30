package ru.point.profile.ui.editUserData

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import kotlinx.coroutines.flow.filterNotNull
import ru.point.common.di.FeatureDepsProvider
import ru.point.common.ext.bottomBar
import ru.point.common.ext.clearErrorOnTextChanged
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment
import ru.point.profile.databinding.FragmentEditUserDataBinding
import ru.point.profile.di.DaggerProfileComponent
import javax.inject.Inject

internal class EditUserDataFragment : BaseFragment<FragmentEditUserDataBinding>() {

    @Inject
    lateinit var editUserDataViewModelFactory: EditUserDataViewModelFactory

    private val editUserDataViewModel by viewModels<EditUserDataViewModel> { editUserDataViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerProfileComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .build()
            .inject(this)
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
                    editUserDataUsernameEt.setText(userData.username.toString())
                    editUserDataEmailEt.setText(userData.email.toString())
                    editUserDataPhoneNumberEt.setText(userData.phoneNumber.toString())
                }
            }
        }

        repeatOnLifecycleScope {
            editUserDataViewModel.usernameError.filterNotNull().collect {
                binding.editUserDataUsernameTil.error = it
            }
        }

        repeatOnLifecycleScope {
            editUserDataViewModel.emailError.filterNotNull().collect {
                binding.editUserDataEmailTil.error = it
            }
        }

        repeatOnLifecycleScope {
            editUserDataViewModel.phoneNumberError.filterNotNull().collect {
                binding.editUserDataPhoneNumberTil.error = it
            }
        }

        repeatOnLifecycleScope {
            editUserDataViewModel.userDataChangedEvent.collect {
                navigator.popBackStack()
            }
        }

        binding.editUserDataUsernameEt.clearErrorOnTextChanged(binding.editUserDataUsernameTil)

        binding.editUserDataEmailEt.clearErrorOnTextChanged(binding.editUserDataEmailTil)

        binding.editUserDataPhoneNumberEt.clearErrorOnTextChanged(binding.editUserDataPhoneNumberTil)

        binding.editUserDataToolBar.checkIcon.setOnClickListener {
            editUserDataViewModel.editUserData(
                username = binding.editUserDataUsernameEt.text.toString().takeIf { it.isNotBlank() },
                email = binding.editUserDataEmailEt.text.toString().takeIf { it.isNotBlank() },
                phoneNumber = binding.editUserDataPhoneNumberEt.text.toString().takeIf { it.isNotBlank() }
            )
        }

        binding.editUserDataNoConnectionPlaceholder.tryAgainTv.setOnClickListener {
            editUserDataViewModel.getUserData()
        }

        binding.editUserDataToolBar.closeIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                editUserDataShimmerLayout.isVisible = true
                editUserDataShimmerLayout.startShimmer()
                editUserDataToolBar.checkIcon.isVisible = false
                editUserDataUsernameTil.isVisible = false
                editUserDataEmailTil.isVisible = false
                editUserDataPhoneNumberTil.isVisible = false
                editUserDataNoConnectionPlaceholder.root.isVisible = false
            }

            is Status.Success -> {
                editUserDataShimmerLayout.isVisible = false
                editUserDataShimmerLayout.stopShimmer()
                editUserDataToolBar.checkIcon.isVisible = true
                editUserDataUsernameTil.isVisible = true
                editUserDataEmailTil.isVisible = true
                editUserDataPhoneNumberTil.isVisible = true
                editUserDataNoConnectionPlaceholder.root.isVisible = false
            }

            is Status.Error -> {
                editUserDataShimmerLayout.isVisible = false
                editUserDataShimmerLayout.stopShimmer()
                editUserDataToolBar.checkIcon.isVisible = false
                editUserDataUsernameTil.isVisible = false
                editUserDataEmailTil.isVisible = false
                editUserDataPhoneNumberTil.isVisible = false
                editUserDataNoConnectionPlaceholder.root.isVisible = true
            }
        }
    }
}