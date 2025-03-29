package ru.point.common.di

import kotlin.properties.Delegates.notNull

object FeatureDepsStore : FeatureDepsProvider {
    override var featureDeps: FeatureDeps by notNull()
}