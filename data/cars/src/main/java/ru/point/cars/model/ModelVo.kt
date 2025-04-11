package ru.point.cars.model

data class ModelVo(val name: String)

val ModelDto.asModelVo
    get() = ModelVo(name = name)