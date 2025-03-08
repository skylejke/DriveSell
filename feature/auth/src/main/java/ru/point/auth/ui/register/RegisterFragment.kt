package ru.point.auth.ui.register


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.point.auth.databinding.FragmentRegisterBinding
import ru.point.auth.di.authComponent
import ru.point.core.ext.bottomBar
import ru.point.core.ui.ComponentHolderFragment
import javax.inject.Inject


internal class RegisterFragment : ComponentHolderFragment<FragmentRegisterBinding>() {

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

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                registerViewModel.registerState.filterNotNull().collect { state ->
                    if (state) {
                        navigator.fromRegisterFragmentToHomeFragment()
                        Toast.makeText(
                            requireContext(),
                            "Successful registration",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
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