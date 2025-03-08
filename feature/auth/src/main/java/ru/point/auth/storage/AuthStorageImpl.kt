package ru.point.auth.storage

import android.content.SharedPreferences
import androidx.core.content.edit

internal class AuthStorageImpl(private val sharedPreferences: SharedPreferences) : AuthStorage {
    override var token: String?
        get() = sharedPreferences.getString(KEY_TOKEN, null)
        set(value) {
            sharedPreferences.edit { putString(KEY_TOKEN, value) }
        }

    companion object {
        const val KEY_TOKEN = "token"
    }
}