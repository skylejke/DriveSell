package ru.point.profile.ui.editPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import kotlinx.coroutines.flow.filterNotNull
import ru.point.common.ext.bottomBar
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ui.BaseFragment
import ru.point.profile.databinding.FragmentEditPasswordBinding
import ru.point.profile.di.profileComponent
import javax.inject.Inject


internal class EditPasswordFragment : BaseFragment<FragmentEditPasswordBinding>() {

    @Inject
    lateinit var editPasswordViewModelFactory: EditPasswordViewModelFactory

    private val editPasswordViewModel by viewModels<EditPasswordViewModel> { editPasswordViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileComponent.inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentEditPasswordBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomBar.hide()

        repeatOnLifecycleScope {
            editPasswordViewModel.oldPasswordError.filterNotNull().collect {
                binding.oldPasswordTil.error = it
            }
        }

        repeatOnLifecycleScope {
            editPasswordViewModel.newPasswordError.filterNotNull().collect {
                binding.newPasswordTil.error = it
            }
        }

        repeatOnLifecycleScope {
            editPasswordViewModel.confirmNewPasswordError.filterNotNull().collect {
                binding.confirmNewPasswordTil.error = it
            }
        }

        repeatOnLifecycleScope {
            editPasswordViewModel.passwordChangedEvent.collect {
                navigator.popBackStack()
            }
        }

        binding.oldPasswordEt.doOnTextChanged { _, _, _, _ ->
            binding.oldPasswordTil.error = null
        }

        binding.newPasswordEt.doOnTextChanged { _, _, _, _ ->
            binding.newPasswordTil.error = null
        }

        binding.confirmNewPasswordEt.doOnTextChanged { _, _, _, _ ->
            binding.confirmNewPasswordTil.error = null
        }

        binding.fragmentEditUserDataToolBar.checkIcon.setOnClickListener {
            editPasswordViewModel.editPassword(
                oldPassword = binding.oldPasswordEt.text.toString(),
                newPassword = binding.newPasswordEt.text.toString(),
                confirmNewPassword = binding.confirmNewPasswordEt.text.toString()
            )
        }

        binding.fragmentEditUserDataToolBar.closeIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }
}