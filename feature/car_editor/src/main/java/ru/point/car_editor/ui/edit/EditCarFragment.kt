package ru.point.car_editor.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.filterNotNull
import ru.point.car_editor.R
import ru.point.car_editor.databinding.FragmentEditCarBinding
import ru.point.car_editor.di.CarEditorComponentHolderVM
import ru.point.car_editor.di.carEditorComponent
import ru.point.cars.BuildConfig
import ru.point.cars.model.CarVo
import ru.point.cars.model.EditCarRequest
import ru.point.cars.ui.CarEditorPhotosAdapter
import ru.point.cars.ui.CarEditorPhotosAdapterItem
import ru.point.common.ext.NumberErrorConsts
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ext.showSnackbar
import ru.point.common.ui.ComponentHolderFragment
import javax.inject.Inject

internal class EditCarFragment : ComponentHolderFragment<FragmentEditCarBinding>() {

    @Inject
    lateinit var editCarViewModelFactory: EditCarViewModelFactory

    private val editCarViewModel by viewModels<EditCarViewModel> { editCarViewModelFactory }

    private val args: EditCarFragmentArgs by navArgs()

    val pickMultipleMedia =
        registerForActivityResult(PickMultipleVisualMedia(30)) { uris ->
            if (uris.isNotEmpty()) {
                val currentItems = carEditorPhotosAdapter.currentList.toMutableList()
                currentItems.addAll(uris.map { CarEditorPhotosAdapterItem.Photo(it) })
                carEditorPhotosAdapter.submitList(currentItems)
            }
        }

    private val removedPhotoIds = mutableListOf<String>()

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
        editCarViewModel.getCarData(args.adId)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentEditCarBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapters()

        binding.brandEt.setOnItemClickListener { parent, view, position, id ->
            binding.modelEt.setText("", false)
            editCarViewModel.getModels(parent.getItemAtPosition(position) as String)
        }

        repeatOnLifecycleScope {
            editCarViewModel.brands.collect { brands ->
                brandsAdapter.clear()
                brandsAdapter.addAll(brands.map { it.name })
                brandsAdapter.notifyDataSetChanged()
            }
        }

        repeatOnLifecycleScope {
            editCarViewModel.models.collect { models ->
                modelsAdapter.clear()
                modelsAdapter.addAll(models.map { it.name })
                modelsAdapter.notifyDataSetChanged()
            }
        }

        repeatOnLifecycleScope {
            editCarViewModel.carData.filterNotNull().collect {
                setData(it.car)
                setPhotos(it.photos)
                editCarViewModel.getModels(it.car.brand)
            }
        }

        repeatOnLifecycleScope {
            editCarViewModel.editCarEvent.collect {
                navigator.popBackStack()
                showSnackbar(binding.root, "Successfully edit car")
            }
        }

        binding.editCarToolBar.checkIcon.setOnClickListener {
            with(binding) {
                val ownershipPeriod = run {
                    val yearsText = ownershipPeriodYearsEt.text.toString()
                    val monthsText = ownershipPeriodMonthsEt.text.toString()
                    if (yearsText.startsWith("0")) monthsText else "$yearsText $monthsText"
                }

                editCarViewModel.editCar(
                    adId = args.adId,
                    carUpdateRequest = EditCarRequest(
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
                        driveTrain = drivetrainEt.text.toString(),
                        wheel = wheelEt.text.toString(),
                        condition = conditionEt.text.toString(),
                        owners = numberOfOwnersEt.text.toString().toByteOrNull()
                            ?: NumberErrorConsts.ERROR_BYTE_VALUE,
                        vin = vinEt.text.toString(),
                        ownershipPeriod = ownershipPeriod,
                        description = descriptionEt.text.toString()
                    ),
                    newPhotos = carEditorPhotosAdapter.currentList
                        .filterIsInstance<CarEditorPhotosAdapterItem.Photo>()
                        .filter { it.photoId == null }
                        .map { it.uri },
                    removedPhotos = removedPhotoIds
                )
            }
        }

        binding.editCarToolBar.backIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }

    private fun initializeAdapters() {

        _carEditorPhotosAdapter =
            CarEditorPhotosAdapter(
                onAddPhotoClick = {
                    pickMultipleMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageAndVideo))
                },
                onDeletePhotoClick = { photo ->
                    val photoItem = photo
                    val id = photoItem.photoId
                    if (id != null) {
                        removedPhotoIds.add(id)
                    }
                    carEditorPhotosAdapter.submitList(
                        carEditorPhotosAdapter.currentList.filter { it != photo }
                    )
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

    private fun setData(carVo: CarVo) {
        with(binding) {
            brandEt.setText(carVo.brand, false)
            modelEt.setText(carVo.model, false)
            yearEt.setText(carVo.year.toString(), false)
            vinEt.setText(carVo.vin)
            priceEt.setText(carVo.price.toString())
            mileageEt.setText(carVo.mileage.toString())
            enginePowerEt.setText(carVo.enginePower.toString())
            engineCapacityEt.setText(carVo.engineCapacity.toString())
            fuelTypeEt.setText(carVo.fuelType, false)
            bodyTypeEt.setText(carVo.bodyType, false)
            colorEt.setText(carVo.color, false)
            transmissionEt.setText(carVo.transmission, false)
            drivetrainEt.setText(carVo.drivetrain, false)
            wheelEt.setText(carVo.wheel, false)
            conditionEt.setText(carVo.condition, false)
            numberOfOwnersEt.setText(carVo.owners.toString())
            descriptionEt.setText(carVo.description)
            val (yearsText, monthsText) = parseOwnershipPeriod(carVo)
            ownershipPeriodYearsEt.setText(yearsText, false)
            ownershipPeriodMonthsEt.setText(monthsText, false)
        }
    }

    private fun setPhotos(photos: List<String>) {
        val currentItems = carEditorPhotosAdapter.currentList.toMutableList()
        currentItems.addAll(
            photos.map { photoId ->
                CarEditorPhotosAdapterItem.Photo(
                    uri = "${BuildConfig.BASE_URL}/photos/$photoId".toUri(),
                    photoId = photoId
                )
            }
        )
        carEditorPhotosAdapter.submitList(currentItems)
    }

    private fun parseOwnershipPeriod(carVo: CarVo): Pair<String, String> {
        val ownershipPeriodRegex =
            """(?:(\d+)\s*years?)?\s*(?:(\d+)\s*months?)?""".toRegex(RegexOption.IGNORE_CASE)

        val matchResult = ownershipPeriodRegex.find(carVo.ownershipPeriod)
        val years = matchResult?.groups?.get(1)?.value?.toIntOrNull() ?: 0
        val months = matchResult?.groups?.get(2)?.value?.toIntOrNull() ?: 0

        val yearsText = if (years > 0) "$years ${if (years == 1) "year" else "years"}" else ""
        val monthsText = if (months > 0) "$months ${if (months == 1) "month" else "months"}" else ""
        return yearsText to monthsText
    }
}