package ru.point.car_editor.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.filterNotNull
import ru.point.car_editor.R
import ru.point.car_editor.databinding.FragmentEditCarBinding
import ru.point.car_editor.di.DaggerCarEditorComponent
import ru.point.cars.BuildConfig
import ru.point.cars.model.CarVo
import ru.point.cars.model.EditCarRequest
import ru.point.cars.ui.CarEditorPhotosAdapter
import ru.point.cars.ui.CarEditorPhotosAdapterItem
import ru.point.common.di.FeatureDepsProvider
import ru.point.common.ext.NumberErrorConsts
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ext.showSnackbar
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment
import javax.inject.Inject

private const val MAX_PHOTOS = 30

internal class EditCarFragment : BaseFragment<FragmentEditCarBinding>() {

    @Inject
    lateinit var editCarViewModelFactory: EditCarViewModelFactory

    private val editCarViewModel by viewModels<EditCarViewModel> { editCarViewModelFactory }

    private var _carEditorPhotosAdapter: CarEditorPhotosAdapter? = null
    private val carEditorPhotosAdapter get() = requireNotNull(_carEditorPhotosAdapter)

    private val args: EditCarFragmentArgs by navArgs()

    private val pickMultipleMedia =
        registerForActivityResult(PickMultipleVisualMedia(MAX_PHOTOS)) { uris ->
            if (uris.isNotEmpty()) {
                val items = carEditorPhotosAdapter.currentList.toMutableList()
                items.addAll(uris.map { CarEditorPhotosAdapterItem.Photo(it) })
                carEditorPhotosAdapter.submitList(items)
            }
        }

    private val removedPhotoIds = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerCarEditorComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .adId(args.adId)
            .build()
            .inject(this)

        _carEditorPhotosAdapter = CarEditorPhotosAdapter(
            onAddPhotoClick = {
                pickMultipleMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageAndVideo))
            },
            onDeletePhotoClick = { photo ->
                photo.photoId?.let { removedPhotoIds.add(it) }
                carEditorPhotosAdapter.submitList(
                    carEditorPhotosAdapter.currentList.filter { it != photo }
                )
            }
        )
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentEditCarBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clearFields()

        with(binding.carEditorFields) {
            carEditorBrandEt.setSimpleItems(emptyArray())
            carEditorModelEt.setSimpleItems(emptyArray())
            carEditorYearEt.setSimpleItems(resources.getStringArray(R.array.years))
            carEditorFuelTypeEt.setSimpleItems(resources.getStringArray(R.array.fuel_types))
            carEditorBodyTypeEt.setSimpleItems(resources.getStringArray(R.array.body_types))
            carEditorColorEt.setSimpleItems(resources.getStringArray(R.array.colors))
            carEditorTransmissionEt.setSimpleItems(resources.getStringArray(R.array.transmissions))
            carEditorDrivetrainEt.setSimpleItems(resources.getStringArray(R.array.drivetrains))
            carEditorWheelEt.setSimpleItems(resources.getStringArray(R.array.wheels))
            carEditorConditionEt.setSimpleItems(resources.getStringArray(R.array.conditions))
            carEditorOwnershipPeriodYearsEt.setSimpleItems(resources.getStringArray(R.array.number_of_years))
            carEditorOwnershipPeriodMonthsEt.setSimpleItems(resources.getStringArray(R.array.number_of_months))
        }

        binding.editCarPhotosRv.adapter = carEditorPhotosAdapter
        carEditorPhotosAdapter.submitList(listOf(CarEditorPhotosAdapterItem.ButtonEditor))

        editCarViewModel.getBrands()

        repeatOnLifecycleScope { editCarViewModel.status.collect { updatePlaceholder(it) } }

        repeatOnLifecycleScope {
            editCarViewModel.brands.collect { brands ->
                binding.carEditorFields.carEditorBrandEt.setSimpleItems(
                    brands.map { it.name }.toTypedArray()
                )
            }
        }
        repeatOnLifecycleScope {
            editCarViewModel.models.collect { models ->
                binding.carEditorFields.carEditorModelEt.setSimpleItems(
                    models.map { it.name }.toTypedArray()
                )
                binding.carEditorFields.carEditorModelTil.isVisible = models.isNotEmpty()
            }
        }

        repeatOnLifecycleScope {
            editCarViewModel.carData.filterNotNull().collect { data ->
                populateFields(data.car)
                populatePhotos(data.photos)
                editCarViewModel.getModels(data.car.brand)
            }
        }

        repeatOnLifecycleScope {
            editCarViewModel.editCarEvent.collect {
                navigator.popBackStack()
                showSnackbar(binding.root, getString(R.string.successfully_updated_car_ad))
            }
        }

        binding.editCarToolBar.checkIcon.setOnClickListener {
            editCarViewModel.editCar(
                adId = args.adId,
                carUpdateRequest = binding.carEditorFields.run {
                    EditCarRequest(
                        brand = carEditorBrandEt.text.toString(),
                        model = carEditorModelEt.text.toString(),
                        year = carEditorYearEt.text.toString().toShortOrNull() ?: NumberErrorConsts.ERROR_SHORT_VALUE,
                        price = carEditorPriceEt.text.toString().toIntOrNull() ?: NumberErrorConsts.ERROR_INT_VALUE,
                        enginePower = carEditorEnginePowerEt.text.toString().toShortOrNull()
                            ?: NumberErrorConsts.ERROR_SHORT_VALUE,
                        engineCapacity = carEditorEngineCapacityEt.text.toString().toDoubleOrNull()
                            ?: NumberErrorConsts.ERROR_DOUBLE_VALUE,
                        fuelType = carEditorFuelTypeEt.text.toString(),
                        mileage = carEditorMileageEt.text.toString().toIntOrNull() ?: NumberErrorConsts.ERROR_INT_VALUE,
                        bodyType = carEditorBodyTypeEt.text.toString(),
                        color = carEditorColorEt.text.toString(),
                        transmission = carEditorTransmissionEt.text.toString(),
                        driveTrain = carEditorDrivetrainEt.text.toString(),
                        wheel = carEditorWheelEt.text.toString(),
                        condition = carEditorConditionEt.text.toString(),
                        owners = carEditorNumberOfOwnersEt.text.toString().toByteOrNull()
                            ?: NumberErrorConsts.ERROR_BYTE_VALUE,
                        vin = carEditorVinEt.text.toString(),
                        ownershipPeriod = binding.carEditorFields.run {
                            val years = carEditorOwnershipPeriodYearsEt.text.toString()
                            val months = carEditorOwnershipPeriodMonthsEt.text.toString()
                            if (years.startsWith("0")) months else "$years $months"
                        },
                        description = carEditorDescriptionEt.text.toString()
                    )
                },
                newPhotos = carEditorPhotosAdapter.currentList
                    .filterIsInstance<CarEditorPhotosAdapterItem.Photo>()
                    .filter { it.photoId == null }
                    .map { it.uri },
                removedPhotos = removedPhotoIds
            )
        }

        binding.editCarToolBar.backIcon.setOnClickListener { navigator.popBackStack() }

        binding.noConnectionPlaceholder.tryAgainTv.setOnClickListener {
            editCarViewModel.getCarData(args.adId)
            editCarViewModel.getBrands()
        }
    }

    private fun clearFields() {
        with(binding.carEditorFields) {
            carEditorBrandEt.setText("", false)
            carEditorModelEt.setText("", false)
            carEditorYearEt.setText("", false)
            carEditorVinEt.text?.clear()
            carEditorPriceEt.text?.clear()
            carEditorMileageEt.text?.clear()
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
            carEditorDescriptionEt.text?.clear()
            carEditorOwnershipPeriodYearsEt.setText("", false)
            carEditorOwnershipPeriodMonthsEt.setText("", false)
            carEditorPhotosAdapter.submitList(listOf(CarEditorPhotosAdapterItem.ButtonEditor))
        }
    }

    private fun populateFields(car: CarVo) {
        with(binding.carEditorFields) {
            carEditorBrandEt.setText(car.brand, false)
            carEditorModelEt.setText(car.model, false)
            carEditorYearEt.setText(car.year.toString(), false)
            carEditorVinEt.setText(car.vin)
            carEditorPriceEt.setText(car.price.toString())
            carEditorMileageEt.setText(car.mileage.toString())
            carEditorEnginePowerEt.setText(car.enginePower.toString())
            carEditorEngineCapacityEt.setText(car.engineCapacity.toString())
            carEditorFuelTypeEt.setText(car.fuelType, false)
            carEditorBodyTypeEt.setText(car.bodyType, false)
            carEditorColorEt.setText(car.color, false)
            carEditorTransmissionEt.setText(car.transmission, false)
            carEditorDrivetrainEt.setText(car.drivetrain, false)
            carEditorWheelEt.setText(car.wheel, false)
            carEditorConditionEt.setText(car.condition, false)
            carEditorNumberOfOwnersEt.setText(car.owners.toString())
            carEditorDescriptionEt.setText(car.description)
        }
    }

    private fun populatePhotos(photoIds: List<String>) {
        val items = carEditorPhotosAdapter.currentList.toMutableList()
        items.addAll(photoIds.map { id ->
            CarEditorPhotosAdapterItem.Photo(
                uri = "${BuildConfig.BASE_URL}/photos/$id".toUri(),
                photoId = id
            )
        })
        carEditorPhotosAdapter.submitList(items)
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                editCarShimmerLayout.isVisible = true
                editCarShimmerLayout.startShimmer()
                editCarToolBar.checkIcon.isVisible = false
                editCarPhotosTv.isVisible = false
                editCarPhotosRv.isVisible = false
                carEditorFields.root.isVisible = false
                noConnectionPlaceholder.root.isVisible = false
            }

            is Status.Success -> {
                editCarShimmerLayout.isVisible = false
                editCarShimmerLayout.stopShimmer()
                editCarToolBar.checkIcon.isVisible = true
                editCarPhotosTv.isVisible = true
                editCarPhotosRv.isVisible = true
                carEditorFields.root.isVisible = true
                noConnectionPlaceholder.root.isVisible = false
            }

            is Status.Error -> {
                editCarShimmerLayout.isVisible = false
                editCarShimmerLayout.stopShimmer()
                editCarToolBar.checkIcon.isVisible = false
                editCarPhotosTv.isVisible = false
                editCarPhotosRv.isVisible = false
                carEditorFields.root.isVisible = false
                noConnectionPlaceholder.root.isVisible = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _carEditorPhotosAdapter = null
    }
}
