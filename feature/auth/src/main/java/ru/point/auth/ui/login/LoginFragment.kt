package ru.point.auth.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.point.auth.databinding.FragmentLoginBinding
import ru.point.core.navigation.BottomBarManager
import ru.point.core.ui.BaseAuthFragment

class LoginFragment : BaseAuthFragment<FragmentLoginBinding>() {

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as BottomBarManager).hide()

        binding.signInBtn.setOnClickListener {
            //navigator.fromLoginFragmentToHomeFragment()
        }

        binding.signUpBtn.setOnClickListener {
            navigator.fromLoginFragmentToRegisterFragment()
        }

        binding.continueAsAGuestBtn.setOnClickListener {
            navigator.fromLoginFragmentToHomeFragment()
        }
    }
}