package ru.point.common.di

import android.content.Context
import retrofit2.Retrofit

interface FeatureDeps {
    val retrofit: Retrofit
    val context: Context
}