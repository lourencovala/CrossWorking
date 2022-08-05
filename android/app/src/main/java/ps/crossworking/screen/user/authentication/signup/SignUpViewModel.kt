package ps.crossworking.screen.user.authentication.signup

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
import ps.crossworking.screen.user.authentication.ISignUpGoogleUseCase
import javax.inject.Inject

sealed class SignUpState {
    object Idle : SignUpState()
    object WaitingForSignUp : SignUpState()
    object Success : SignUpState()
    object Completed : SignUpState()
    data class Error(val errorMessageId: Int) : SignUpState()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: ISignUpUseCase,
    private val signUpGoogleUseCase: ISignUpGoogleUseCase
) : ViewModel() {

    private val _state: MutableState<SignUpState> = mutableStateOf(SignUpState.Idle)
    val state: State<SignUpState> = _state

    fun createAccount(email: String, password: String) {
        _state.value = SignUpState.WaitingForSignUp
        viewModelScope.launch {
            try {
                UserInstance.user.value = signUpUseCase(email, password)
                _state.value = SignUpState.Success
            } catch (e: Exception) {
                _state.value = SignUpState.Error(errorConversion(e))
            }

        }
    }

    fun signInWithGoogle(intent: Intent?) {
        _state.value = SignUpState.WaitingForSignUp
        viewModelScope.launch {
            try {
                UserInstance.user.value = signUpGoogleUseCase(intent)
                _state.value = SignUpState.Success
            } catch (e: Exception) {
                _state.value = SignUpState.Error(errorConversion(e))
            }
        }
    }

    fun makeCompleted() {
        _state.value = SignUpState.Completed
    }

    fun restartInitialState() {
        _state.value = SignUpState.Idle
    }
}
