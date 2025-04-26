package ru.point.menu.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.point.common.ext.showSnackbar
import ru.point.common.ui.ComponentHolderFragment
import ru.point.menu.R
import ru.point.menu.databinding.FragmentMenuBinding
import ru.point.menu.di.MenuComponentHolderVM
import ru.point.menu.di.menuComponent
import javax.inject.Inject

internal class MenuFragment : ComponentHolderFragment<FragmentMenuBinding>() {

    @Inject
    lateinit var menuViewModelFactory: MenuViewModelFactory

    private val menuViewModel by viewModels<MenuViewModel> { menuViewModelFactory }

    private var _menuAdapter: MenuAdapter? = null
    private val menuAdapter get() = requireNotNull(_menuAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHolder<MenuComponentHolderVM>()
        menuComponent.inject(this)
        _menuAdapter = MenuAdapter(
            onProfileClick = { navigator.fromMenuFragmentToProfileFragment() },
            onUsersAdsClick = { navigator.fromMenuFragmentToUsersAdsFragment() },
            onComparisonsClick = { navigator.fromMenuFragmentToComparisonsFragment() },
            onAboutClick = {},
            onLogOutClick = { LogOutDialog { logOut() }.show(childFragmentManager, LogOutDialog.TAG) },
            onLogInClick = { navigator.fromMenuFragmentToLogInFragment() }
        )
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMenuBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.menuRv.adapter = _menuAdapter
        binding.menuRv.layoutManager = NonScrollableLinearLayoutManager(requireContext())
        binding.menuRv.addItemDecoration(MenuDecorator())

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                menuViewModel.isAuthorized.filterNotNull().collect { isAuthorized ->
                    if (isAuthorized) {
                        menuAdapter.submitList(
                            listOf(
                                MenuItem.Profile,
                                MenuItem.UsersAds,
                                MenuItem.Comparisons,
                                MenuItem.About,
                                MenuItem.LogOut
                            )
                        )
                    } else {
                        menuAdapter.submitList(
                            listOf(
                                MenuItem.Login,
                                MenuItem.About
                            )
                        )
                    }
                }
            }
        }
    }

    class LogOutDialog(private val onPositiveClick: () -> Unit) : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.logout_dialog_title))
                .setMessage(getString(R.string.logout_dialog_message))
                .setPositiveButton(getString(R.string.logout_dialog_positive_button)) { dialog, _ ->
                    onPositiveClick()
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.logout_dialog_negative_button)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

        companion object {
            const val TAG = "LogOutDialog"
        }
    }

    private fun logOut() {
        menuViewModel.logOut()
        navigator.fromMenuFragmentToLogInFragment()
        showSnackbar(binding.root, "Log out")
    }

    override fun onDestroy() {
        super.onDestroy()
        _menuAdapter = null
    }
}