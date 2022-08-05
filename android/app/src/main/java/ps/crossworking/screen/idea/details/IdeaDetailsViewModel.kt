package ps.crossworking.screen.idea.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ps.crossworking.common.UserInstance
import ps.crossworking.exceptions.IdeaNotFoundException
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.model.Candidature
import ps.crossworking.model.Idea
import ps.crossworking.screen.idea.IGetIdeaUseCase
import javax.inject.Inject

/**
 * First we obtain the idea, then we try and get the candidature. (if it's not the idea's owner)
 * Loading -> GotIdeaAndIsOwner or GotIdeaAndLoadingCandidature
 * GotIdeaAndLoadingCandidature -> GotIdeaButNoCandidature or GotIdeaAndCandidature
 * GotIdeaAndCandidature has candidature property which is null if user hasn't applied.
 */
sealed class IdeaDetailsState {
    object Loading : IdeaDetailsState()
    data class GotIdeaAndIsOwner(val idea: Idea) : IdeaDetailsState()
    data class GotIdeaAndLoadingCandidature(val idea: Idea) : IdeaDetailsState()
    data class GotIdeaButNoCandidature(val idea: Idea) : IdeaDetailsState()
    data class GotIdeaAndCandidature(val idea: Idea, val candidature: Candidature?) :
        IdeaDetailsState()
    object Deleted : IdeaDetailsState()
    object Completed : IdeaDetailsState()
    data class Error(val errorMessageId: Int) : IdeaDetailsState()
}

@HiltViewModel
class IdeaDetailsViewModel @Inject constructor(
    private val getIdeaDetailsUseCase: IGetIdeaUseCase,
    private val deleteIdeaUseCase: IDeleteIdeaUseCase,
    private val applyToIdeaUseCase: IApplyToIdeaUseCase,
    private val getCandidatureStatusUseCase: IGetCandidatureStatusUseCase,
    private val undoApplyIdeaUseCase: IUndoApplyIdeaUseCase
) : ViewModel() {

    private val _ideaState: MutableState<IdeaDetailsState> =
        mutableStateOf(IdeaDetailsState.Loading)
    val ideaState: State<IdeaDetailsState> = _ideaState

    private val _isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isRefreshing: State<Boolean> = _isRefreshing

    private val _takingAction: MutableState<Boolean> = mutableStateOf(false)
    val takingAction: State<Boolean> = _takingAction

    private val _errorMessageId: MutableState<Int?> = mutableStateOf(null)
    val errorMessageId: State<Int?> = _errorMessageId

    fun getIdea(ideaId: String) {
        viewModelScope.launch {
            try {
                val idea = getIdeaDetailsUseCase(ideaId)
                if (idea.user.userId != UserInstance.user.value!!.userId) {
                    _ideaState.value = IdeaDetailsState.GotIdeaAndLoadingCandidature(idea)
                    getCandidatureStatus(ideaId)
                } else _ideaState.value = IdeaDetailsState.GotIdeaAndIsOwner(idea)
            } catch (e: Exception) {
                val errorStringId = errorConversion(e)
                if (!_isRefreshing.value)
                    _ideaState.value =
                        IdeaDetailsState.Error(errorStringId)
                else _errorMessageId.value = errorStringId
            }
            _isRefreshing.value = false
        }
    }

    fun refresh(ideaId: String) {
        _isRefreshing.value = true
        getIdea(ideaId)
    }

    fun delete() {
        startAction()
        val idea = when(val value = _ideaState.value) {
            is IdeaDetailsState.GotIdeaAndIsOwner -> value.idea
            else -> null
        }
        viewModelScope.launch {
            try {
                deleteIdeaUseCase(idea?.ideaId ?: throw IdeaNotFoundException())
                _ideaState.value = IdeaDetailsState.Deleted
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
            endAction()
        }
    }

    fun applyToIdea(ideaId: String) {
        startAction()
        viewModelScope.launch {
            try {
                val candidature = applyToIdeaUseCase(UserInstance.user.value!!.userId, ideaId)
                val currentState =
                    (_ideaState.value as IdeaDetailsState.GotIdeaAndCandidature)

                _ideaState.value = IdeaDetailsState.GotIdeaAndCandidature(
                    currentState.idea,
                    candidature
                )
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
            endAction()
        }
    }

    private fun getCandidatureStatus(ideaId: String) {
        startAction()
        viewModelScope.launch {
            try {
                val candidature =
                    getCandidatureStatusUseCase(UserInstance.user.value!!.userId, ideaId)

                _ideaState.value = IdeaDetailsState.GotIdeaAndCandidature(
                    (_ideaState.value as IdeaDetailsState.GotIdeaAndLoadingCandidature).idea,
                    candidature
                )
            } catch (e: Exception) {
                _ideaState.value =
                    IdeaDetailsState.GotIdeaButNoCandidature((_ideaState.value as IdeaDetailsState.GotIdeaAndLoadingCandidature).idea)

                _errorMessageId.value = errorConversion(e)
            }
            endAction()
        }
    }

    fun undoApply(ideaId: String) {
        startAction()
        viewModelScope.launch {
            try {
                undoApplyIdeaUseCase(UserInstance.user.value!!.userId, ideaId)

                val currentState = (_ideaState.value as IdeaDetailsState.GotIdeaAndCandidature)

                _ideaState.value = IdeaDetailsState.GotIdeaAndCandidature(
                    currentState.idea,
                    null
                )
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
            endAction()
        }
    }

    fun startAction() {
        _takingAction.value = true
    }

    fun endAction() {
        _takingAction.value = false
    }

    fun makeComplete() {
        _ideaState.value = IdeaDetailsState.Completed
    }

    fun restoreInitialState() {
        _ideaState.value = IdeaDetailsState.Loading
    }

    fun clearError() {
        _errorMessageId.value = null
    }
}
