package ru.point.auth.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import jakarta.inject.Inject
import ru.point.auth.databinding.FragmentLoginBinding
import ru.point.auth.di.ComponentHolderAuthVM
import ru.point.auth.di.authComponent
import ru.point.core.ext.bottomBar
import ru.point.core.ui.ComponentHolderFragment

class LoginFragment : ComponentHolderFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory

    private val loginViewModel by viewModels<LoginViewModel> { loginViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHolder<ComponentHolderAuthVM>()
        authComponent.inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomBar.hide()

        binding.signInBtn.setOnClickListener {
            navigator.fromLoginFragmentToHomeFragment()
        }

        binding.signUpBtn.setOnClickListener {
            navigator.fromLoginFragmentToRegisterFragment()
        }

        binding.continueAsAGuestBtn.setOnClickListener {
            navigator.fromLoginFragmentToHomeFragment()
        }
    }
}