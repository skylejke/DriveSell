package ru.point.comparisons.ui.stateholder

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
            comparedCarEnginePowerValue.text = adVo.car.enginePower.toString()

            comparedCarEngineCapacityValue.text = adVo.car.engineCapacity.toString()

            comparedCarFuelTypeValue.text = adVo.car.fuelType

            comparedCarMileageValue.text = adVo.car.mileage.toString()

            comparedCarBodyTypeValue.text = adVo.car.bodyType

            comparedCarColorValue.text = adVo.car.color

            comparedCarTransmissionValue.text = adVo.car.transmission

            comparedCarDrivetrainValue.text = adVo.car.drivetrain

            comparedCarWheelValue.text = adVo.car.wheel

            comparedCarConditionValue.text = adVo.car.condition

            comparedCarOwnersValue.text = adVo.car.owners.toString()
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