package ru.point.profile.ui.editUserData

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import kotlinx.coroutines.flow.filterNotNull
import ru.point.core.ext.bottomBar
import ru.point.core.ext.repeatOnLifecycleScope
import ru.point.core.ui.BaseFragment
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

        binding.usernameEt.doOnTextChanged { _, _, _, _ ->
            binding.usernameTil.error = null
        }

        binding.emailEt.doOnTextChanged { _, _, _, _ ->
            binding.emailTil.error = null
        }

        binding.phoneNumberEt.doOnTextChanged { _, _, _, _ ->
            binding.phoneNumberTil.error = null
        }

        binding.fragmentEditUserDataToolBar.checkIcon.setOnClickListener {
            editUserDataViewModel.editUserData(
                username = binding.usernameEt.text.toString().takeIf { it.isNotBlank() },
                email = binding.emailEt.text.toString().takeIf { it.isNotBlank() },
                phoneNumber = binding.phoneNumberEt.text.toString().takeIf { it.isNotBlank() }
            )
        }

        binding.fragmentEditUserDataToolBar.closeIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }
}