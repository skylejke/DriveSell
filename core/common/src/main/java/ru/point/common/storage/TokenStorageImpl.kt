package ru.point.common.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.auth0.jwt.JWT

const val DEFAULT_USER_ID = ""

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

    override fun getUserId() = JWT.decode(token).subject ?: DEFAULT_USER_ID

    companion object {
        const val KEY_TOKEN = "token"
    }
}