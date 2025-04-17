package ru.point.comparisons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.point.common.ui.ComponentHolderFragment
import ru.point.comparisons.databinding.FragmentComparisonsBinding
import ru.point.comparisons.di.ComparisonsComponentHolderVM
import ru.point.comparisons.di.comparisonsComponent
import javax.inject.Inject

internal class ComparisonsFragment : ComponentHolderFragment<FragmentComparisonsBinding>() {

    @Inject
    lateinit var comparisonsViewModelFactory: ComparisonsViewModelFactory

    private val comparisonsViewModel by viewModels<ComparisonsViewModel> { comparisonsViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHolder<ComparisonsComponentHolderVM>()
        comparisonsComponent.inject(this)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentComparisonsBinding.inflate(inflater, container, false)
}