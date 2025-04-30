package ru.point.auth.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import jakarta.inject.Inject
import kotlinx.coroutines.flow.filterNotNull
import ru.point.auth.R
import ru.point.auth.databinding.FragmentLoginBinding
import ru.point.auth.di.DaggerAuthComponent
import ru.point.common.di.FeatureDepsProvider
import ru.point.common.ext.bottomBar
import ru.point.common.ext.clearErrorOnTextChanged
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ext.showSnackbar
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment

internal class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory

    private val loginViewModel by viewModels<LoginViewModel> { loginViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerAuthComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .build()
            .inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomBar.hide()

        repeatOnLifecycleScope {
            loginViewModel.status.filterNotNull().collect {
                updatePlaceholder(it)
            }
        }

        repeatOnLifecycleScope {
            loginViewModel.usernameError.filterNotNull().collect {
                binding.loginPasswordTil.error = it
            }
        }
        repeatOnLifecycleScope {
            loginViewModel.passwordError.filterNotNull().collect {
                binding.loginPasswordTil.error = it
            }
        }

        repeatOnLifecycleScope {
            loginViewModel.loginEvent.filterNotNull().collect {
                navigator.fromLoginFragmentToHomeFragment()
            }
        }

        binding.loginUsernameEt.clearErrorOnTextChanged(binding.loginPasswordTil)

        binding.loginPasswordEt.clearErrorOnTextChanged(binding.loginPasswordTil)

        binding.loginBtn.setOnClickListener {
            loginViewModel.login(
                username = binding.loginUsernameEt.text.toString(),
                password = binding.loginPasswordEt.text.toString()
            )
        }

        binding.navigateToRegisterBtn.setOnClickListener {
            navigator.fromLoginFragmentToRegisterFragment()
        }

        binding.loginContinueAsAGuestTv.setOnClickListener {
            navigator.fromLoginFragmentToHomeFragment()
        }
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                loginLoadingPlaceholder.root.isVisible = true
            }

            is Status.Success -> {
                loginLoadingPlaceholder.root.isVisible = false
                showSnackbar(root, getString(R.string.success_sign_in))
            }

            is Status.Error -> {
                loginLoadingPlaceholder.root.isVisible = false
                showSnackbar(root, getString(R.string.something_went_wrong))
            }
        }
    }
}
