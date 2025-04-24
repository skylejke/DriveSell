package ru.point.common.model

sealed interface Status {
    data object Success : Status
    data object Loading : Status
    data object Error : Status
}