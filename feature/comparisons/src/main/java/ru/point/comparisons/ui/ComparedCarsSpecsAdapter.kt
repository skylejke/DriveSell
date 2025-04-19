package ru.point.comparisons.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.point.cars.model.AdVo
import ru.point.comparisons.databinding.ComparedCarSpecsItemBinding

internal class ComparedCarsSpecsAdapter :
    ListAdapter<AdVo, ComparedCarsSpecsAdapter.ComparedCarViewHolder>(ComparedCarDiffCallback()) {

    class ComparedCarViewHolder(val binding: ComparedCarSpecsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(adVo: AdVo) = with(binding) {
            enginePowerValue.text = adVo.car.enginePower.toString()

            engineCapacityValue.text = adVo.car.engineCapacity.toString()

            fuelTypeValue.text = adVo.car.fuelType

            mileageValue.text = adVo.car.mileage.toString()

            bodyTypeValue.text = adVo.car.bodyType

            colorValue.text = adVo.car.color

            transmissionValue.text = adVo.car.transmission

            drivetrainValue.text = adVo.car.drivetrain

            wheelValue.text = adVo.car.wheel

            conditionValue.text = adVo.car.condition

            ownersValue.text = adVo.car.owners.toString()
        }
    }

    class ComparedCarDiffCallback : DiffUtil.ItemCallback<AdVo>() {
        override fun areItemsTheSame(oldItem: AdVo, newItem: AdVo) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: AdVo, newItem: AdVo) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ComparedCarViewHolder(
            ComparedCarSpecsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ComparedCarViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.titleGroup.visibility = if (position == 0) View.VISIBLE else View.INVISIBLE
    }
}