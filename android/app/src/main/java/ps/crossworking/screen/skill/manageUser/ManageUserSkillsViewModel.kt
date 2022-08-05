package ps.crossworking.screen.skill.manageUser

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.model.Skill
import javax.inject.Inject

sealed class LoadState {
    object Loading : LoadState()
    data class Success(val data: List<Skill>) : LoadState()
    object FullManaged : LoadState()
    object Completed : LoadState()
    data class Error(val errorMessageId: Int) : LoadState()
}

@HiltViewModel
class ManageUserSkillsViewModel @Inject constructor(
    private val getUserSkillsUseCase: IGetUserSkillsUseCase,
    private val deleteUserSkillUseCase: IDeleteUserSkillUseCase
) : ViewModel() {

    private val _isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isRefreshing: State<Boolean> = _isRefreshing

    private val _mutableState: MutableState<LoadState> = mutableStateOf(LoadState.Loading)
    val state: State<LoadState> = _mutableState

    private val _errorMessageId: MutableState<Int?> = mutableStateOf(null)
    val errorMessageId: State<Int?> = _errorMessageId

    fun getScreenInfo(userId: String) {
        viewModelScope.launch {
            try {
                _mutableState.value = LoadState.Success(getUserSkillsUseCase(userId))
            } catch (e: Exception) {
                if (!isRefreshing.value)
                    _mutableState.value = LoadState.Error(errorConversion(e))
                else
                    _errorMessageId.value = errorConversion(e)
            }
            _isRefreshing.value = false
        }
    }

    fun deleteSkill(userId: String, skillId: String) {
        viewModelScope.launch {
            try {
                deleteUserSkillUseCase(userId, skillId)
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
            refreshing(userId)
        }
    }

    fun refreshing(userId: String) {
        if (state.value is LoadState.Loading)
            return

        _isRefreshing.value = true
        getScreenInfo(userId)
    }

    fun endManagement() {
        _mutableState.value = LoadState.FullManaged
    }

    fun makeComplete() {
        _mutableState.value = LoadState.Completed
    }

    fun restoreInitialState() {
        _mutableState.value = LoadState.Loading
    }

    fun clearError() {
        _errorMessageId.value = null
    }
}