package ru.point.core.di

interface FeatureDepsProvider {
    val featureDeps: FeatureDeps
    companion object : FeatureDepsProvider by FeatureDepsStore
}