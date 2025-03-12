package ru.point.user.storage

import android.content.SharedPreferences
import androidx.core.content.edit

class TokenStorageImpl(private val sharedPreferences: SharedPreferences) : TokenStorage {
    override var token: String?
        get() = sharedPreferences.getString(KEY_TOKEN, null)
        set(value) {
            sharedPreferences.edit {
                if (value == null) {
                    remove(KEY_TOKEN)
                } else {
                    putString(KEY_TOKEN, value)
                }
            }
        }

    companion object {
        const val KEY_TOKEN = "token"
    }
}