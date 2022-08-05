package ps.crossworking.helper

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ps.crossworking.common.UserInstance
import ps.crossworking.exceptions.NetworkException
import ps.crossworking.screen.user.IIsLoggedInUseCase
import ps.crossworking.screen.user.profile.own.ISignOutUseCase

sealed class AccountState {
    object Loading : AccountState()
    object NotLoggedIn : AccountState()
    object NeedsInfo : AccountState()
    object LoggedIn : AccountState()
    object NetworkError : AccountState()
}

class HelperViewModel(
    private val getOwnUser: IGetOwnUserUseCase,
    private val isLoggedIn: IIsLoggedInUseCase,
    private val signOut: ISignOutUseCase
) : ViewModel() {

    private val _mutableState: MutableState<AccountState> = mutableStateOf(AccountState.Loading)
    val state: State<AccountState> = _mutableState

    private val _isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isRefreshing: State<Boolean> = _isRefreshing

    fun getUserIfExist() {
        viewModelScope.launch {
            if (!isLoggedIn()) {
                _mutableState.value = AccountState.NotLoggedIn
                return@launch
            }

            try {
                val user = getOwnUser()
                UserInstance.user.value = user

                if (user.name != null)
                    _mutableState.value = AccountState.LoggedIn
                else _mutableState.value = AccountState.NeedsInfo
            } catch (e: Exception) {
                if (e is NetworkException)
                    _mutableState.value = AccountState.NetworkError
                else
                    try {
                        signOut()
                        _mutableState.value = AccountState.NotLoggedIn
                    } catch (e: Exception) {
                        _mutableState.value = AccountState.NotLoggedIn
                    }
            }
            _isRefreshing.value = false
        }
    }

    fun refresh() {
        _isRefreshing.value = true
        getUserIfExist()
    }
}
