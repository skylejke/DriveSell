package ru.point.car_details.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.filterNotNull
import ru.point.car_details.R
import ru.point.car_details.databinding.FragmentCarDetailsBinding
import ru.point.car_details.di.DaggerCarDetailsComponent
import ru.point.car_details.ui.stateholder.CarPhotoAdapter
import ru.point.cars.model.AdVo
import ru.point.common.di.FeatureDepsProvider
import ru.point.common.ext.bottomBar
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ext.showSnackbar
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment
import javax.inject.Inject

internal class CarDetailsFragment : BaseFragment<FragmentCarDetailsBinding>() {

    @Inject
    lateinit var carDetailsViewModelFactory: CarDetailsViewModelFactory

    private val carDetailsViewModel by viewModels<CarDetailsViewModel> { carDetailsViewModelFactory }

    private var _carPhotoAdapter: CarPhotoAdapter? = null
    private val carPhotoAdapter get() = requireNotNull(_carPhotoAdapter)

    private val args: CarDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerCarDetailsComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .adId(args.adId)
            .userId(args.userId)
            .build()
            .inject(this)

        _carPhotoAdapter = CarPhotoAdapter()
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCarDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomBar.hide()

        binding.photosVp.adapter = carPhotoAdapter

        repeatOnLifecycleScope {
            carDetailsViewModel.status.collect { status ->
                updatePlaceholder(status)
            }
        }

        repeatOnLifecycleScope {
            carDetailsViewModel.isUsersAd.filterNotNull().collect { isUsersAd ->
                if (isUsersAd) {
                    binding.carDetailsToolBar.secondaryIcon.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.edit_icon)
                    binding.carDetailsToolBar.primaryIcon.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.delete_icon)
                } else {
                    binding.carDetailsToolBar.secondaryIcon.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.compare_plus_icon)
                    binding.carDetailsToolBar.primaryIcon.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.fav_icon)
                }
            }
        }

        var currentFavouriteState: Boolean? = null

        binding.carDetailsToolBar.primaryIcon.setOnClickListener {
            if (carDetailsViewModel.isGuest.value!!) showSnackbar(binding.root, getString(R.string.pls_login_to_fav))
            if (carDetailsViewModel.isUsersAd.value!!) {
                DeleteAdDialog { deleteAd() }.show(
                    childFragmentManager,
                    DeleteAdDialog.TAG
                )
            } else {
                currentFavouriteState?.let { isFavourite ->
                    if (isFavourite) {
                        carDetailsViewModel.removeCarFromFavourites(args.adId)
                    } else {
                        carDetailsViewModel.addCarToFavourites(args.adId)
                    }
                }
            }
        }

        repeatOnLifecycleScope {
            carDetailsViewModel.isFavourite.filterNotNull().collect { isFavourite ->
                if (!carDetailsViewModel.isUsersAd.value!!) {
                    currentFavouriteState = isFavourite
                    binding.carDetailsToolBar.primaryIcon.background = ContextCompat.getDrawable(
                        requireContext(),
                        if (isFavourite) R.drawable.fav_filled_icon else R.drawable.fav_icon
                    )
                }
            }
        }

        var currentInComparisonsState: Boolean? = null

        binding.carDetailsToolBar.secondaryIcon.setOnClickListener {
            if (carDetailsViewModel.isGuest.value!!) showSnackbar(binding.root, getString(R.string.pls_login_to_comp))
            if (carDetailsViewModel.isUsersAd.value!!) {
                navigator.fromCarDetailsFragmentToEditCarFragment(args.adId, args.userId)
            } else {
                currentInComparisonsState?.let { isInComparisons ->
                    if (isInComparisons) {
                        carDetailsViewModel.removeCarFromComparisons(args.adId)
                    } else {
                        carDetailsViewModel.addCarToComparisons(args.adId)
                    }
                }
            }
        }

        repeatOnLifecycleScope {
            carDetailsViewModel.isInComparisons.filterNotNull().collect { isInComparisons ->
                if (!carDetailsViewModel.isUsersAd.value!!) {
                    currentInComparisonsState = isInComparisons
                    binding.carDetailsToolBar.secondaryIcon.background = ContextCompat.getDrawable(
                        requireContext(),
                        if (isInComparisons) R.drawable.compare_check_icon else R.drawable.compare_plus_icon
                    )
                }
            }
        }

        repeatOnLifecycleScope {
            carDetailsViewModel.carDetails.filterNotNull().collect {
                setCarSpecs(it)
            }
        }

        binding.noConnectionPlaceholder.tryAgainTv.setOnClickListener {
            getCarDetails()
        }

        binding.carDetailsToolBar.backIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }

    private fun getCarDetails() {
        with(carDetailsViewModel) {
            getCarAdById(args.adId)
            checkIsUsersAd(args.userId)
            checkIsFavourite(args.adId)
            checkIsInComparisons(args.adId)
        }
    }

    private fun updatePlaceholder(status: Status) = with(binding) {
        when (status) {
            is Status.Loading -> {
                shimmerPhoto.isVisible = true
                shimmerPhoto.startShimmer()

                shimmerSpecs.isVisible = true
                shimmerSpecs.startShimmer()

                photosVp.isVisible = false
                carDetailsSpecs.root.isVisible = false
                carDetailsToolBar.secondaryIcon.isVisible = false
                carDetailsToolBar.primaryIcon.isVisible = false
                noConnectionPlaceholder.root.isVisible = false
            }

            is Status.Success -> {
                shimmerPhoto.isVisible = false
                shimmerPhoto.stopShimmer()

                shimmerSpecs.isVisible = false
                shimmerSpecs.stopShimmer()

                photosVp.isVisible = true
                carDetailsSpecs.root.isVisible = true
                carDetailsToolBar.secondaryIcon.isVisible = true
                carDetailsToolBar.primaryIcon.isVisible = true
                noConnectionPlaceholder.root.isVisible = false
            }

            is Status.Error -> {
                shimmerPhoto.isVisible = false
                shimmerPhoto.stopShimmer()

                shimmerSpecs.isVisible = false
                shimmerSpecs.stopShimmer()

                photosVp.isVisible = false
                carDetailsSpecs.root.isVisible = false
                carDetailsToolBar.secondaryIcon.isVisible = false
                carDetailsToolBar.primaryIcon.isVisible = false
                noConnectionPlaceholder.root.isVisible = true
            }
        }
    }

    private fun setCarSpecs(adVo: AdVo) {
        carPhotoAdapter.submitList(adVo.photos)

        with(binding.carDetailsSpecs) {
            creationDateTv.text = adVo.creationDate

            carDetailsPriceTv.text = getString(
                R.string.car_details_car_price,
                adVo.car.price
            ).replace(",", " ")

            carDetailsBrandModelYear.text = getString(
                R.string.car_details_brand_model_year,
                adVo.car.brand,
                adVo.car.model,
                adVo.car.year
            )

            carDetailsYearTv.text = getString(
                R.string.year_of_release,
                adVo.car.year.toString()
            )

            carDetailsMileageTv.text = getString(
                R.string.car_details_mileage,
                adVo.car.mileage.toString()
            )

            carDetailsBodyTypeTv.text = getString(
                R.string.car_details_body_type,
                adVo.car.bodyType
            )

            carDetailsColorTv.text = getString(
                R.string.car_details_color,
                adVo.car.color
            )

            carDetailsEngineTv.text = getString(
                R.string.car_details_engine,
                adVo.car.engineCapacity.toString(),
                adVo.car.enginePower.toString(),
                adVo.car.fuelType
            )

            carDetailsTransmissionTv.text = getString(
                R.string.car_details_transmission,
                adVo.car.transmission
            )

            carDetailsDrivetrainTv.text = getString(
                R.string.car_details_drivetrain,
                adVo.car.drivetrain
            )

            carDetailsWheelTv.text = getString(
                R.string.car_details_wheel,
                adVo.car.wheel
            )

            carDetailsConditionTv.text = getString(
                R.string.car_details_condition,
                adVo.car.condition
            )

            carDetailsOwnersTv.text = getString(
                R.string.car_details_owners,
                adVo.car.owners.toString()
            )

            carDetailsOwnershipPeriodTv.text = getString(
                R.string.car_details_ownership_period,
                adVo.car.ownershipPeriod
            )

            carDetailsVinTv.text = getString(
                R.string.car_details_vin,
                adVo.car.vin
            )

            carDetailsSellerSCommentTvValue.text = adVo.car.description
        }
    }

    class DeleteAdDialog(private val onPositiveClick: () -> Unit) : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.delete_ad_dialog_title))
                .setMessage(getString(R.string.delete_ad_dialog_message))
                .setPositiveButton(getString(R.string.delete_ad_dialog_positive_button)) { dialog, _ ->
                    onPositiveClick()
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.delete_ad_dialog_negative_button)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

        companion object {
            const val TAG = "DeleteAdDialog"
        }
    }

    private fun deleteAd() {
        carDetailsViewModel.deleteAd(args.adId)
        navigator.popBackStack()
        showSnackbar(binding.root, getString(R.string.ad_deleted))
    }


    override fun onDestroy() {
        super.onDestroy()
        _carPhotoAdapter = null
    }
}
