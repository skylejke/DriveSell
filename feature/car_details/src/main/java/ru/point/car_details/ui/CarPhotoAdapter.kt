package ru.point.car_details.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.point.car_details.databinding.FragmentCarDetailsPhotoItemBinding
import ru.point.cars.BuildConfig

class CarPhotoAdapter :
    ListAdapter<String, CarPhotoAdapter.CarPhotoViewHolder>(DiffUtilCarPhotoCallback()) {

    class CarPhotoViewHolder(private val binding: FragmentCarDetailsPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photoId: String) {
            binding.carPhoto.load("${BuildConfig.BASE_URL}/photos/$photoId")
        }
    }

    class DiffUtilCarPhotoCallback() : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CarPhotoViewHolder(
        FragmentCarDetailsPhotoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: CarPhotoViewHolder, position: Int) =
        holder.bind(getItem(position))
}