package ps.crossworking.screen.user.profile.own

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ps.crossworking.common.UserInstance
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.screen.user.IGetUserUseCase
import javax.inject.Inject

sealed class LogoutState {
    object Idle : LogoutState()
    object LoggedOut : LogoutState()
    object Completed : LogoutState()
}

@HiltViewModel
class OwnProfileViewModel @Inject constructor(
    private val getUser: IGetUserUseCase,
    private val signOut: ISignOutUseCase
) : ViewModel() {

    private val _isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isRefreshing: State<Boolean> = _isRefreshing

    private val _isLoggedOut: MutableState<LogoutState> = mutableStateOf(LogoutState.Idle)
    val isLoggedOut: State<LogoutState> = _isLoggedOut

    private val _takingAction: MutableState<Boolean> = mutableStateOf(false)
    val takingAction: State<Boolean> = _takingAction

    private val _errorMessageId: MutableState<Int?> = mutableStateOf(null)
    val errorMessageId: State<Int?> = _errorMessageId

    fun getProfile() {
        startAction()
        viewModelScope.launch {
            try {
                UserInstance.user.value = getUser(UserInstance.user.value!!.userId)
                _isRefreshing.value = false
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
            endAction()
        }
    }

    fun refresh() {
        _isRefreshing.value = true
        getProfile()
    }

    fun logOut() {
        startAction()
        viewModelScope.launch {
            try {
                signOut()
                _isLoggedOut.value = LogoutState.LoggedOut
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
            endAction()
        }
    }

    fun makeComplete() {
        _isLoggedOut.value = LogoutState.Completed
    }

    fun restoreInitialState() {
        _isLoggedOut.value = LogoutState.Idle
    }

    fun startAction() {
        _takingAction.value = true
    }

    fun endAction() {
        _takingAction.value = false
    }

    fun clearError() {
        _errorMessageId.value = null
    }
}