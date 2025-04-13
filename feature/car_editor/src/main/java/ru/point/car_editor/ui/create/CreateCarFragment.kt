package ru.point.car_editor.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
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

    private var _brandsAdapter: ArrayAdapter<String>? = null
    private val brandsAdapter get() = requireNotNull(_brandsAdapter)

    private var _modelsAdapter: ArrayAdapter<String>? = null
    private val modelsAdapter get() = requireNotNull(_modelsAdapter)

    private var _yearsAdapter: ArrayAdapter<String>? = null
    private val yearsAdapter get() = requireNotNull(_yearsAdapter)

    private var _fuelTypeAdapter: ArrayAdapter<String>? = null
    private val fuelTypeAdapter get() = requireNotNull(_fuelTypeAdapter)

    private var _bodyTypeAdapter: ArrayAdapter<String>? = null
    private val bodyTypeAdapter get() = requireNotNull(_bodyTypeAdapter)

    private var _colorAdapter: ArrayAdapter<String>? = null
    private val colorAdapter get() = requireNotNull(_colorAdapter)

    private var _drivetrainAdapter: ArrayAdapter<String>? = null
    private val drivetrainAdapter get() = requireNotNull(_drivetrainAdapter)

    private var _wheelAdapter: ArrayAdapter<String>? = null
    private val wheelAdapter get() = requireNotNull(_wheelAdapter)

    private var _conditionAdapter: ArrayAdapter<String>? = null
    private val conditionAdapter get() = requireNotNull(_conditionAdapter)

    private var _transmissionAdapter: ArrayAdapter<String>? = null
    private val transmissionAdapter get() = requireNotNull(_transmissionAdapter)

    private var _numberOfYearsAdapter: ArrayAdapter<String>? = null
    private val numberOfYearsAdapter get() = requireNotNull(_numberOfYearsAdapter)

    private var _numberOfMonthsAdapter: ArrayAdapter<String>? = null
    private val numberOfMonthsAdapter get() = requireNotNull(_numberOfMonthsAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHolder<CarEditorComponentHolderVM>()
        carEditorComponent.inject(this)
        initializeAdapters()
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCreateCarBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnLifecycleScope {
            createCarViewModel.isGuest.collect { isGuest ->
                if (isGuest == true) {
                    binding.guestContainer.root.isVisible = true
                    binding.mainContainer.isVisible = false
                } else {
                    binding.guestContainer.root.isVisible = false
                    binding.mainContainer.isVisible = true
                }
            }
        }

        setAdapters()

        observeErrors()

        repeatOnLifecycleScope {
            createCarViewModel.brands.collect { brands ->
                brandsAdapter.clear()
                brandsAdapter.addAll(brands.map { it.name })
                brandsAdapter.notifyDataSetChanged()
            }
        }

        repeatOnLifecycleScope {
            createCarViewModel.models.collect { models ->
                modelsAdapter.clear()
                modelsAdapter.addAll(models.map { it.name })
                modelsAdapter.notifyDataSetChanged()
            }
        }

        repeatOnLifecycleScope {
            createCarViewModel.addCarEvent.collect {
                navigator.fromAddCarFragmentToHomeFragment()
                Snackbar.make(binding.root, "Successfully created new car ad", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.brandEt.setOnItemClickListener { parent, view, position, id ->
            binding.modelTil.isVisible = true
            binding.modelEt.setText("", false)
            createCarViewModel.getModels(parent.getItemAtPosition(position) as String)
        }

        binding.createAdButton.setOnClickListener {
            val ownershipPeriod = run {
                val yearsText = binding.ownershipPeriodYearsEt.text.toString()
                val monthsText = binding.ownershipPeriodMonthsEt.text.toString()
                if (yearsText.startsWith("0")) monthsText else "$yearsText $monthsText"
            }

            createCarViewModel.createNewAd(
                car = with(binding) {
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
    }

    private fun initializeAdapters() {

        _carEditorPhotosAdapter =
            CarEditorPhotosAdapter(
                onAddPhotoClick = {
                    pickMultipleMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageAndVideo))
                },
                onDeletePhotoClick = { photo ->
                    carEditorPhotosAdapter.submitList(
                        carEditorPhotosAdapter.currentList.filter { it != photo })
                }
            )

        _brandsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item
        )

        _modelsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item
        )

        _yearsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.years)
        )

        _fuelTypeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.fuel_types)
        )

        _bodyTypeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.body_types)
        )

        _colorAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.colors)
        )

        _drivetrainAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.drivetrains)
        )

        _wheelAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.wheels)
        )

        _conditionAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.conditions)
        )

        _transmissionAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.transmissions)
        )

        _numberOfYearsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.number_of_years)
        )

        _numberOfMonthsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_item,
            resources.getStringArray(R.array.number_of_months)
        )
    }

    private fun setAdapters() = with(binding) {
        brandEt.setAdapter(brandsAdapter)
        modelEt.setAdapter(modelsAdapter)
        yearEt.setAdapter(yearsAdapter)
        fuelTypeEt.setAdapter(fuelTypeAdapter)
        bodyTypeEt.setAdapter(bodyTypeAdapter)
        colorEt.setAdapter(colorAdapter)
        transmissionEt.setAdapter(transmissionAdapter)
        drivetrainEt.setAdapter(drivetrainAdapter)
        wheelEt.setAdapter(wheelAdapter)
        conditionEt.setAdapter(conditionAdapter)
        ownershipPeriodYearsEt.setAdapter(numberOfYearsAdapter)
        ownershipPeriodMonthsEt.setAdapter(numberOfMonthsAdapter)
        photosRv.adapter = carEditorPhotosAdapter
        carEditorPhotosAdapter.submitList(listOf(CarEditorPhotosAdapterItem.ButtonEditor))
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

    private fun observeBrandError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.brandError.collect {
                binding.brandTil.error = it
            }
        }
        brandEt.clearErrorOnTextChanged(brandTil)
    }

    private fun observeModelError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.modelError.collect {
                modelTil.error = it
            }
        }
        modelEt.clearErrorOnTextChanged(modelTil)
    }

    private fun observeYearError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.yearError.collect {
                yearTil.error = it
            }
        }
        yearEt.clearErrorOnTextChanged(yearTil)
    }

    private fun observeVinError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.vinError.collect {
                vinTil.error = it
            }
        }
        vinEt.clearErrorOnTextChanged(vinTil)
    }

    private fun observePriceError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.priceError.collect {
                priceTil.error = it
            }
        }
        priceEt.clearErrorOnTextChanged(priceTil)
    }

    private fun observeMileageError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.mileageError.collect {
                mileageTil.error = it
            }
        }
        mileageEt.clearErrorOnTextChanged(mileageTil)
    }

    private fun observeEnginePowerError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.enginePowerError.collect {
                enginePowerTil.error = it
            }
        }
        enginePowerEt.clearErrorOnTextChanged(enginePowerTil)
    }

    private fun observeEngineCapacityError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.engineCapacityError.collect {
                engineCapacityTil.error = it
            }
        }
        engineCapacityEt.clearErrorOnTextChanged(engineCapacityTil)
    }

    private fun observeFuelTypeError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.fuelTypeError.collect {
                fuelTypeTil.error = it
            }
        }
        fuelTypeEt.clearErrorOnTextChanged(fuelTypeTil)
    }

    private fun observeBodyTypeError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.bodyTypeError.collect {
                bodyTypeTil.error = it
            }
        }
        bodyTypeEt.clearErrorOnTextChanged(bodyTypeTil)
    }

    private fun observeColorError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.colorError.collect {
                colorTil.error = it
            }
        }
        colorEt.clearErrorOnTextChanged(colorTil)
    }

    private fun observeTransmissionError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.transmissionError.collect {
                transmissionTil.error = it
            }
        }
        transmissionEt.clearErrorOnTextChanged(transmissionTil)
    }

    private fun observeDrivetrainError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.drivetrainError.collect {
                drivetrainTil.error = it
            }
        }
        drivetrainEt.clearErrorOnTextChanged(drivetrainTil)
    }

    private fun observeWheelError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.wheelError.collect {
                wheelTil.error = it
            }
        }
        wheelEt.clearErrorOnTextChanged(wheelTil)
    }

    private fun observeConditionError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.conditionError.collect {
                conditionTil.error = it
            }
        }
        conditionEt.clearErrorOnTextChanged(conditionTil)
    }

    private fun observeOwnersError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.ownersError.collect {
                numberOfOwnersTil.error = it
            }
        }
        numberOfOwnersEt.clearErrorOnTextChanged(numberOfOwnersTil)
    }

    private fun observeOwnershipPeriodError() = with(binding) {
        repeatOnLifecycleScope {
            createCarViewModel.ownershipPeriodError.collect {
                ownershipPeriodYearsTil.error = it
                ownershipPeriodMonthsTil.error = it
            }
        }
        ownershipPeriodYearsEt.clearErrorOnTextChanged(ownershipPeriodYearsTil)
        ownershipPeriodMonthsEt.clearErrorOnTextChanged(ownershipPeriodMonthsTil)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearAdapters()
    }

    private fun clearAdapters() {
        _brandsAdapter = null
        _modelsAdapter = null
        _fuelTypeAdapter = null
        _bodyTypeAdapter = null
        _colorAdapter = null
        _drivetrainAdapter = null
        _wheelAdapter = null
        _conditionAdapter = null
        _transmissionAdapter = null
        _numberOfYearsAdapter = null
        _numberOfMonthsAdapter = null
    }
}