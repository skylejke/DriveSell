package ru.point.common.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.auth0.jwt.JWT

const val GUEST_ID = "GUEST_ID"

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

    override fun getUserId() =
        token?.let { JWT.decode(it).subject } ?: GUEST_ID

    companion object {
        const val KEY_TOKEN = "token"
    }
}