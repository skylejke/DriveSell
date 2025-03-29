package ru.point.search.domain.model

import ru.point.cars.model.BrandDto


data class BrandVo(val name: String)

val BrandDto.asBrandVo
    get() = BrandVo(name = name)