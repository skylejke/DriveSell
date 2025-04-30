package ru.point.profile.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import ru.point.common.di.FeatureDepsProvider
import ru.point.common.ext.bottomBar
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ext.showSnackbar
import ru.point.common.ext.toFormattedRussianPhone
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment
import ru.point.profile.R
import ru.point.profile.databinding.FragmentProfileBinding
import ru.point.profile.di.DaggerProfileComponent
import javax.inject.Inject

internal class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val profileViewModel by viewModels<ProfileViewModel> { profileViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerProfileComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .build()
            .inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomBar.hide()

        profileViewModel.refreshUserData()

        repeatOnLifecycleScope {
            profileViewModel.status.collect { status ->
                updatePlaceholder(status)
            }
        }

        repeatOnLifecycleScope {
            profileViewModel.userData.collect { userData ->
                with(binding.profileCard) {
                    profileUsernameTv.text = userData?.username.toString()
                    profileEmailTv.text = userData?.email.toString()
                    profilePhoneNumberTv.text = userData?.phoneNumber.orEmpty().toFormattedRussianPhone()
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

        binding.profileNoConnectionPlaceholder.tryAgainTv.setOnClickListener {
            profileViewModel.refreshUserData()
        }
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                profileShimmerLayout.isVisible = true
                profileShimmerLayout.startShimmer()
                profileCard.root.isVisible = false
                deleteProfileBtn.isVisible = false
                changePasswordBtn.isVisible = false
                profileNoConnectionPlaceholder.root.isVisible = false
            }

            is Status.Success -> {
                profileShimmerLayout.isVisible = false
                profileShimmerLayout.stopShimmer()
                profileCard.root.isVisible = true
                deleteProfileBtn.isVisible = true
                changePasswordBtn.isVisible = true
                profileNoConnectionPlaceholder.root.isVisible = false
            }

            is Status.Error -> {
                profileShimmerLayout.isVisible = false
                profileShimmerLayout.stopShimmer()
                profileCard.root.isVisible = false
                deleteProfileBtn.isVisible = false
                changePasswordBtn.isVisible = false
                profileNoConnectionPlaceholder.root.isVisible = true
            }
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

    private fun deleteProfile() {
        profileViewModel.deleteUser()
        navigator.fromProfileFragmentToLoginFragment()
        showSnackbar(binding.root, getString(R.string.deleted_profile))
    }
}