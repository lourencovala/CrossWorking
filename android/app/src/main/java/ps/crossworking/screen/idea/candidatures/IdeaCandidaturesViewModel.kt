package ps.crossworking.screen.idea.candidatures

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.model.Candidate
import javax.inject.Inject

sealed class IdeaCandidaturesState {
    object Loading : IdeaCandidaturesState()
    data class Success(val data: List<Candidate>) : IdeaCandidaturesState()
    data class Error(val errorMessageId: Int) : IdeaCandidaturesState()
}

@HiltViewModel
class IdeaCandidaturesViewModel @Inject constructor(
    private val getIdeaCandidaturesUseCase: IGetIdeaCandidaturesUseCase,
    private val updateCandidateUseCase: UpdateCandidateUseCase
) : ViewModel() {

    private val _state: MutableState<IdeaCandidaturesState> =
        mutableStateOf(IdeaCandidaturesState.Loading)
    val state: State<IdeaCandidaturesState> = _state

    private val _isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isRefreshing: State<Boolean> = _isRefreshing

    private val _errorMessageId: MutableState<Int?> = mutableStateOf(null)
    val errorMessageId: State<Int?> = _errorMessageId

    fun getIdeaCandidates(ideaId: String) {
        viewModelScope.launch {
            try {
                _state.value =
                    IdeaCandidaturesState.Success(getIdeaCandidaturesUseCase(ideaId, 0))
            } catch (e: Exception) {
                if (!_isRefreshing.value) {
                    _state.value =
                        IdeaCandidaturesState.Error(errorConversion(e))
                } else
                    _errorMessageId.value = errorConversion(e)
            }
            _isRefreshing.value = false
        }
    }

    fun getNextPage(userId: String) {
        if (_state.value !is IdeaCandidaturesState.Success)
            return
        viewModelScope.launch {
            try {
                val list = (_state.value as IdeaCandidaturesState.Success).data
                val newData = getIdeaCandidaturesUseCase(userId, list.size)
                _state.value = IdeaCandidaturesState.Success(listOf(list, newData).flatten())
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
        }
    }

    fun refresh(ideaId: String) {
        if (_state.value == IdeaCandidaturesState.Loading)
            return

        _isRefreshing.value = true

        getIdeaCandidates(ideaId)
    }

    fun updateCandidate(ideaId: String, candidateId: String, isAccepted: Boolean) {
        viewModelScope.launch {
            try {
                val updated = updateCandidateUseCase(ideaId, candidateId, isAccepted)
                _state.value =
                    IdeaCandidaturesState.Success(((_state.value as IdeaCandidaturesState.Success).data as MutableList).map {
                        if (it.user.userId == candidateId)
                            updated
                        else
                            it
                    })
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
        }
    }

    fun clearError() {
        _errorMessageId.value = null
    }
}
