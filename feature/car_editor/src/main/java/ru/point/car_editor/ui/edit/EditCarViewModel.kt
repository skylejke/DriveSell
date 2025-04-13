package ru.point.car_editor.ui.edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.cars.model.AdVo
import ru.point.cars.model.BrandVo
import ru.point.cars.model.EditCarRequest
import ru.point.cars.model.ModelVo
import ru.point.cars.model.asAdVo
import ru.point.cars.model.asBrandVo
import ru.point.cars.model.asModelVo
import ru.point.cars.repository.CarsRepository

internal class EditCarViewModel(private val carsRepository: CarsRepository) : ViewModel() {

    private val _carData = MutableStateFlow<AdVo?>(null)
    val carData get() = _carData.asStateFlow()

    private val _brands = MutableStateFlow<List<BrandVo>>(emptyList())
    val brands get() = _brands.asStateFlow()

    private val _models = MutableStateFlow<List<ModelVo>>(emptyList())
    val models get() = _models.asStateFlow()

    private val _editCarEvent = MutableSharedFlow<Unit>()
    val editCarEvent get() = _editCarEvent.asSharedFlow()

    init {
        getBrands()
    }

    fun getBrands() {
        viewModelScope.launch {
            carsRepository.getBrands().onSuccess { brandList ->
                _brands.value = brandList.map { it.asBrandVo }
            }
        }
    }

    fun getModels(brandName: String) {
        viewModelScope.launch {
            carsRepository.getModelsByBrand(brandName).onSuccess { modelList ->
                _models.value = modelList.map { it.asModelVo }
            }
        }
    }

    fun getCarData(adId: String) {
        viewModelScope.launch {
            carsRepository.getCarAdById(adId).onSuccess {
                _carData.value = it.asAdVo
            }
        }
    }

    fun editCar(
        adId: String,
        carUpdateRequest: EditCarRequest,
        newPhotos: List<Uri>,
        removedPhotos: List<String>
    ) {
        viewModelScope.launch {
            carsRepository.updateAd(
                adId = adId,
                car = carUpdateRequest,
                newPhotos = newPhotos,
                removePhotoIds = removedPhotos
            ).fold(
                onSuccess = { _editCarEvent.emit(Unit) },
                onFailure = {  }
            )
        }
    }
}