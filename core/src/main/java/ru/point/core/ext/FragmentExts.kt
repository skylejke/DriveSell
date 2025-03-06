package ru.point.core.ext

import androidx.fragment.app.Fragment
import ru.point.core.navigation.BottomBarManager

val Fragment.bottomBar
    get() = (requireActivity() as BottomBarManager)