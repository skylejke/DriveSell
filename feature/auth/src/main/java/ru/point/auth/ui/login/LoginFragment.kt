package ru.point.auth.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import jakarta.inject.Inject
import kotlinx.coroutines.flow.filterNotNull
import ru.point.auth.databinding.FragmentLoginBinding
import ru.point.auth.di.AuthComponentHolderVM
import ru.point.auth.di.authComponent
import ru.point.core.ext.bottomBar
import ru.point.core.ext.repeatOnLifecycleScope
import ru.point.core.ui.ComponentHolderFragment

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

        collectLoginFields()

        binding.usernameEt.doOnTextChanged { _, _, _, _ ->
            binding.usernameTil.error = null
        }

        binding.passwordEt.doOnTextChanged { _, _, _, _ ->
            binding.passwordTil.error = null
        }

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

    private fun collectLoginFields(){
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
                Toast.makeText(
                    requireContext(),
                    "Successfully logged in",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
