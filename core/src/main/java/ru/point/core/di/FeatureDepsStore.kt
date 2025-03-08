package ru.point.core.di

import kotlin.properties.Delegates.notNull

object FeatureDepsStore : FeatureDepsProvider {
    override var featureDeps: FeatureDeps by notNull()
}