package ru.point.core.di

import android.content.Context
import retrofit2.Retrofit

interface FeatureDeps {
    val retrofit: Retrofit
    val context: Context
}