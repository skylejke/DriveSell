package ru.point.car_details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.filterNotNull
import ru.point.car_details.R
import ru.point.car_details.databinding.FragmentCarDetailsBinding
import ru.point.car_details.di.CarDetailsComponentHolderVM
import ru.point.car_details.di.carDetailsComponent
import ru.point.cars.model.AdVo
import ru.point.common.ext.bottomBar
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ui.ComponentHolderFragment
import javax.inject.Inject

internal class CarDetailsFragment : ComponentHolderFragment<FragmentCarDetailsBinding>() {

    @Inject
    lateinit var carDetailsViewModelFactory: CarDetailsViewModelFactory

    private val carDetailsViewModel by viewModels<CarDetailsViewModel> { carDetailsViewModelFactory }

    private var _carPhotoAdapter: CarPhotoAdapter? = null
    private val carPhotoAdapter get() = requireNotNull(_carPhotoAdapter)

    private val args: CarDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHolder<CarDetailsComponentHolderVM>()
        carDetailsComponent.inject(this)
        carDetailsViewModel.getCarAdById(args.adId)
        _carPhotoAdapter = CarPhotoAdapter()
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCarDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomBar.hide()

        binding.carPhotos.apply {
            adapter = carPhotoAdapter
        }

        repeatOnLifecycleScope {
            carDetailsViewModel.carDetails.filterNotNull().collect {
                setCarSpecs(it)
            }
        }

        binding.carDetailsToolBar.backIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }

    private fun setCarSpecs(adVo: AdVo) {
        carPhotoAdapter.submitList(adVo.photos)
        binding.carDetailsSpecs.creationDate.text = adVo.creationDate
        binding.carDetailsSpecs.price.text =
            resources.getString(R.string.car_details_car_price, adVo.car.price).replace(",", " ")
        binding.carDetailsSpecs.brandModelYear.text = resources.getString(
            R.string.car_details_brand_model_year,
            adVo.car.brand,
            adVo.car.model,
            adVo.car.year
        )
        binding.carDetailsSpecs.yearTv.text =
            resources.getString(R.string.year_of_release, adVo.car.year.toString())
        binding.carDetailsSpecs.mileageTv.text =
            resources.getString(R.string.car_details_mileage, adVo.car.mileage.toString())
        binding.carDetailsSpecs.bodyTypeTv.text =
            resources.getString(R.string.car_details_body_type, adVo.car.bodyType)
        binding.carDetailsSpecs.colorTv.text =
            resources.getString(R.string.car_details_color, adVo.car.color)
        binding.carDetailsSpecs.engineTv.text = resources.getString(
            R.string.car_details_engine,
            adVo.car.engineCapacity.toString(),
            adVo.car.enginePower.toString(),
            adVo.car.fuelType
        )
        binding.carDetailsSpecs.transmissionTv.text =
            resources.getString(R.string.car_details_transmission, adVo.car.transmission)
        binding.carDetailsSpecs.drivetrainTv.text =
            resources.getString(R.string.car_details_drivetrain, adVo.car.drivetrain)
        binding.carDetailsSpecs.wheelTv.text =
            resources.getString(R.string.car_details_wheel, adVo.car.wheel)
        binding.carDetailsSpecs.conditionTv.text =
            resources.getString(R.string.car_details_condition, adVo.car.condition)
        binding.carDetailsSpecs.ownersTv.text =
            resources.getString(R.string.car_details_owners, adVo.car.owners.toString())
        binding.carDetailsSpecs.ownershipPeriodTv.text =
            resources.getString(R.string.car_details_ownership_period, adVo.car.ownershipPeriod)
        binding.carDetailsSpecs.vinTv.text =
            resources.getString(R.string.car_details_vin, adVo.car.vin)
        binding.carDetailsSpecs.sellerSCommentTvValue.text = adVo.car.description
    }

    override fun onDestroy() {
        super.onDestroy()
        _carPhotoAdapter = null
    }
}