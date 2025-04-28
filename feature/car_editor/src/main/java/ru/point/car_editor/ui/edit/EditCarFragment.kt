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
            brandEt.setSimpleItems(emptyArray())
            modelEt.setSimpleItems(emptyArray())
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

        editCarViewModel.getBrands()

        repeatOnLifecycleScope { editCarViewModel.status.collect { updatePlaceholder(it) } }

        repeatOnLifecycleScope {
            editCarViewModel.brands.collect { brands ->
                binding.carEditorFields.brandEt.setSimpleItems(
                    brands.map { it.name }.toTypedArray()
                )
            }
        }
        repeatOnLifecycleScope {
            editCarViewModel.models.collect { models ->
                binding.carEditorFields.modelEt.setSimpleItems(
                    models.map { it.name }.toTypedArray()
                )
                binding.carEditorFields.modelTil.isVisible = models.isNotEmpty()
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
                        brand = brandEt.text.toString(),
                        model = modelEt.text.toString(),
                        year = yearEt.text.toString().toShortOrNull() ?: NumberErrorConsts.ERROR_SHORT_VALUE,
                        price = priceEt.text.toString().toIntOrNull() ?: NumberErrorConsts.ERROR_INT_VALUE,
                        enginePower = enginePowerEt.text.toString().toShortOrNull()
                            ?: NumberErrorConsts.ERROR_SHORT_VALUE,
                        engineCapacity = engineCapacityEt.text.toString().toDoubleOrNull()
                            ?: NumberErrorConsts.ERROR_DOUBLE_VALUE,
                        fuelType = fuelTypeEt.text.toString(),
                        mileage = mileageEt.text.toString().toIntOrNull() ?: NumberErrorConsts.ERROR_INT_VALUE,
                        bodyType = bodyTypeEt.text.toString(),
                        color = colorEt.text.toString(),
                        transmission = transmissionEt.text.toString(),
                        driveTrain = drivetrainEt.text.toString(),
                        wheel = wheelEt.text.toString(),
                        condition = conditionEt.text.toString(),
                        owners = numberOfOwnersEt.text.toString().toByteOrNull() ?: NumberErrorConsts.ERROR_BYTE_VALUE,
                        vin = vinEt.text.toString(),
                        ownershipPeriod = binding.carEditorFields.run {
                            val years = ownershipPeriodYearsEt.text.toString()
                            val months = ownershipPeriodMonthsEt.text.toString()
                            if (years.startsWith("0")) months else "$years $months"
                        },
                        description = descriptionEt.text.toString()
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
            brandEt.setText("", false)
            modelEt.setText("", false)
            yearEt.setText("", false)
            vinEt.text?.clear()
            priceEt.text?.clear()
            mileageEt.text?.clear()
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
            descriptionEt.text?.clear()
            ownershipPeriodYearsEt.setText("", false)
            ownershipPeriodMonthsEt.setText("", false)
            carEditorPhotosAdapter.submitList(listOf(CarEditorPhotosAdapterItem.ButtonEditor))
        }
    }

    private fun populateFields(car: CarVo) {
        with(binding.carEditorFields) {
            brandEt.setText(car.brand, false)
            modelEt.setText(car.model, false)
            yearEt.setText(car.year.toString(), false)
            vinEt.setText(car.vin)
            priceEt.setText(car.price.toString())
            mileageEt.setText(car.mileage.toString())
            enginePowerEt.setText(car.enginePower.toString())
            engineCapacityEt.setText(car.engineCapacity.toString())
            fuelTypeEt.setText(car.fuelType, false)
            bodyTypeEt.setText(car.bodyType, false)
            colorEt.setText(car.color, false)
            transmissionEt.setText(car.transmission, false)
            drivetrainEt.setText(car.drivetrain, false)
            wheelEt.setText(car.wheel, false)
            conditionEt.setText(car.condition, false)
            numberOfOwnersEt.setText(car.owners.toString())
            descriptionEt.setText(car.description)
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
                shimmerLayout.isVisible = true
                shimmerLayout.startShimmer()
                editCarToolBar.checkIcon.isVisible = false
                photosTv.isVisible = false
                photosRv.isVisible = false
                carEditorFields.root.isVisible = false
                noConnectionPlaceholder.root.isVisible = false
            }

            is Status.Success -> {
                shimmerLayout.isVisible = false
                shimmerLayout.stopShimmer()
                editCarToolBar.checkIcon.isVisible = true
                photosTv.isVisible = true
                photosRv.isVisible = true
                carEditorFields.root.isVisible = true
                noConnectionPlaceholder.root.isVisible = false
            }

            is Status.Error -> {
                shimmerLayout.isVisible = false
                shimmerLayout.stopShimmer()
                editCarToolBar.checkIcon.isVisible = false
                photosTv.isVisible = false
                photosRv.isVisible = false
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
