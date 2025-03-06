package ru.point.auth.di

interface FeatureDepsProvider {
    val featureDeps: FeatureDeps
    companion object : FeatureDepsProvider by FeatureDepsStore
}