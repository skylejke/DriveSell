package ru.point.search.domain.model

import ru.point.cars.model.ModelDto

data class ModelVo(val name: String)

val ModelDto.asModelVo
    get() = ModelVo(name = name)