package ru.point.car_editor.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import ru.point.car_editor.R
import ru.point.car_editor.databinding.FragmentCreateCarBinding
import ru.point.car_editor.di.CarEditorComponentHolderVM
import ru.point.car_editor.di.carEditorComponent
import ru.point.cars.model.CreateCarRequest
import ru.point.cars.ui.CarEditorPhotosAdapter
import ru.point.cars.ui.CarEditorPhotosAdapterItem
import ru.point.common.ext.NumberErrorConsts
import ru.point.common.ext.clearErrorOnTextChanged
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ext.showSnackbar
import ru.point.common.model.Status
import ru.point.common.ui.ComponentHolderFragment
import javax.inject.Inject

internal class CreateCarFragment : ComponentHolderFragment<FragmentCreateCarBinding>() {

    @Inject
    lateinit var createCarViewModelFactory: CreateCarViewModelFactory

    private val createCarViewModel by viewModels<CreateCarViewModel> { createCarViewModelFactory }

    val pickMultipleMedia =
        registerForActivityResult(PickMultipleVisualMedia(30)) { uris ->
            if (uris.isNotEmpty()) {
                val currentItems = carEditorPhotosAdapter.currentList.toMutableList()
                currentItems.addAll(uris.map { CarEditorPhotosAdapterItem.Photo(it) })
                carEditorPhotosAdapter.submitList(currentItems)
            }
        }

    private var _carEditorPhotosAdapter: CarEditorPhotosAdapter? = null
    private val carEditorPhotosAdapter get() = requireNotNull(_carEditorPhotosAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHolder<CarEditorComponentHolderVM>()
        carEditorComponent.inject(this)
        _carEditorPhotosAdapter = CarEditorPhotosAdapter(
            onAddPhotoClick = {
                pickMultipleMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageAndVideo))
            },
            onDeletePhotoClick = { photo ->
                carEditorPhotosAdapter.submitList(
                    carEditorPhotosAdapter.currentList.filter { it != photo }
                )
            }
        )
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCreateCarBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clearFields()

        with(binding.carEditorFields) {
            yearEt.setSimpleItems(resources.getStringArray(R.array.years))
            fuelTypeEt.setSimpleItems(resources.getStringArray(R.array.fuel_types))
            bodyTypeEt.setSimpleItems(resources.getStringArray(R.array.body_types))
            colorEt.setSimpleItems(resources.getStringArray(R.array.colors))
            transmissionEt.setSimpleItems(resources.getStringArray(R.array.transmissions))
            drivetrainEt.setSimpleItems(resources.getStringArray(R.array.drivetrains))
            wheelEt.setSimpleItems(resources.getStringArray(R.array.wheels))
            conditionEt.setSimpleItems(resources.getStringArray(R.array.conditions))
            ownershipPeriodYearsEt.setSimpleItems(resources.getStringArray(R.array.number_of_years))
            ownershipPeriodMonthsEt.setSimpleItems(resources.getStringArray(R.array.number_of_months))
        }

        binding.photosRv.adapter = carEditorPhotosAdapter

        carEditorPhotosAdapter.submitList(listOf(CarEditorPhotosAdapterItem.ButtonEditor))

        createCarViewModel.getBrands()

        observeErrors()

        repeatOnLifecycleScope { createCarViewModel.status.collect { updatePlaceholder(it) } }

        repeatOnLifecycleScope {
            createCarViewModel.brands.collect { brands ->
                binding.carEditorFields.brandEt.setSimpleItems(
                    brands.map { it.name }.toTypedArray()
                )
            }
        }

        repeatOnLifecycleScope {
            createCarViewModel.models.collect { models ->
                binding.carEditorFields.modelEt.setSimpleItems(
                    models.map { it.name }.toTypedArray()
                )
                binding.carEditorFields.modelTil.isVisible = models.isNotEmpty()
            }
        }

        binding.carEditorFields.brandEt.setOnItemClickListener { parent, _, position, _ ->
            binding.carEditorFields.modelTil.isVisible = true
            binding.carEditorFields.modelEt.setText("", false)
            createCarViewModel.getModels(parent.getItemAtPosition(position) as String)
        }

        binding.createAdButton.setOnClickListener {
            val ownershipPeriod = run {
                val yearsText = binding.carEditorFields.ownershipPeriodYearsEt.text.toString()
                val monthsText = binding.carEditorFields.ownershipPeriodMonthsEt.text.toString()
                if (yearsText.startsWith("0")) monthsText else "$yearsText $monthsText"
            }

            createCarViewModel.createNewAd(
                car = with(binding.carEditorFields) {
                    CreateCarRequest(
                        brand = brandEt.text.toString(),
                        model = modelEt.text.toString(),
                        year = yearEt.text.toString().toShortOrNull()
                            ?: NumberErrorConsts.ERROR_SHORT_VALUE,
                        price = priceEt.text.toString().toIntOrNull()
                            ?: NumberErrorConsts.ERROR_INT_VALUE,
                        enginePower = enginePowerEt.text.toString().toShortOrNull()
                            ?: NumberErrorConsts.ERROR_SHORT_VALUE,
                        engineCapacity = engineCapacityEt.text.toString().toDoubleOrNull()
                            ?: NumberErrorConsts.ERROR_DOUBLE_VALUE,
                        fuelType = fuelTypeEt.text.toString(),
                        mileage = mileageEt.text.toString().toIntOrNull()
                            ?: NumberErrorConsts.ERROR_INT_VALUE,
                        bodyType = bodyTypeEt.text.toString(),
                        color = colorEt.text.toString(),
                        transmission = transmissionEt.text.toString(),
                        drivetrain = drivetrainEt.text.toString(),
                        wheel = wheelEt.text.toString(),
                        condition = conditionEt.text.toString(),
                        owners = numberOfOwnersEt.text.toString().toByteOrNull()
                            ?: NumberErrorConsts.ERROR_BYTE_VALUE,
                        vin = vinEt.text.toString(),
                        ownershipPeriod = ownershipPeriod,
                        description = descriptionEt.text.toString()
                    )
                },
                photos = carEditorPhotosAdapter.currentList
                    .filterIsInstance<CarEditorPhotosAdapterItem.Photo>()
                    .map { it.uri }
            )
        }

        binding.noConnectionPlaceholder.tryAgainTv.setOnClickListener {
            clearFields()
            createCarViewModel.getBrands()
        }
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                shimmerLayout.isVisible = true
                shimmerLayout.startShimmer()
                photosTv.isVisible = false
                createAdButton.isVisible = false
                photosRv.isVisible = false
                carEditorFields.root.isVisible = false
                noConnectionPlaceholder.root.isVisible = false
            }

            is Status.Success -> {
                shimmerLayout.isVisible = false
                shimmerLayout.stopShimmer()
                photosTv.isVisible = true
                createAdButton.isVisible = true
                photosRv.isVisible = true
                carEditorFields.root.isVisible = true
                noConnectionPlaceholder.root.isVisible = false
            }

            is Status.Error -> {
                shimmerLayout.isVisible = false
                shimmerLayout.stopShimmer()
                photosTv.isVisible = false
                createAdButton.isVisible = false
                photosRv.isVisible = false
                carEditorFields.root.isVisible = false
                noConnectionPlaceholder.root.isVisible = true
            }
        }
    }

    private fun observeErrors() {
        observerPhotosError()
        observeBrandError()
        observeModelError()
        observeYearError()
        observeVinError()
        observePriceError()
        observeMileageError()
        observeEnginePowerError()
        observeEngineCapacityError()
        observeFuelTypeError()
        observeBodyTypeError()
        observeColorError()
        observeTransmissionError()
        observeDrivetrainError()
        observeWheelError()
        observeConditionError()
        observeOwnersError()
        observeOwnershipPeriodError()
    }

    private fun observerPhotosError() {
        repeatOnLifecycleScope {
            createCarViewModel.photoError.collect { error ->
                error?.let {
                    showSnackbar(binding.root, it)
                }
            }
        }
    }

    private fun observeBrandError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.brandError.collect {
                brandTil.error = it
            }
        }
        brandEt.clearErrorOnTextChanged(brandTil)
    }

    private fun observeModelError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.modelError.collect {
                modelTil.error = it
            }
        }
        modelEt.clearErrorOnTextChanged(modelTil)
    }

    private fun observeYearError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.yearError.collect {
                yearTil.error = it
            }
        }
        yearEt.clearErrorOnTextChanged(yearTil)
    }

    private fun observeVinError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.vinError.collect {
                vinTil.error = it
            }
        }
        vinEt.clearErrorOnTextChanged(vinTil)
    }

    private fun observePriceError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.priceError.collect {
                priceTil.error = it
            }
        }
        priceEt.clearErrorOnTextChanged(priceTil)
    }

    private fun observeMileageError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.mileageError.collect {
                mileageTil.error = it
            }
        }
        mileageEt.clearErrorOnTextChanged(mileageTil)
    }

    private fun observeEnginePowerError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.enginePowerError.collect {
                enginePowerTil.error = it
            }
        }
        enginePowerEt.clearErrorOnTextChanged(enginePowerTil)
    }

    private fun observeEngineCapacityError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.engineCapacityError.collect {
                engineCapacityTil.error = it
            }
        }
        engineCapacityEt.clearErrorOnTextChanged(engineCapacityTil)
    }

    private fun observeFuelTypeError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.fuelTypeError.collect {
                fuelTypeTil.error = it
            }
        }
        fuelTypeEt.clearErrorOnTextChanged(fuelTypeTil)
    }

    private fun observeBodyTypeError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.bodyTypeError.collect {
                bodyTypeTil.error = it
            }
        }
        bodyTypeEt.clearErrorOnTextChanged(bodyTypeTil)
    }

    private fun observeColorError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.colorError.collect {
                colorTil.error = it
            }
        }
        colorEt.clearErrorOnTextChanged(colorTil)
    }

    private fun observeTransmissionError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.transmissionError.collect {
                transmissionTil.error = it
            }
        }
        transmissionEt.clearErrorOnTextChanged(transmissionTil)
    }

    private fun observeDrivetrainError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.drivetrainError.collect {
                drivetrainTil.error = it
            }
        }
        drivetrainEt.clearErrorOnTextChanged(drivetrainTil)
    }

    private fun observeWheelError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.wheelError.collect {
                wheelTil.error = it
            }
        }
        wheelEt.clearErrorOnTextChanged(wheelTil)
    }

    private fun observeConditionError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.conditionError.collect {
                conditionTil.error = it
            }
        }
        conditionEt.clearErrorOnTextChanged(conditionTil)
    }

    private fun observeOwnersError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.ownersError.collect {
                numberOfOwnersTil.error = it
            }
        }
        numberOfOwnersEt.clearErrorOnTextChanged(numberOfOwnersTil)
    }

    private fun observeOwnershipPeriodError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.ownershipPeriodError.collect {
                ownershipPeriodYearsTil.error = it
                ownershipPeriodMonthsTil.error = it
            }
        }
        ownershipPeriodYearsEt.clearErrorOnTextChanged(ownershipPeriodYearsTil)
        ownershipPeriodMonthsEt.clearErrorOnTextChanged(ownershipPeriodMonthsTil)
    }

    private fun clearFields() {
        with(binding.carEditorFields) {
            brandEt.setText("", false)
            modelEt.setText("", false)
            mileageEt.text?.clear()
            yearEt.setText("", false)
            priceEt.text?.clear()
            enginePowerEt.text?.clear()
            engineCapacityEt.text?.clear()
            fuelTypeEt.setText("", false)
            bodyTypeEt.setText("", false)
            colorEt.setText("", false)
            transmissionEt.setText("", false)
            drivetrainEt.setText("", false)
            wheelEt.setText("", false)
            conditionEt.setText("", false)
            numberOfOwnersEt.text?.clear()
            vinEt.text?.clear()
            ownershipPeriodYearsEt.setText("", false)
            ownershipPeriodMonthsEt.setText("", false)
            descriptionEt.text?.clear()
            carEditorPhotosAdapter.submitList(listOf(CarEditorPhotosAdapterItem.ButtonEditor))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _carEditorPhotosAdapter = null
    }
}