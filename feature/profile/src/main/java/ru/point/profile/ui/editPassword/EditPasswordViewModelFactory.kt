package ru.point.profile.ui.editPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.profile.domain.EditPasswordUseCase

@Suppress("UNCHECKED_CAST")
internal class EditPasswordViewModelFactory(
    private val editPasswordUseCase: EditPasswordUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditPasswordViewModel(
            editPasswordUseCase = editPasswordUseCase
        ) as T
}