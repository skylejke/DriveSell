package ru.point.profile.ui.editPassword

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
import ru.point.common.ext.showSnackbar
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment
import ru.point.profile.R
import ru.point.profile.databinding.FragmentEditPasswordBinding
import ru.point.profile.di.DaggerProfileComponent
import javax.inject.Inject


internal class EditPasswordFragment : BaseFragment<FragmentEditPasswordBinding>() {

    @Inject
    lateinit var editPasswordViewModelFactory: EditPasswordViewModelFactory

    private val editPasswordViewModel by viewModels<EditPasswordViewModel> { editPasswordViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerProfileComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .build()
            .inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentEditPasswordBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomBar.hide()

        repeatOnLifecycleScope {
            editPasswordViewModel.status.filterNotNull().collect {
                updatePlaceholder(it)
            }
        }

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

        binding.oldPasswordEt.clearErrorOnTextChanged(binding.oldPasswordTil)

        binding.newPasswordEt.clearErrorOnTextChanged(binding.newPasswordTil)

        binding.confirmNewPasswordEt.clearErrorOnTextChanged(binding.confirmNewPasswordTil)

        binding.editPasswordToolBar.checkIcon.setOnClickListener {
            editPasswordViewModel.editPassword(
                oldPassword = binding.oldPasswordEt.text.toString(),
                newPassword = binding.newPasswordEt.text.toString(),
                confirmNewPassword = binding.confirmNewPasswordEt.text.toString()
            )
        }

        binding.editPasswordToolBar.closeIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                loadingPlaceholder.root.isVisible = true
            }

            is Status.Success -> {
                loadingPlaceholder.root.isVisible = false
                showSnackbar(binding.root, getString(R.string.successfully_updated_password))
            }

            is Status.Error -> {
                loadingPlaceholder.root.isVisible = false
                showSnackbar(binding.root, getString(R.string.something_went_wrong))
            }
        }
    }
}