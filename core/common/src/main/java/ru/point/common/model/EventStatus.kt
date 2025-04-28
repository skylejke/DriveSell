package ru.point.common.model

sealed interface EventState {
    data object Success : EventState
    data object Failure : EventState
}