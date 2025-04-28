package ru.point.cars.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.point.cars.BuildConfig
import ru.point.cars.R
import ru.point.cars.databinding.CarListItemBinding
import ru.point.cars.model.AdVo


class CarAdapter(private val onAdapterItemClick: OnAdapterItemClick<AdVo>) :
    ListAdapter<AdVo, CarAdapter.CarViewHolder>(DiffUtilCarCallback()) {

    class CarViewHolder(private val binding: CarListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(adVo: AdVo) = with(binding) {

            price.text = root.context.getString(R.string.car_price, adVo.car.price)
                .replace(",", " ")

            brandModelYear.text = root.context.getString(
                R.string.car_brand_model_year,
                adVo.car.brand,
                adVo.car.model,
                adVo.car.year
            )

            specs.text = root.context.getString(
                R.string.car_specs,
                adVo.car.mileage.toString(),
                adVo.car.engineCapacity.toString(),
                adVo.car.enginePower.toString(),
                adVo.car.fuelType,
                adVo.car.drivetrain,
                adVo.car.transmission
            )

            photo.load("${BuildConfig.BASE_URL}/photos/${adVo.photos.first().ifEmpty {  error(R.drawable.error_photo_placeholder) }}") {
                transformations(
                    coil.transform.RoundedCornersTransformation(
                        8 * binding.root.context.resources.displayMetrics.density
                    )
                )
            }
        }
    }

    class DiffUtilCarCallback() : DiffUtil.ItemCallback<AdVo>() {
        override fun areItemsTheSame(oldItem: AdVo, newItem: AdVo) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: AdVo, newItem: AdVo) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CarViewHolder(
            CarListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onAdapterItemClick.onClick(getItem(position))
        }
    }
}