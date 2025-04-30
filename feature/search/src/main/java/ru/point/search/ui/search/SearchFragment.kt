package ru.point.search.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.point.common.di.FeatureDepsProvider
import ru.point.common.ext.repeatOnLifecycleScope
import ru.point.common.ui.BaseFragment
import ru.point.search.databinding.FragmentSearchBinding
import ru.point.search.di.DaggerSearchComponent
import ru.point.search.ui.search.stateholder.SearchHistoryAdapter
import ru.point.search.ui.search.stateholder.SearchHistoryAdapterDecorator
import javax.inject.Inject

internal class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    @Inject
    lateinit var searchViewModelFactory: SearchViewModelFactory

    private val searchViewModel by viewModels<SearchViewModel> { searchViewModelFactory }

    private var _searchHistoryAdapter: SearchHistoryAdapter? = null
    private val searchHistoryAdapter get() = requireNotNull(_searchHistoryAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSearchComponent
            .builder()
            .deps(FeatureDepsProvider.featureDeps)
            .build()
            .inject(this)

        _searchHistoryAdapter = SearchHistoryAdapter { navigator.fromSearchFragmentToSearchResultsFragment(it.query) }
    }

    override fun onStart() {
        super.onStart()
        binding.searchEt.requestFocus()
        binding.searchEt.post {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(binding.searchEt, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchHistoryRv.apply {
            adapter = searchHistoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(SearchHistoryAdapterDecorator(requireContext()))
        }

        repeatOnLifecycleScope {
            searchViewModel.searchHistory.collect {
                searchHistoryAdapter.submitList(it)
                binding.youSearchedTv.isVisible = it.isNotEmpty()
                binding.clearSearchHistoryBtn.isVisible = it.isNotEmpty()
            }
        }

        binding.searchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.searchEt.text.toString().isNotBlank()) {
                    searchViewModel.insertSearchHistoryItem(binding.searchEt.text.toString())
                }
                navigator.fromSearchFragmentToSearchResultsFragment(query = binding.searchEt.text.toString())
                true
            } else {
                false
            }
        }

        binding.clearSearchHistoryBtn.setOnClickListener {
            searchViewModel.clearSearchHistory()
        }

        binding.searchTil.setStartIconOnClickListener {
            navigator.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _searchHistoryAdapter = null
    }
}