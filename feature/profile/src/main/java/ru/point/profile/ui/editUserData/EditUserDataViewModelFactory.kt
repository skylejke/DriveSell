package ru.point.profile.ui.editUserData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.profile.domain.EditUserDataUseCase
import ru.point.profile.domain.GetUserDataUseCase

@Suppress("UNCHECKED_CAST")
internal class EditUserDataViewModelFactory(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val editUserDataUseCase: EditUserDataUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditUserDataViewModel(
            getUserDataUseCase = getUserDataUseCase,
            editUserDataUseCase = editUserDataUseCase
        ) as T
}