package ru.point.common.storage

interface TokenStorage {
    var token: String?
    fun getUserId(): String
}