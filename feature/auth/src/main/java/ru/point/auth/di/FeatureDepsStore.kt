package ru.point.auth.di

import kotlin.properties.Delegates.notNull

object FeatureDepsStore : FeatureDepsProvider {
    override var featureDeps: FeatureDeps by notNull()
}