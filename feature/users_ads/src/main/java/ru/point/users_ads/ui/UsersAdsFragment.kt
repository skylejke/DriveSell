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
import ru.point.common.di.FeatureDepsProvider
import ru.point.common.ext.bottomBar
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.model.Status
import ru.point.common.ui.BaseFragment
import ru.point.users_ads.databinding.FragmentUsersAdsBinding
import ru.point.users_ads.di.DaggerUsersAdsComponent
import javax.inject.Inject

internal class UsersAdsFragment : BaseFragment<FragmentUsersAdsBinding>() {

    @Inject
    lateinit var usersAdsViewModelFactory: UsersAdsViewModelFactory

    private val usersAdsViewModel by viewModels<UsersAdsViewModel> { usersAdsViewModelFactory }

    private var _carAdapter: CarAdapter? = null
    private val carAdapter get() = requireNotNull(_carAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerUsersAdsComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .build()
            .inject(this)

        _carAdapter = CarAdapter { navigator.fromUsersAdsFragmentToCarDetailsFragment(it.id, it.userId) }
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUsersAdsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomBar.hide()

        usersAdsViewModel.getUsersAds()

        binding.usersAdsRv.apply {
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
                            usersAdsEmptyPlaceholder.root.isVisible = cars.isEmpty()
                            usersAdsRv.isVisible = cars.isNotEmpty()
                            if (cars.isNotEmpty()) {
                                carAdapter.submitList(cars)
                            }
                        }
                    }
                }
        }

        binding.usersAdsNoConnectionPlaceholder.tryAgainTv.setOnClickListener {
            usersAdsViewModel.getUsersAds()
        }

        binding.usersAdsSwipeRefresh.setOnRefreshListener {
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
                    usersAdsShimmerLayout.isVisible = true
                    usersAdsShimmerLayout.startShimmer()
                    usersAdsRv.isVisible = false
                    usersAdsNoConnectionPlaceholder.root.isVisible = false
                    usersAdsEmptyPlaceholder.root.isVisible = false
                    usersAdsSwipeRefresh.isRefreshing = true
                    usersAdsSwipeRefresh.isVisible = true
                }

                is Status.Success -> {
                    usersAdsShimmerLayout.stopShimmer()
                    usersAdsShimmerLayout.isVisible = false
                    usersAdsRv.isVisible = true
                    usersAdsNoConnectionPlaceholder.root.isVisible = false
                    usersAdsSwipeRefresh.isRefreshing = false
                    usersAdsSwipeRefresh.isVisible = true
                }

                is Status.Error -> {
                    usersAdsShimmerLayout.stopShimmer()
                    usersAdsShimmerLayout.isVisible = false
                    usersAdsRv.isVisible = false
                    usersAdsNoConnectionPlaceholder.root.isVisible = true
                    usersAdsEmptyPlaceholder.root.isVisible = false
                    usersAdsSwipeRefresh.isRefreshing = false
                    usersAdsSwipeRefresh.isVisible = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _carAdapter = null
    }
}