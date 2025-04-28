package ru.point.car_editor.ui.create

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.car_editor.R
import ru.point.cars.model.BrandVo
import ru.point.cars.model.CreateCarRequest
import ru.point.cars.model.ModelVo
import ru.point.cars.model.asBrandVo
import ru.point.cars.model.asModelVo
import ru.point.cars.repository.CarsRepository
import ru.point.common.ext.NumberErrorConsts
import ru.point.common.ext.isValidCapacity
import ru.point.common.ext.isValidPower
import ru.point.common.ext.isValidVIN
import ru.point.common.model.EventState
import ru.point.common.model.Status
import ru.point.common.utils.ResourceProvider
import ru.point.user.repository.UserRepository

internal class CreateCarViewModel(
    private val carsRepository: CarsRepository,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _isGuest = MutableStateFlow<Boolean?>(null)
    val isGuest get() = _isGuest.asStateFlow()

    private val _brands = MutableStateFlow<List<BrandVo>>(emptyList())
    val brands get() = _brands.asStateFlow()

    private val _models = MutableStateFlow<List<ModelVo>>(emptyList())
    val models get() = _models.asStateFlow()

    private val _addCarEvent = MutableSharedFlow<EventState>()
    val addCarEvent get() = _addCarEvent.asSharedFlow()

    private val _vinError = MutableStateFlow<String?>(null)
    val vinError get() = _vinError.asStateFlow()

    private val _photosError = MutableStateFlow<String?>(null)
    val photoError get() = _photosError.asStateFlow()

    private val _brandError = MutableStateFlow<String?>(null)
    val brandError get() = _brandError.asStateFlow()

    private val _modelError = MutableStateFlow<String?>(null)
    val modelError get() = _modelError.asStateFlow()

    private val _yearError = MutableStateFlow<String?>(null)
    val yearError get() = _yearError.asStateFlow()

    private val _priceError = MutableStateFlow<String?>(null)
    val priceError get() = _priceError.asStateFlow()

    private val _enginePowerError = MutableStateFlow<String?>(null)
    val enginePowerError get() = _enginePowerError.asStateFlow()

    private val _engineCapacityError = MutableStateFlow<String?>(null)
    val engineCapacityError get() = _engineCapacityError.asStateFlow()

    private val _fuelTypeError = MutableStateFlow<String?>(null)
    val fuelTypeError get() = _fuelTypeError.asStateFlow()

    private val _mileageError = MutableStateFlow<String?>(null)
    val mileageError get() = _mileageError.asStateFlow()

    private val _bodyTypeError = MutableStateFlow<String?>(null)
    val bodyTypeError get() = _bodyTypeError.asStateFlow()

    private val _colorError = MutableStateFlow<String?>(null)
    val colorError get() = _colorError.asStateFlow()

    private val _transmissionError = MutableStateFlow<String?>(null)
    val transmissionError get() = _transmissionError.asStateFlow()

    private val _drivetrainError = MutableStateFlow<String?>(null)
    val drivetrainError get() = _drivetrainError.asStateFlow()

    private val _wheelError = MutableStateFlow<String?>(null)
    val wheelError get() = _wheelError.asStateFlow()

    private val _conditionError = MutableStateFlow<String?>(null)
    val conditionError get() = _conditionError.asStateFlow()

    private val _ownersError = MutableStateFlow<String?>(null)
    val ownersError get() = _ownersError.asStateFlow()

    private val _ownershipPeriodError = MutableStateFlow<String?>(null)
    val ownershipPeriodError get() = _ownershipPeriodError.asStateFlow()

    private val _status = MutableStateFlow<Status>(Status.Loading)
    val status get() = _status.asStateFlow()

    init {
        viewModelScope.launch {
            _isGuest.value = !userRepository.isAuthorized()
        }
    }

    fun getBrands() {
        viewModelScope.launch {
            carsRepository.getBrands()
                .onSuccess { brandList ->
                    _status.value = Status.Success
                    _brands.value = brandList.map { it.asBrandVo }
                }
                .onFailure {
                    _status.value = Status.Error
                }
        }
    }

    fun getModels(brandName: String) {
        viewModelScope.launch {
            carsRepository.getModelsByBrand(brandName)
                .onSuccess { modelList ->
                    _status.value = Status.Success
                    _models.value = modelList.map { it.asModelVo }
                }
                .onFailure {
                    _status.value = Status.Error
                }
        }
    }

    fun createNewAd(car: CreateCarRequest, photos: List<Uri>) {
        if (!validateCarFields(car, photos)) return
        viewModelScope.launch {
            carsRepository.createNewAd(
                car = car,
                photos = photos
            ).fold(
                onSuccess = { _addCarEvent.emit(EventState.Success) },
                onFailure = { _addCarEvent.emit(EventState.Failure) }
            )
        }
    }

    private fun validateCarFields(car: CreateCarRequest, photos: List<Uri>): Boolean {

        _photosError.value = null
        _vinError.value = null
        _brandError.value = null
        _modelError.value = null
        _yearError.value = null
        _priceError.value = null
        _enginePowerError.value = null
        _engineCapacityError.value = null
        _fuelTypeError.value = null
        _mileageError.value = null
        _bodyTypeError.value = null
        _colorError.value = null
        _transmissionError.value = null
        _drivetrainError.value = null
        _wheelError.value = null
        _conditionError.value = null
        _ownersError.value = null
        _ownershipPeriodError.value = null

        var valid = true

        if (photos.isEmpty()) {
            _photosError.value = resourceProvider.getString(R.string.error_photos_required)
            valid = false
        }

        if (!car.vin.isValidVIN()) {
            _vinError.value = resourceProvider.getString(R.string.error_vin_invalid)
            valid = false
        } else if (car.vin.isBlank()) {
            _vinError.value = resourceProvider.getString(R.string.error_vin_required)
            valid = false
        }

        if (car.brand.isBlank()) {
            _brandError.value = resourceProvider.getString(R.string.error_brand_required)
            valid = false
        }

        if (car.model.isBlank()) {
            _modelError.value = resourceProvider.getString(R.string.error_model_required)
            valid = false
        }

        if (car.year == NumberErrorConsts.ERROR_SHORT_VALUE) {
            _yearError.value = resourceProvider.getString(R.string.error_year_required)
            valid = false
        }

        if (car.price == NumberErrorConsts.ERROR_INT_VALUE) {
            _priceError.value = resourceProvider.getString(R.string.error_price_required)
            valid = false
        }

        if (!car.enginePower.isValidPower()) {
            _enginePowerError.value = resourceProvider.getString(R.string.error_engine_power_invalid)
            valid = false
        } else if (car.enginePower == NumberErrorConsts.ERROR_SHORT_VALUE) {
            _enginePowerError.value = resourceProvider.getString(R.string.error_engine_power_required)
            valid = false
        }

        if (!car.engineCapacity.isValidCapacity()) {
            _engineCapacityError.value = resourceProvider.getString(R.string.error_engine_capacity_invalid)
            valid = false
        } else if (car.engineCapacity == NumberErrorConsts.ERROR_DOUBLE_VALUE) {
            _engineCapacityError.value = resourceProvider.getString(R.string.error_engine_capacity_required)
            valid = false
        }

        if (car.fuelType.isBlank()) {
            _fuelTypeError.value = resourceProvider.getString(R.string.error_fuel_type_required)
            valid = false
        }

        if (car.mileage == NumberErrorConsts.ERROR_INT_VALUE) {
            _mileageError.value = resourceProvider.getString(R.string.error_mileage_required)
            valid = false
        }

        if (car.bodyType.isBlank()) {
            _bodyTypeError.value = resourceProvider.getString(R.string.error_body_type_required)
            valid = false
        }

        if (car.color.isBlank()) {
            _colorError.value = resourceProvider.getString(R.string.error_color_required)
            valid = false
        }

        if (car.transmission.isBlank()) {
            _transmissionError.value = resourceProvider.getString(R.string.error_transmission_required)
            valid = false
        }

        if (car.drivetrain.isBlank()) {
            _drivetrainError.value = resourceProvider.getString(R.string.error_drivetrain_required)
            valid = false
        }

        if (car.wheel.isBlank()) {
            _wheelError.value = resourceProvider.getString(R.string.error_wheel_required)
            valid = false
        }

        if (car.condition.isBlank()) {
            _conditionError.value = resourceProvider.getString(R.string.error_condition_required)
            valid = false
        }

        if (car.owners == NumberErrorConsts.ERROR_BYTE_VALUE) {
            _ownersError.value = resourceProvider.getString(R.string.error_owners_required)
            valid = false
        }

        if (car.ownershipPeriod.isBlank()) {
            _ownershipPeriodError.value = resourceProvider.getString(R.string.error_ownership_period_required)
            valid = false
        }

        return valid
    }
}