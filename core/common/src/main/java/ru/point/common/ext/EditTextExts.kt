package ru.point.common.ext

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout

fun EditText.clearErrorOnTextChanged(til: TextInputLayout) {
    doOnTextChanged { _, _, _, _ ->
        til.error = null
    }
}