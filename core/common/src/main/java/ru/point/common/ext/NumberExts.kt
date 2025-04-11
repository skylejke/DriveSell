package ru.point.common.ext

fun Short.isValidPower() = this in 1..9999

fun Double.isValidCapacity() = this in 0.1..100.0

object NumberErrorConsts {

    const val ERROR_INT_VALUE = -1

    const val ERROR_SHORT_VALUE: Short = -1

    const val ERROR_BYTE_VALUE: Byte = -1

    const val ERROR_DOUBLE_VALUE: Double = -1.0
}