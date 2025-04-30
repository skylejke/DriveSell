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
import kotlinx.coroutines.flow.filterNotNull
import ru.point.car_editor.R
import ru.point.car_editor.databinding.FragmentCreateCarBinding
import ru.point.car_editor.di.DaggerCarEditorComponent
import ru.point.cars.model.CreateCarRequest
import ru.point.cars.ui.CarEditorPhotosAdapter
import ru.point.cars.ui.CarEditorPhotosAdapterItem
import ru.point.common.di.FeatureDepsProvider
import ru.point.common.ext.NumberErrorConsts
import ru.point.common.ext.clearErrorOnTextChanged
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ext.showSnackbar
import ru.point.common.model.EventState
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment
import javax.inject.Inject

private const val MAX_PHOTOS = 30

internal class CreateCarFragment : BaseFragment<FragmentCreateCarBinding>() {

    @Inject
    lateinit var factory: CreateCarViewModelFactory
    private val createCarViewModel: CreateCarViewModel by viewModels { factory }

    private var _carEditorPhotosAdapter: CarEditorPhotosAdapter? = null
    private val carEditorPhotosAdapter get() = requireNotNull(_carEditorPhotosAdapter)

    private val pickMedia = registerForActivityResult(PickMultipleVisualMedia(MAX_PHOTOS)) { uris ->
        if (uris.isNotEmpty()) {
            val list = carEditorPhotosAdapter.currentList.toMutableList().apply {
                addAll(uris.map { CarEditorPhotosAdapterItem.Photo(it) })
            }
            carEditorPhotosAdapter.submitList(list)
        }
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCreateCarBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerCarEditorComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .adId()
            .build()
            .inject(this)

        _carEditorPhotosAdapter = CarEditorPhotosAdapter(
            onAddPhotoClick = {
                pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageAndVideo))
            },
            onDeletePhotoClick = { item ->
                carEditorPhotosAdapter.submitList(carEditorPhotosAdapter.currentList - item)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clearFields()

        with(binding.carEditorFields) {
            carEditorYearEt.setSimpleItems(resources.getStringArray(R.array.years))
            carEditorBodyTypeEt.setSimpleItems(resources.getStringArray(R.array.fuel_types))
            carEditorBodyTypeEt.setSimpleItems(resources.getStringArray(R.array.body_types))
            carEditorColorEt.setSimpleItems(resources.getStringArray(R.array.colors))
            carEditorTransmissionEt.setSimpleItems(resources.getStringArray(R.array.transmissions))
            carEditorDrivetrainEt.setSimpleItems(resources.getStringArray(R.array.drivetrains))
            carEditorWheelEt.setSimpleItems(resources.getStringArray(R.array.wheels))
            carEditorConditionEt.setSimpleItems(resources.getStringArray(R.array.conditions))
            carEditorOwnershipPeriodYearsEt.setSimpleItems(resources.getStringArray(R.array.number_of_years))
            carEditorOwnershipPeriodMonthsEt.setSimpleItems(resources.getStringArray(R.array.number_of_months))
        }

        binding.createCarPhotosRv.adapter = carEditorPhotosAdapter
        carEditorPhotosAdapter.submitList(listOf(CarEditorPhotosAdapterItem.ButtonEditor))

        createCarViewModel.getBrands()

        observeErrors()

        repeatOnLifecycleScope {
            createCarViewModel.isGuest.filterNotNull().collect {
                binding.createCarMainContainer.isVisible = !it
                binding.createCarGuestContainer.root.isVisible = it
            }
        }

        repeatOnLifecycleScope { createCarViewModel.status.collect { updatePlaceholder(it) } }

        repeatOnLifecycleScope {
            createCarViewModel.brands.collect { brands ->
                binding.carEditorFields.carEditorBrandEt.setSimpleItems(
                    brands.map { it.name }.toTypedArray()
                )
            }
        }

        repeatOnLifecycleScope {
            createCarViewModel.models.collect { models ->
                binding.carEditorFields.carEditorModelEt.setSimpleItems(
                    models.map { it.name }.toTypedArray()
                )
                binding.carEditorFields.carEditorModelTil.isVisible = models.isNotEmpty()
            }
        }

        repeatOnLifecycleScope {
            createCarViewModel.addCarEvent.collect {
                when (it) {
                    is EventState.Success -> {
                        navigator.fromAddCarFragmentToHomeFragment()
                        clearFields()
                        showSnackbar(binding.root, getString(R.string.successfully_created_new_car_ad))
                    }

                    is EventState.Failure -> showSnackbar(
                        binding.root,
                        getString(R.string.something_went_wrong)
                    )
                }
            }
        }

        binding.carEditorFields.carEditorBrandEt.setOnItemClickListener { parent, _, position, _ ->
            binding.carEditorFields.carEditorModelTil.isVisible = true
            binding.carEditorFields.carEditorModelEt.setText("", false)
            createCarViewModel.getModels(parent.getItemAtPosition(position) as String)
        }

        binding.createCarBtn.setOnClickListener {
            val ownershipPeriod = run {
                val yearsText = binding.carEditorFields.carEditorOwnershipPeriodYearsEt.text.toString()
                val monthsText = binding.carEditorFields.carEditorOwnershipPeriodMonthsEt.text.toString()
                if (yearsText.startsWith("0")) monthsText else "$yearsText $monthsText"
            }

            createCarViewModel.createNewAd(
                car = with(binding.carEditorFields) {
                    CreateCarRequest(
                        brand = carEditorBrandEt.text.toString(),
                        model = carEditorModelEt.text.toString(),
                        year = carEditorYearEt.text.toString().toShortOrNull()
                            ?: NumberErrorConsts.ERROR_SHORT_VALUE,
                        price = carEditorPriceEt.text.toString().toIntOrNull()
                            ?: NumberErrorConsts.ERROR_INT_VALUE,
                        enginePower = carEditorEnginePowerEt.text.toString().toShortOrNull()
                            ?: NumberErrorConsts.ERROR_SHORT_VALUE,
                        engineCapacity = carEditorEngineCapacityEt.text.toString().toDoubleOrNull()
                            ?: NumberErrorConsts.ERROR_DOUBLE_VALUE,
                        fuelType = carEditorFuelTypeEt.text.toString(),
                        mileage = carEditorMileageEt.text.toString().toIntOrNull()
                            ?: NumberErrorConsts.ERROR_INT_VALUE,
                        bodyType = carEditorBodyTypeEt.text.toString(),
                        color = carEditorColorEt.text.toString(),
                        transmission = carEditorTransmissionEt.text.toString(),
                        drivetrain = carEditorDrivetrainEt.text.toString(),
                        wheel = carEditorWheelEt.text.toString(),
                        condition = carEditorConditionEt.text.toString(),
                        owners = carEditorNumberOfOwnersEt.text.toString().toByteOrNull()
                            ?: NumberErrorConsts.ERROR_BYTE_VALUE,
                        vin = carEditorVinEt.text.toString(),
                        ownershipPeriod = ownershipPeriod,
                        description = carEditorDescriptionEt.text.toString()
                    )
                },
                photos = carEditorPhotosAdapter.currentList
                    .filterIsInstance<CarEditorPhotosAdapterItem.Photo>()
                    .map { it.uri }
            )
        }

        binding.createCarNoConnectionPlaceholder.tryAgainTv.setOnClickListener {
            clearFields()
            createCarViewModel.getBrands()
        }
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                createCarShimmerLayout.isVisible = true
                createCarShimmerLayout.startShimmer()
                createCarPhotosTv.isVisible = false
                createCarPhotosRv.isVisible = false
                carEditorFields.root.isVisible = false
                createCarBtn.isVisible = false
                createCarNoConnectionPlaceholder.root.isVisible = false
            }

            is Status.Success -> {
                createCarShimmerLayout.isVisible = false
                createCarShimmerLayout.stopShimmer()
                createCarPhotosTv.isVisible = true
                createCarPhotosRv.isVisible = true
                carEditorFields.root.isVisible = true
                createCarBtn.isVisible = true
                createCarNoConnectionPlaceholder.root.isVisible = false
            }

            is Status.Error -> {
                createCarShimmerLayout.isVisible = false
                createCarShimmerLayout.stopShimmer()
                createCarPhotosTv.isVisible = false
                createCarPhotosRv.isVisible = false
                carEditorFields.root.isVisible = false
                createCarBtn.isVisible = false
                createCarNoConnectionPlaceholder.root.isVisible = true
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
                carEditorBrandTil.error = it
            }
        }
        carEditorBrandEt.clearErrorOnTextChanged(carEditorBrandTil)
    }

    private fun observeModelError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.modelError.collect {
                carEditorModelTil.error = it
            }
        }
        carEditorModelEt.clearErrorOnTextChanged(carEditorModelTil)
    }

    private fun observeYearError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.yearError.collect {
                carEditorYearTil.error = it
            }
        }
        carEditorYearEt.clearErrorOnTextChanged(carEditorYearTil)
    }

    private fun observeVinError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.vinError.collect {
                carEditorVinTil.error = it
            }
        }
        carEditorVinEt.clearErrorOnTextChanged(carEditorVinTil)
    }

    private fun observePriceError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.priceError.collect {
                carEditorPriceTil.error = it
            }
        }
        carEditorPriceEt.clearErrorOnTextChanged(carEditorPriceTil)
    }

    private fun observeMileageError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.mileageError.collect {
                carEditorMileageTil.error = it
            }
        }
        carEditorMileageEt.clearErrorOnTextChanged(carEditorMileageTil)
    }

    private fun observeEnginePowerError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.enginePowerError.collect {
                carEditorEnginePowerTil.error = it
            }
        }
        carEditorEnginePowerEt.clearErrorOnTextChanged(carEditorEnginePowerTil)
    }

    private fun observeEngineCapacityError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.engineCapacityError.collect {
                carEditorEngineCapacityTil.error = it
            }
        }
        carEditorEngineCapacityEt.clearErrorOnTextChanged(carEditorEngineCapacityTil)
    }

    private fun observeFuelTypeError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.fuelTypeError.collect {
                carEditorFuelTypeTil.error = it
            }
        }
        carEditorFuelTypeEt.clearErrorOnTextChanged(carEditorFuelTypeTil)
    }

    private fun observeBodyTypeError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.bodyTypeError.collect {
                carEditorBodyTypeTil.error = it
            }
        }
        carEditorBodyTypeEt.clearErrorOnTextChanged(carEditorBodyTypeTil)
    }

    private fun observeColorError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.colorError.collect {
                carEditorColorTil.error = it
            }
        }
        carEditorColorEt.clearErrorOnTextChanged(carEditorColorTil)
    }

    private fun observeTransmissionError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.transmissionError.collect {
                carEditorTransmissionTil.error = it
            }
        }
        carEditorTransmissionEt.clearErrorOnTextChanged(carEditorTransmissionTil)
    }

    private fun observeDrivetrainError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.drivetrainError.collect {
                carEditorDrivetrainTil.error = it
            }
        }
        carEditorDrivetrainEt.clearErrorOnTextChanged(carEditorDrivetrainTil)
    }

    private fun observeWheelError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.wheelError.collect {
                carEditorWheelTil.error = it
            }
        }
        carEditorWheelEt.clearErrorOnTextChanged(carEditorWheelTil)
    }

    private fun observeConditionError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.conditionError.collect {
                carEditorConditionTil.error = it
            }
        }
        carEditorConditionEt.clearErrorOnTextChanged(carEditorConditionTil)
    }

    private fun observeOwnersError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.ownersError.collect {
                carEditorNumberOfOwnersTil.error = it
            }
        }
        carEditorNumberOfOwnersEt.clearErrorOnTextChanged(carEditorNumberOfOwnersTil)
    }

    private fun observeOwnershipPeriodError() = with(binding.carEditorFields) {
        repeatOnLifecycleScope {
            createCarViewModel.ownershipPeriodError.collect {
                carEditorOwnershipPeriodYearsTil.error = it
                carEditorOwnershipPeriodMonthsTil.error = it
            }
        }
        carEditorOwnershipPeriodYearsEt.clearErrorOnTextChanged(carEditorOwnershipPeriodYearsTil)
        carEditorOwnershipPeriodMonthsEt.clearErrorOnTextChanged(carEditorOwnershipPeriodMonthsTil)
    }

    private fun clearFields() {
        with(binding.carEditorFields) {
            carEditorBrandEt.setText("", false)
            carEditorModelEt.setText("", false)
            carEditorMileageEt.text?.clear()
            carEditorYearEt.setText("", false)
            carEditorPriceEt.text?.clear()
            carEditorEnginePowerEt.text?.clear()
            carEditorEngineCapacityEt.text?.clear()
            carEditorFuelTypeEt.setText("", false)
            carEditorBodyTypeEt.setText("", false)
            carEditorColorEt.setText("", false)
            carEditorTransmissionEt.setText("", false)
            carEditorDrivetrainEt.setText("", false)
            carEditorWheelEt.setText("", false)
            carEditorConditionEt.setText("", false)
            carEditorNumberOfOwnersEt.text?.clear()
            carEditorVinEt.text?.clear()
            carEditorOwnershipPeriodYearsEt.setText("", false)
            carEditorOwnershipPeriodMonthsEt.setText("", false)
            carEditorDescriptionEt.text?.clear()
            carEditorPhotosAdapter.submitList(listOf(CarEditorPhotosAdapterItem.ButtonEditor))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _carEditorPhotosAdapter = null
    }
}
