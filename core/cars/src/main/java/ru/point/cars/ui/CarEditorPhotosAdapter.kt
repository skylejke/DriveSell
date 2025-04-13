package ru.point.cars.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.point.cars.databinding.ItemAddPhotoBinding
import ru.point.cars.databinding.ItemPhotoBinding

class CarEditorPhotosAdapter(
    private val onAddPhotoClick: () -> Unit,
    private val onDeletePhotoClick: (CarEditorPhotosAdapterItem.Photo) -> Unit
) :
    ListAdapter<CarEditorPhotosAdapterItem, RecyclerView.ViewHolder>(PhotoDiffCallback()) {

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
        private val onDeletePhotoClick: (CarEditorPhotosAdapterItem.Photo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: CarEditorPhotosAdapterItem.Photo) {
            binding.photoImage.load(photo.uri)
            binding.deleteIcon.setOnClickListener { onDeletePhotoClick(photo) }
        }
    }

    class PhotoDiffCallback : DiffUtil.ItemCallback<CarEditorPhotosAdapterItem>() {
        override fun areItemsTheSame(oldItem: CarEditorPhotosAdapterItem, newItem: CarEditorPhotosAdapterItem) =
            when {
                oldItem is CarEditorPhotosAdapterItem.ButtonEditor && newItem is CarEditorPhotosAdapterItem.ButtonEditor -> true
                oldItem is CarEditorPhotosAdapterItem.Photo && newItem is CarEditorPhotosAdapterItem.Photo -> oldItem.uri == newItem.uri
                else -> false
            }

        override fun areContentsTheSame(oldItem: CarEditorPhotosAdapterItem, newItem: CarEditorPhotosAdapterItem): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val VIEW_TYPE_ADD = 0
        private const val VIEW_TYPE_PHOTO = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CarEditorPhotosAdapterItem.ButtonEditor -> VIEW_TYPE_ADD
            is CarEditorPhotosAdapterItem.Photo -> VIEW_TYPE_PHOTO
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
            is CarEditorPhotosAdapterItem.ButtonEditor -> (holder as AddPhotoViewHolder).bind()
            is CarEditorPhotosAdapterItem.Photo -> (holder as PhotoViewHolder).bind(item)
        }
    }
}

sealed class CarEditorPhotosAdapterItem {
    object ButtonEditor : CarEditorPhotosAdapterItem()
    data class Photo(val uri: Uri, val photoId: String? = null) : CarEditorPhotosAdapterItem()
}

