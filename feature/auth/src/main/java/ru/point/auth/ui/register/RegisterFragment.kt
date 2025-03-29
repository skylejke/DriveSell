package ru.point.auth.ui.register


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import kotlinx.coroutines.flow.filterNotNull
import ru.point.auth.databinding.FragmentRegisterBinding
import ru.point.auth.di.authComponent
import ru.point.common.ext.bottomBar
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ui.BaseFragment
import javax.inject.Inject


internal class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    @Inject
    lateinit var registerViewModelFactory: RegisterViewModelFactory

    private val registerViewModel by viewModels<RegisterViewModel> { registerViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authComponent.inject(this)
    }

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomBar.hide()

        repeatOnLifecycleScope {
            registerViewModel.usernameError.filterNotNull().collect {
                binding.usernameTil.error = it
            }
        }

        repeatOnLifecycleScope {
            registerViewModel.emailError.filterNotNull().collect {
                binding.emailTil.error = it
            }
        }

        repeatOnLifecycleScope {
            registerViewModel.phoneNumberError.filterNotNull().collect {
                binding.phoneNumberTil.error = it
            }
        }

        repeatOnLifecycleScope {
            registerViewModel.passwordError.filterNotNull().collect {
                binding.passwordTil.error = it
            }
        }

        repeatOnLifecycleScope {
            registerViewModel.registerEvent.filterNotNull().collect {
                navigator.fromRegisterFragmentToHomeFragment()
                Toast.makeText(
                    requireContext(),
                    "Successful registration",
                    Toast.LENGTH_SHORT
                ).show()
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

        binding.passwordEt.doOnTextChanged { _, _, _, _ ->
            binding.passwordTil.error = null
        }

        binding.signUpBtn.setOnClickListener {
            registerViewModel.register(
                username = binding.usernameEt.text.toString(),
                email = binding.emailEt.text.toString(),
                phoneNumber = binding.phoneNumberEt.text.toString(),
                password = binding.passwordEt.text.toString()
            )
        }

        binding.signInBtn.setOnClickListener {
            navigator.fromRegisterFragmentToLoginFragment()
        }

        binding.continueAsAGuestBtn.setOnClickListener {
            navigator.fromRegisterFragmentToHomeFragment()
        }
    }
}