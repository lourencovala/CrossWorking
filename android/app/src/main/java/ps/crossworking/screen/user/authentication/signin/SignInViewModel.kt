package ps.crossworking.screen.user.authentication.signin

import android.content.Intent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ps.crossworking.common.UserInstance
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.model.User
import ps.crossworking.screen.user.authentication.ISignUpGoogleUseCase
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: ISignInUseCase,
    private val signUpGoogleUseCase: ISignUpGoogleUseCase
) : ViewModel() {

    sealed class SignInState {
        object Idle : SignInState()
        object WaitingForLogin : SignInState()
        data class Success(val user: User) : SignInState()
        object Completed : SignInState()
        data class Error(val errorMessageId: Int) : SignInState()
    }

    private val _state: MutableState<SignInState> = mutableStateOf(SignInState.Idle)
    val state: State<SignInState> = _state

    fun login(email: String, password: String) {
        _state.value = SignInState.WaitingForLogin
        viewModelScope.launch {
            try {
                val user = signInUseCase(email, password)
                UserInstance.user.value = user
                _state.value = SignInState.Success(user)
            } catch (exception: Exception) {
                _state.value = SignInState.Error(errorConversion(exception))
            }
        }
    }

    fun signInWithGoogle(intent: Intent?) {
        _state.value = SignInState.WaitingForLogin
        viewModelScope.launch {
            try {
                val user = signUpGoogleUseCase(intent)
                UserInstance.user.value = user
                _state.value = SignInState.Success(user)
            } catch (e: Exception) {
                _state.value = SignInState.Error(errorConversion(e))
            }
        }
    }

    fun makeComplete() {
        _state.value = SignInState.Completed
    }

    fun restoreInitialState() {
        _state.value = SignInState.Idle
    }
}
