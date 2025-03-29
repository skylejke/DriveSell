package ru.point.common.di

interface FeatureDepsProvider {
    val featureDeps: FeatureDeps
    companion object : FeatureDepsProvider by FeatureDepsStore
}