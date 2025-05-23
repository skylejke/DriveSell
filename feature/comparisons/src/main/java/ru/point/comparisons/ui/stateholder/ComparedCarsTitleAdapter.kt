package ru.point.comparisons.ui.stateholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import ru.point.cars.model.AdVo
import ru.point.common.BuildConfig
import ru.point.comparisons.R
import ru.point.comparisons.databinding.ComparedCarTitleItemBinding

internal class ComparedCarsTitleAdapter(
    private val onCarPhotoClick: (AdVo) -> Unit,
    private val onDeleteCarClick: (AdVo) -> Unit
) :
    ListAdapter<AdVo, ComparedCarsTitleAdapter.ComparedCarsTitleViewHolder>(ComparedCarsTitleDiffUtilCallback()) {

    class ComparedCarsTitleViewHolder(
        private val binding: ComparedCarTitleItemBinding,
        private val onCarPhotoClick: (AdVo) -> Unit,
        private val onDeleteCarClick: (AdVo) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(adVo: AdVo) {
            with(binding) {
                comparedCarPhoto.load("${BuildConfig.BASE_URL}/photos/${adVo.photos.first()}") {
                    transformations(
                        RoundedCornersTransformation(
                            8 * binding.root.context.resources.displayMetrics.density
                        )
                    )
                }

                comparedCarPhoto.setOnClickListener {
                    onCarPhotoClick(adVo)
                }

                comparedCarPriceTv.text = root.context.getString(R.string.car_price, adVo.car.price)
                    .replace(",", " ")

                comparedCarTitleTv.text = root.context.getString(
                    R.string.car_brand_model_year,
                    adVo.car.brand,
                    adVo.car.model,
                    adVo.car.year
                )

                deleteIcon.setOnClickListener {
                    onDeleteCarClick(adVo)
                }
            }
        }
    }

    class ComparedCarsTitleDiffUtilCallback : DiffUtil.ItemCallback<AdVo>() {
        override fun areItemsTheSame(oldItem: AdVo, newItem: AdVo) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: AdVo, newItem: AdVo) =
            oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ComparedCarsTitleViewHolder(
            ComparedCarTitleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCarPhotoClick = onCarPhotoClick,
            onDeleteCarClick = onDeleteCarClick
        )

    override fun onBindViewHolder(holder: ComparedCarsTitleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}