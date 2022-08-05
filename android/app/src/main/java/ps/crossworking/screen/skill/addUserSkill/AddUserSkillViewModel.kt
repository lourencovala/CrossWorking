package ps.crossworking.screen.skill.addUserSkill

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ps.crossworking.common.UserInstance
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.model.Category
import ps.crossworking.screen.skill.IGetCategoryUseCase
import javax.inject.Inject

sealed class AddUserSkillsState {
    object Loading : AddUserSkillsState()
    data class FetchedCategories(val categories: List<Category>) : AddUserSkillsState()
    object Added : AddUserSkillsState()
    object Completed : AddUserSkillsState()
    data class Error(val errorMessageId: Int) : AddUserSkillsState()
}

@HiltViewModel
class AddUserSkillViewModel @Inject constructor(
    private val getCategoryUseCase: IGetCategoryUseCase,
    private val addUserSkillUseCase: IAddUserSkillUseCase
) : ViewModel() {

    private val _mutableState: MutableState<AddUserSkillsState> =
        mutableStateOf(AddUserSkillsState.Loading)
    val state: State<AddUserSkillsState> = _mutableState

    private val _takingAction: MutableState<Boolean> = mutableStateOf(false)
    val takingAction: State<Boolean> = _takingAction

    private val _errorMessageId: MutableState<Int?> = mutableStateOf(null)
    val errorMessageId: State<Int?> = _errorMessageId

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                _mutableState.value =
                    AddUserSkillsState.FetchedCategories(getCategoryUseCase())
            } catch (e: Exception) {
                _mutableState.value = AddUserSkillsState.Error(errorConversion(e))
            }
        }
    }

    fun addSkill(skillName: String, about: String, categoryId: String) {
        clearError()
        viewModelScope.launch {
            _takingAction.value = true
            try {
                addUserSkillUseCase(UserInstance.user.value?.userId!!, skillName, about, categoryId)
                _mutableState.value = AddUserSkillsState.Added
            } catch (ex: Exception) {
                _errorMessageId.value = errorConversion(ex)
            }
            _takingAction.value = false
        }
    }

    fun makeComplete() {
        _mutableState.value = AddUserSkillsState.Completed
    }

    fun restoreInitialState() {
        _mutableState.value = AddUserSkillsState.Loading
    }

    fun clearError() {
        _errorMessageId.value = null
    }
}