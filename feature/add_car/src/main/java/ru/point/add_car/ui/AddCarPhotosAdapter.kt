package ru.point.add_car.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.point.add_car.databinding.ItemAddPhotoBinding
import ru.point.add_car.databinding.ItemPhotoBinding

internal class AddCarPhotosAdapter(
    private val onAddPhotoClick: () -> Unit,
    private val onDeletePhotoClick: (AddCarPhotosAdapterItem.Photo) -> Unit
) :
    ListAdapter<AddCarPhotosAdapterItem, RecyclerView.ViewHolder>(PhotoDiffCallback()) {

    class AddPhotoViewHolder(
        private val binding: ItemAddPhotoBinding,
        private val onAddPhotoClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener { onAddPhotoClick() }
        }
    }

    class PhotoViewHolder(
        private val binding: ItemPhotoBinding,
        private val onDeletePhotoClick: (AddCarPhotosAdapterItem.Photo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: AddCarPhotosAdapterItem.Photo) {
            binding.photoImage.load(photo.uri)
            binding.deleteIcon.setOnClickListener { onDeletePhotoClick(photo) }
        }
    }

    class PhotoDiffCallback : DiffUtil.ItemCallback<AddCarPhotosAdapterItem>() {
        override fun areItemsTheSame(oldItem: AddCarPhotosAdapterItem, newItem: AddCarPhotosAdapterItem) =
            when {
                oldItem is AddCarPhotosAdapterItem.AddButton && newItem is AddCarPhotosAdapterItem.AddButton -> true
                oldItem is AddCarPhotosAdapterItem.Photo && newItem is AddCarPhotosAdapterItem.Photo -> oldItem.uri == newItem.uri
                else -> false
            }

        override fun areContentsTheSame(oldItem: AddCarPhotosAdapterItem, newItem: AddCarPhotosAdapterItem): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val VIEW_TYPE_ADD = 0
        private const val VIEW_TYPE_PHOTO = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AddCarPhotosAdapterItem.AddButton -> VIEW_TYPE_ADD
            is AddCarPhotosAdapterItem.Photo -> VIEW_TYPE_PHOTO
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ADD -> {
                AddPhotoViewHolder(
                    binding = ItemAddPhotoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onAddPhotoClick = onAddPhotoClick
                )
            }

            VIEW_TYPE_PHOTO -> PhotoViewHolder(
                ItemPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onDeletePhotoClick = onDeletePhotoClick
            )

            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is AddCarPhotosAdapterItem.AddButton -> (holder as AddPhotoViewHolder).bind()
            is AddCarPhotosAdapterItem.Photo -> (holder as PhotoViewHolder).bind(item)
        }
    }
}

sealed class AddCarPhotosAdapterItem {
    object AddButton : AddCarPhotosAdapterItem()
    data class Photo(val uri: Uri) : AddCarPhotosAdapterItem()
}

