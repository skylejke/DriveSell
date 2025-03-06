package ru.point.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class ComponentHolderFragment<T : ViewBinding> : BaseFragment<T>() {
    inline fun <reified T : ViewModel> initHolder() {
        val viewModel = ViewModelProvider(this)[T::class.java]
    }
}