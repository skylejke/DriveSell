package ru.point.core.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import ru.point.core.navigation.BottomBarManager

abstract class BaseAuthFragment<T : ViewBinding> : BaseFragment<T>() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as BottomBarManager).hide()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}