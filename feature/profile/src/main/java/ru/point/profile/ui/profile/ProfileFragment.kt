package ru.point.profile.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import ru.point.common.ext.bottomBar
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ui.ComponentHolderFragment
import ru.point.profile.R
import ru.point.profile.databinding.FragmentProfileBinding
import ru.point.profile.di.ProfileComponentHolderVM
import ru.point.profile.di.profileComponent
import javax.inject.Inject

internal class ProfileFragment : ComponentHolderFragment<FragmentProfileBinding>() {

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val profileViewModel by viewModels<ProfileViewModel> { profileViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHolder<ProfileComponentHolderVM>()
        profileComponent.inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomBar.hide()

        profileViewModel.refreshUserData()

        repeatOnLifecycleScope {
            profileViewModel.userData.collect { userData ->
                with(binding.profileCard) {
                    username.text = userData?.username.toString()
                    email.text = userData?.email.toString()
                    phoneNumber.text = userData?.phoneNumber.toString()
                }
            }
        }

        repeatOnLifecycleScope {
            profileViewModel.deleteProfileEvent.collect {
                navigator.fromProfileFragmentToLoginFragment()
            }
        }

        binding.deleteProfileBtn.setOnClickListener {
            DeleteProfileDialog { deleteProfile() }
                .show(
                    childFragmentManager,
                    DeleteProfileDialog.TAG
                )
        }

        binding.profileToolBar.editIcon.setOnClickListener {
            navigator.fromProfileFragmentToEditUserDataFragment()
        }

        binding.profileToolBar.backIcon.setOnClickListener {
            navigator.popBackStack()
        }

        binding.changePasswordBtn.setOnClickListener {
            navigator.fromProfileFragmentToEditPasswordFragment()
        }
    }

    class DeleteProfileDialog(private val onPositiveClick: () -> Unit) : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.delete_profile_dialog_title))
                .setMessage(getString(R.string.delete_profile_dialog_message))
                .setPositiveButton(getString(R.string.delete_profile_dialog_positive_button)) { dialog, _ ->
                    onPositiveClick()
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.delete_profile_dialog_negative_button)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

        companion object {
            const val TAG = "DeleteProfileDialog"
        }
    }

    private fun deleteProfile(){
        profileViewModel.deleteUser()
        navigator.fromProfileFragmentToLoginFragment()
        Toast.makeText(requireContext(), "Deleted Profile", Toast.LENGTH_SHORT).show()
    }
}