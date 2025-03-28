package ru.point.search.domain.model

import kotlinx.serialization.Serializable
import ru.point.cars.model.ModelDto

@Serializable
data class ModelVo(val name: String)

val ModelDto.asModelVo
    get() = ModelVo(name = name)