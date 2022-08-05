package ps.crossworking.screen.idea.form

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ps.crossworking.common.UserInstance
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.model.Idea
import ps.crossworking.screen.idea.IGetIdeaUseCase
import javax.inject.Inject

sealed class IdeaFormState {
    object Loading : IdeaFormState()
    data class GotIdea(val idea: Idea) : IdeaFormState()
    data class CreatedIdea(val idea: Idea) : IdeaFormState()
    data class EditedIdea(val idea: Idea) : IdeaFormState()
    object Completed : IdeaFormState()
    data class Error(val errorMessageId: Int) : IdeaFormState()
}

@HiltViewModel
class IdeaFormViewModel @Inject constructor(
    val createIdeaUseCase: ICreateIdeaUseCase,
    val getIdeaUseCase: IGetIdeaUseCase,
    val editIdeaUseCase: IEditIdeaUseCase
) : ViewModel() {

    private val _ideaFormState: MutableState<IdeaFormState> = mutableStateOf(IdeaFormState.Loading)
    val ideaFormState: State<IdeaFormState> = _ideaFormState

    private val _takingAction: MutableState<Boolean> = mutableStateOf(false)
    val takingAction: State<Boolean> = _takingAction

    private val _errorMessageId: MutableState<Int?> = mutableStateOf(null)
    val errorMessageId: State<Int?> = _errorMessageId

    fun getIdea(ideaId: String) {
        _ideaFormState.value = IdeaFormState.Loading
        clearError()
        viewModelScope.launch {
            try {
                val idea = getIdeaUseCase(ideaId)
                _ideaFormState.value = IdeaFormState.GotIdea(idea)
            } catch (e: Exception) {
                _ideaFormState.value = IdeaFormState.Error(errorConversion(e))
            }
        }
    }

    fun createIdea(title: String, smallDescription: String, description: String) {
        _takingAction.value = true
        clearError()
        viewModelScope.launch {
            try {
                val idea =
                    createIdeaUseCase(
                        UserInstance.user.value!!.userId,
                        title,
                        smallDescription,
                        description
                    )
                _ideaFormState.value = IdeaFormState.CreatedIdea(idea)
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
            _takingAction.value = false
        }
    }

    fun editIdea(title: String, smallDescription: String, description: String) {
        _takingAction.value = true
        clearError()
        viewModelScope.launch {
            try {
                val idea = editIdeaUseCase(
                    (_ideaFormState.value as IdeaFormState.GotIdea).idea.ideaId,
                    title,
                    smallDescription,
                    description
                )
                _ideaFormState.value = IdeaFormState.EditedIdea(idea)
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
            _takingAction.value = false
        }
    }

    fun makeCompleted() {
        _ideaFormState.value = IdeaFormState.Completed
    }

    fun restoreInitialState() {
        _ideaFormState.value = IdeaFormState.Loading
    }

    private fun clearError() {
        _errorMessageId.value = null
    }
}
