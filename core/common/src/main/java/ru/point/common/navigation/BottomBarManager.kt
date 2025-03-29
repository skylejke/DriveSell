package ru.point.common.navigation

import androidx.lifecycle.LifecycleObserver

interface BottomBarManager : LifecycleObserver {
    fun hide()
    fun show()
}