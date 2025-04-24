package ru.point.common.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.auth0.jwt.JWT
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TokenStorageImpl(private val context: Context) : TokenStorage {

    override val tokenFlow = context.userDataStore.data
        .map { prefs -> prefs[KEY_TOKEN] }

    override suspend fun setToken(token: String?) {
        context.userDataStore.edit { prefs ->
            if (token == null) {
                prefs -= KEY_TOKEN
            } else {
                prefs[KEY_TOKEN] = token
            }
        }
    }

    override suspend fun getUserId(): String =
        context.userDataStore.data
            .map { it[KEY_TOKEN] }
            .first()?.let { JWT.decode(it).subject } ?: GUEST_ID

    companion object {
        private val Context.userDataStore by preferencesDataStore(name = SHARED_PREFS_USER)

        private const val GUEST_ID = "GUEST_ID"

        private val KEY_TOKEN = stringPreferencesKey("TOKEN")

        private const val SHARED_PREFS_USER = "USER_PREFS"
    }
}