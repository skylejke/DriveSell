package ru.point.home.domain

import ru.point.cars.model.asAdVo
import ru.point.cars.repository.CarsRepository

internal class GetCarsUseCase(private val carsRepository: CarsRepository) {
    suspend operator fun invoke(query: String = "") = carsRepository.getCars(query).map { adList ->
        adList.map { it.asAdVo }
    }
}