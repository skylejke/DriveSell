package ru.point.cars.ui

fun interface OnAdapterItemClick<T> {
    fun onClick(t: T)
}