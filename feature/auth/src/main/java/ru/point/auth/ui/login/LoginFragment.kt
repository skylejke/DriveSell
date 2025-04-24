package ru.point.auth.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import jakarta.inject.Inject
import kotlinx.coroutines.flow.filterNotNull
import ru.point.auth.databinding.FragmentLoginBinding
import ru.point.auth.di.AuthComponentHolderVM
import ru.point.auth.di.authComponent
import ru.point.common.ext.bottomBar
import ru.point.common.ext.clearErrorOnTextChanged
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ext.showSnackbar
import ru.point.common.model.Status
import ru.point.common.ui.ComponentHolderFragment

internal class LoginFragment : ComponentHolderFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory

    private val loginViewModel by viewModels<LoginViewModel> { loginViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHolder<AuthComponentHolderVM>()
        authComponent.inject(this)
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
                binding.usernameTil.error = it
            }
        }
        repeatOnLifecycleScope {
            loginViewModel.passwordError.filterNotNull().collect {
                binding.passwordTil.error = it
            }
        }

        repeatOnLifecycleScope {
            loginViewModel.loginEvent.filterNotNull().collect {
                navigator.fromLoginFragmentToHomeFragment()
            }
        }

        binding.usernameEt.clearErrorOnTextChanged(binding.usernameTil)

        binding.passwordEt.clearErrorOnTextChanged(binding.passwordTil)

        binding.signInBtn.setOnClickListener {
            loginViewModel.login(
                username = binding.usernameEt.text.toString(),
                password = binding.passwordEt.text.toString()
            )
        }

        binding.signUpBtn.setOnClickListener {
            navigator.fromLoginFragmentToRegisterFragment()
        }

        binding.continueAsAGuestBtn.setOnClickListener {
            navigator.fromLoginFragmentToHomeFragment()
        }
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                loadingPlaceholder.root.isVisible = true
            }

            is Status.Success -> {
                loadingPlaceholder.root.isVisible = false
                showSnackbar(root, "Successfully logged in")
            }

            is Status.Error -> {
                loadingPlaceholder.root.isVisible = false
                showSnackbar(root, "Something went wrong")
            }
        }
    }
}
