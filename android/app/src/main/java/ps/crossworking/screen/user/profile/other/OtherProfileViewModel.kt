package ps.crossworking.screen.user.profile.other

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.model.User
import ps.crossworking.screen.user.IGetUserUseCase
import javax.inject.Inject

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val data: User) : ProfileState()
    data class Error(val errorMessageId: Int) : ProfileState()
}

@HiltViewModel
class OtherProfileViewModel @Inject constructor(
    private val useCase: IGetUserUseCase
) : ViewModel() {

    private val _profileState: MutableState<ProfileState> = mutableStateOf(ProfileState.Loading)
    val profileState: State<ProfileState> = _profileState

    private val _isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isRefreshing: State<Boolean> = _isRefreshing

    private val _errorMessageId: MutableState<Int?> = mutableStateOf(null)
    val errorMessageId: State<Int?> = _errorMessageId

    fun getProfile(userId: String) {
        viewModelScope.launch {
            try {
                _profileState.value = ProfileState.Success(useCase(userId))
            } catch (e: Exception) {
                if (!isRefreshing.value)
                    _profileState.value =
                        ProfileState.Error(errorConversion(e))
                else
                    _errorMessageId.value = errorConversion(e)
            }

            _isRefreshing.value = false
        }
    }

    fun refresh(userId: String) {
        if (_profileState.value is ProfileState.Loading)
            return

        _isRefreshing.value = true
        getProfile(userId)
    }

    fun clearError() {
        _errorMessageId.value = null
    }
}