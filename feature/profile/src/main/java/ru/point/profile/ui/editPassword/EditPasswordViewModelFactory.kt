package ru.point.profile.ui.editPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.common.utils.ResourceProvider
import ru.point.user.repository.UserRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
internal class EditPasswordViewModelFactory @Inject constructor(
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditPasswordViewModel(userRepository = userRepository, resourceProvider = resourceProvider) as T
}