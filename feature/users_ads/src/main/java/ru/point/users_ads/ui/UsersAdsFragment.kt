package ru.point.users_ads.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.combine
import ru.point.cars.ui.CarAdapter
import ru.point.cars.ui.CarAdapterDecorator
import ru.point.common.ext.bottomBar
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.model.Status
import ru.point.common.ui.ComponentHolderFragment
import ru.point.users_ads.databinding.FragmentUsersAdsBinding
import ru.point.users_ads.di.UsersAdsComponentHolderVM
import ru.point.users_ads.di.usersAdsComponent
import javax.inject.Inject

internal class UsersAdsFragment : ComponentHolderFragment<FragmentUsersAdsBinding>() {

    @Inject
    lateinit var usersAdsViewModelFactory: UsersAdsViewModelFactory

    private val usersAdsViewModel by viewModels<UsersAdsViewModel> { usersAdsViewModelFactory }

    private var _carAdapter: CarAdapter? = null
    private val carAdapter get() = requireNotNull(_carAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _carAdapter = CarAdapter { navigator.fromUsersAdsFragmentToCarDetailsFragment(it.id, it.userId) }
        initHolder<UsersAdsComponentHolderVM>()
        usersAdsComponent.inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUsersAdsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomBar.hide()

        usersAdsViewModel.getUsersAds()

        binding.myAdsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = carAdapter
            addItemDecoration(CarAdapterDecorator())
        }

        repeatOnLifecycleScope {
            combine(usersAdsViewModel.status, usersAdsViewModel.usersAds) { status, cars -> status to cars }
                .collect { (status, cars) ->
                    with(binding) {
                        updatePlaceholder(status)
                        if (status == Status.Success) {
                            emptyPlaceholder.root.isVisible = cars.isEmpty()
                            myAdsList.isVisible = cars.isNotEmpty()
                            if (cars.isNotEmpty()) {
                                carAdapter.submitList(cars)
                            }
                        }
                    }
                }
        }

        binding.noConnectionPlaceholder.tryAgainTv.setOnClickListener {
            usersAdsViewModel.getUsersAds()
        }

        binding.swipeRefresh.setOnRefreshListener {
            usersAdsViewModel.getUsersAds()
        }

        binding.usersAdsToolBar.backIcon.setOnClickListener {
            navigator.popBackStack()
        }
    }

    private fun updatePlaceholder(status: Status) {
        with(binding) {
            when (status) {
                is Status.Loading -> {
                    shimmerLayout.isVisible = true
                    shimmerLayout.startShimmer()
                    myAdsList.isVisible = false
                    noConnectionPlaceholder.root.isVisible = false
                    emptyPlaceholder.root.isVisible = false
                    swipeRefresh.isRefreshing = true
                }

                is Status.Success -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.isVisible = false
                    myAdsList.isVisible = true
                    noConnectionPlaceholder.root.isVisible = false
                    swipeRefresh.isRefreshing = true
                }

                is Status.Error -> {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.isVisible = false
                    myAdsList.isVisible = false
                    noConnectionPlaceholder.root.isVisible = true
                    emptyPlaceholder.root.isVisible = false
                    swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _carAdapter = null
    }
}