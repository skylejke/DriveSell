package ru.point.auth.ui.register


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.point.auth.databinding.FragmentRegisterBinding
import ru.point.auth.di.authComponent
import ru.point.core.navigation.BottomBarManager
import ru.point.core.ui.ComponentHolderFragment
import javax.inject.Inject


class RegisterFragment : ComponentHolderFragment<FragmentRegisterBinding>() {

    @Inject
    lateinit var registerViewModelFactory: RegisterViewModelFactory

    private val registerViewModel by viewModels<RegisterViewModel>{ registerViewModelFactory }

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
        (requireActivity() as BottomBarManager).hide()

        binding.signUpBtn.setOnClickListener {
            navigator.fromRegisterFragmentToHomeFragment()
        }

        binding.signInBtn.setOnClickListener {
            navigator.fromRegisterFragmentToLoginFragment()
        }

        binding.continueAsAGuestBtn.setOnClickListener {
            navigator.fromRegisterFragmentToHomeFragment()
        }
    }
}