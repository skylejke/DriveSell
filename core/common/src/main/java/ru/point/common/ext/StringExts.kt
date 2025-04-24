package ru.point.common.ext

import android.telephony.PhoneNumberUtils
import java.util.Locale

fun String.isValidUserName(): Boolean {
    return this.isNotBlank() && this.length >= 5 && this.length <= 20 && !this.first().isDigit()
}

fun String.isValidPassword(): Boolean {
    return this.isNotBlank() && this.length >= 8
}

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$").matches(this)
}

fun String.isValidPhoneNumber(): Boolean {
    return this.isNotBlank() && Regex("^(\\+7|8)[0-9]{10}$").matches(this)
}

fun String.isValidVIN(): Boolean {
    return this.length == 17 && Regex("^[A-HJ-NPR-Z0-9]{17}$").matches(this)
}

fun String.toFormattedRussianPhone(): String {
    val digits = filter(Char::isDigit)
    val normalized = when {
        digits.length == 11 && (digits.startsWith("8") || digits.startsWith("7")) ->
            "7" + digits.substring(1)

        digits.length == 10 ->
            "7$digits"

        else ->
            return this
    }
    val formatted = PhoneNumberUtils.formatNumber("+$normalized", Locale.getDefault().country)
    return formatted ?: this
}
