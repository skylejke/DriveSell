package ru.point.common.storage

import kotlinx.coroutines.flow.Flow

interface TokenStorage {
    val tokenFlow: Flow<String?>

    suspend fun setToken(token: String?)

    suspend fun getUserId(): String
}
