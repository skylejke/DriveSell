package ru.point.auth.ui.register


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import kotlinx.coroutines.flow.filterNotNull
import ru.point.auth.R
import ru.point.auth.databinding.FragmentRegisterBinding
import ru.point.auth.di.DaggerAuthComponent
import ru.point.common.di.FeatureDepsProvider
import ru.point.common.ext.bottomBar
import ru.point.common.ext.clearErrorOnTextChanged
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ext.showSnackbar
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment
import javax.inject.Inject


internal class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    @Inject
    lateinit var registerViewModelFactory: RegisterViewModelFactory

    private val registerViewModel by viewModels<RegisterViewModel> { registerViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerAuthComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .build()
            .inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRegisterBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomBar.hide()

        repeatOnLifecycleScope {
            registerViewModel.status.filterNotNull().collect {
                updatePlaceholder(it)
            }
        }

        repeatOnLifecycleScope {
            registerViewModel.usernameError.filterNotNull().collect {
                binding.registerUsernameTil.error = it
            }
        }

        repeatOnLifecycleScope {
            registerViewModel.emailError.filterNotNull().collect {
                binding.registerEmailTil.error = it
            }
        }

        repeatOnLifecycleScope {
            registerViewModel.phoneNumberError.filterNotNull().collect {
                binding.registerPhoneNumberTil.error = it
            }
        }

        repeatOnLifecycleScope {
            registerViewModel.passwordError.filterNotNull().collect {
                binding.registerPasswordTil.error = it
            }
        }

        repeatOnLifecycleScope {
            registerViewModel.registerEvent.filterNotNull().collect {
                navigator.fromRegisterFragmentToHomeFragment()
            }
        }

        binding.registerUsernameEt.clearErrorOnTextChanged(binding.registerUsernameTil)

        binding.registerEmailEt.clearErrorOnTextChanged(binding.registerEmailTil)

        binding.registerPhoneNumberEt.clearErrorOnTextChanged(binding.registerPhoneNumberTil)

        binding.registerPasswordEt.clearErrorOnTextChanged(binding.registerPasswordTil)

        binding.registerBtn.setOnClickListener {
            registerViewModel.register(
                username = binding.registerUsernameEt.text.toString(),
                email = binding.registerEmailEt.text.toString(),
                phoneNumber = binding.registerPhoneNumberEt.text.toString(),
                password = binding.registerPasswordEt.text.toString()
            )
        }

        binding.navigateToLoginBtn.setOnClickListener {
            navigator.fromRegisterFragmentToLoginFragment()
        }

        binding.registerContinueAsAGuestTv.setOnClickListener {
            navigator.fromRegisterFragmentToHomeFragment()
        }
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                registerLoadingPlaceholder.root.isVisible = true
            }

            is Status.Success -> {
                registerLoadingPlaceholder.root.isVisible = false
                showSnackbar(root, getString(R.string.success_sign_up))
            }

            is Status.Error -> {
                registerLoadingPlaceholder.root.isVisible = false
                showSnackbar(root, getString(R.string.something_went_wrong))
            }
        }
    }
}