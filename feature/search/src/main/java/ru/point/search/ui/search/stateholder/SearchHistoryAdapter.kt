package ru.point.search.ui.search.stateholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.point.cars.model.SearchHistory
import ru.point.cars.ui.OnAdapterItemClick
import ru.point.search.databinding.SearchHistoryListItemBinding

internal class SearchHistoryAdapter(private val onAdapterItemClick: OnAdapterItemClick<SearchHistory>) :
    ListAdapter<SearchHistory, SearchHistoryAdapter.SearchHistoryViewHolder>(DiffUtilSearchHistoryCallBack()) {

    class SearchHistoryViewHolder(private val binding: SearchHistoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(searchHistory: SearchHistory) {
            binding.searchHistoryListItem.text = searchHistory.query
        }
    }

    class DiffUtilSearchHistoryCallBack() : DiffUtil.ItemCallback<SearchHistory>() {
        override fun areItemsTheSame(oldItem: SearchHistory, newItem: SearchHistory) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: SearchHistory, newItem: SearchHistory) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SearchHistoryViewHolder(
            SearchHistoryListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onAdapterItemClick.onClick(getItem(position))
        }
    }
}