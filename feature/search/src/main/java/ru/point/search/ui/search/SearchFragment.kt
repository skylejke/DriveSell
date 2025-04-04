package ru.point.search.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import ru.point.common.ui.ComponentHolderFragment
import ru.point.search.databinding.FragmentSearchBinding
import ru.point.search.di.SearchComponentHolderVM
import ru.point.search.di.searchComponent
import javax.inject.Inject

internal class SearchFragment : ComponentHolderFragment<FragmentSearchBinding>() {

    @Inject
    lateinit var searchViewModelFactory: SearchViewModelFactory

    private val searchViewModel by viewModels<SearchViewModel> { searchViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHolder<SearchComponentHolderVM>()
        searchComponent.inject(this)
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

        binding.searchTil.setStartIconOnClickListener {
            navigator.popBackStack()
        }
    }
}