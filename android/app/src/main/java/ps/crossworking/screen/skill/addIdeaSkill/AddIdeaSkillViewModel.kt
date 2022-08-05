package ps.crossworking.screen.skill.addIdeaSkill

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.model.Category
import ps.crossworking.screen.skill.IGetCategoryUseCase
import javax.inject.Inject

sealed class AddIdeaSkillsState {
    object Loading : AddIdeaSkillsState()
    data class FetchedCategories(val categories: List<Category>) : AddIdeaSkillsState()
    object Added : AddIdeaSkillsState()
    object Completed : AddIdeaSkillsState()
    data class Error(val errorMessageId: Int) : AddIdeaSkillsState()
}

@HiltViewModel
class AddIdeaSkillViewModel @Inject constructor(
    private val addIdeaSkillUseCase: IAddIdeaSkillUseCase,
    private val getCategoryUseCase: IGetCategoryUseCase
) : ViewModel() {

    private val _mutableState: MutableState<AddIdeaSkillsState> =
        mutableStateOf(AddIdeaSkillsState.Loading)
    val state: State<AddIdeaSkillsState> = _mutableState

    private val _takingAction: MutableState<Boolean> = mutableStateOf(false)
    val takingAction: State<Boolean> = _takingAction

    private val _errorMessageId: MutableState<Int?> = mutableStateOf(null)
    val errorMessageId: State<Int?> = _errorMessageId

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                _mutableState.value =
                    AddIdeaSkillsState.FetchedCategories(getCategoryUseCase())
            } catch (e: Exception) {
                _mutableState.value = AddIdeaSkillsState.Error(errorConversion(e))
            }
        }
    }

    fun addSkill(ideaId: String, skillName: String, about: String, categoryId: String) {
        clearError()
        viewModelScope.launch {
            _takingAction.value = true

            try {
                addIdeaSkillUseCase(ideaId, skillName, about, categoryId)
                _mutableState.value = AddIdeaSkillsState.Added
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
            _takingAction.value = false
        }
    }

    fun restoreInitialState() {
        _mutableState.value = AddIdeaSkillsState.Loading
    }

    fun makeComplete() {
        _mutableState.value = AddIdeaSkillsState.Completed
    }

    fun clearError() {
        _errorMessageId.value = null
    }
}