package ru.point.menu.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.point.menu.R
import ru.point.menu.databinding.MenuItemBinding

internal class MenuAdapter(
    private val onProfileClick: () -> Unit,
    private val onUsersAdsClick: () -> Unit,
    private val onComparisonsClick: () -> Unit,
    private val onAboutClick: () -> Unit,
    private val onLogOutClick: () -> Unit,
    private val onLogInClick: () -> Unit,
) : ListAdapter<MenuItem, RecyclerView.ViewHolder>(MenuDiffUtilCallback()) {

    class ProfileViewHolder(private val binding: MenuItemBinding, private val onProfileClick: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(binding) {
                menuItemIcon.background = AppCompatResources.getDrawable(root.context, R.drawable.profile_icon)
                menuItemTv.text = root.context.getString(R.string.my_profile)
                root.setOnClickListener { onProfileClick() }
            }
        }
    }

    class UsersAdsViewHolder(private val binding: MenuItemBinding, private val onUsersAdsClick: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(binding) {
                menuItemIcon.background = AppCompatResources.getDrawable(root.context, R.drawable.users_ads_icon)
                menuItemTv.text = root.context.getString(R.string.my_ads)
                root.setOnClickListener { onUsersAdsClick() }
            }
        }
    }

    class ComparisonsViewHolder(private val binding: MenuItemBinding, private val onComparisonsClick: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(binding) {
                menuItemIcon.background = AppCompatResources.getDrawable(root.context, R.drawable.comparisons_icon)
                menuItemTv.text = root.context.getString(R.string.comparisons)
                root.setOnClickListener { onComparisonsClick() }
            }
        }
    }

    class AboutViewHolder(private val binding: MenuItemBinding, private val onAboutClick: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(binding) {
                menuItemIcon.background = AppCompatResources.getDrawable(root.context, R.drawable.about_icon)
                menuItemTv.text = root.context.getString(R.string.about_app)
                root.setOnClickListener { onAboutClick() }
            }
        }
    }

    class LogOutViewHolder(private val binding: MenuItemBinding, private val onLogOutClick: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(binding) {
                menuItemIcon.background = AppCompatResources.getDrawable(root.context, R.drawable.log_out_icon)
                menuItemTv.text = root.context.getString(R.string.log_out)
                root.setOnClickListener { onLogOutClick() }
            }
        }
    }

    class LogInViewHolder(private val binding: MenuItemBinding, private val onLogInClick: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(binding) {
                menuItemIcon.background = AppCompatResources.getDrawable(root.context, R.drawable.login_icon)
                menuItemTv.text = root.context.getString(R.string.log_in)
                root.setOnClickListener { onLogInClick() }
            }
        }
    }

    class MenuDiffUtilCallback : DiffUtil.ItemCallback<MenuItem>() {
        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when (viewType) {
            VIEW_TYPE_PROFILE -> ProfileViewHolder(binding = binding, onProfileClick = onProfileClick)
            VIEW_TYPE_USERS_ADS -> UsersAdsViewHolder(binding = binding, onUsersAdsClick = onUsersAdsClick)
            VIEW_TYPE_COMPARISONS -> ComparisonsViewHolder(binding = binding, onComparisonsClick = onComparisonsClick)
            VIEW_TYPE_ABOUT -> AboutViewHolder(binding = binding, onAboutClick = onAboutClick)
            VIEW_TYPE_LOG_OUT -> LogOutViewHolder(binding = binding, onLogOutClick = onLogOutClick)
            VIEW_TYPE_LOG_IN -> LogInViewHolder(binding = binding, onLogInClick = onLogInClick)
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (getItem(position)) {
            is MenuItem.Profile -> (holder as ProfileViewHolder).bind()
            is MenuItem.UsersAds -> (holder as UsersAdsViewHolder).bind()
            is MenuItem.Comparisons -> (holder as ComparisonsViewHolder).bind()
            is MenuItem.About -> (holder as AboutViewHolder).bind()
            is MenuItem.LogOut -> (holder as LogOutViewHolder).bind()
            is MenuItem.Login -> (holder as LogInViewHolder).bind()
        }

    override fun getItemViewType(position: Int) =
        when (getItem(position)) {
            is MenuItem.Profile -> VIEW_TYPE_PROFILE
            is MenuItem.UsersAds -> VIEW_TYPE_USERS_ADS
            is MenuItem.Comparisons -> VIEW_TYPE_COMPARISONS
            is MenuItem.About -> VIEW_TYPE_ABOUT
            is MenuItem.LogOut -> VIEW_TYPE_LOG_OUT
            is MenuItem.Login -> VIEW_TYPE_LOG_IN
        }

    companion object {
        private const val VIEW_TYPE_PROFILE = 0
        private const val VIEW_TYPE_USERS_ADS = 1
        private const val VIEW_TYPE_COMPARISONS = 2
        private const val VIEW_TYPE_ABOUT = 3
        private const val VIEW_TYPE_LOG_OUT = 4
        private const val VIEW_TYPE_LOG_IN = 5
    }
}

internal sealed interface MenuItem {
    data object Profile : MenuItem
    data object UsersAds : MenuItem
    data object Comparisons : MenuItem
    data object About : MenuItem
    data object LogOut : MenuItem
    data object Login : MenuItem
}

class NonScrollableLinearLayoutManager(
    context: Context,
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {
    override fun canScrollVertically(): Boolean = false
}