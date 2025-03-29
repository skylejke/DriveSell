package ru.point.common.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.point.common.navigation.BottomBarManager

val Fragment.bottomBar
    get() = (requireActivity() as BottomBarManager)

fun Fragment.repeatOnLifecycleScope(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend () -> Unit
) {
    lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state = state) {
            block()
        }
    }
}