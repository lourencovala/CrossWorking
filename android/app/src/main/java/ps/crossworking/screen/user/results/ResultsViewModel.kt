package ps.crossworking.screen.user.results

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ps.crossworking.common.UserInstance
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.model.CandidatureResult
import javax.inject.Inject

sealed class ResultsState {
    object Loading : ResultsState()
    data class Success(val data: List<CandidatureResult>) : ResultsState()
    data class Error(val errorMessageId: Int) : ResultsState()
}

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val getResultsUseCase: IGetResultsUseCase
) : ViewModel() {

    private val _resultsState: MutableState<ResultsState> = mutableStateOf(ResultsState.Loading)
    val resultsState: State<ResultsState> = _resultsState

    private val _isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isRefreshing: State<Boolean> = _isRefreshing

    private val _errorMessageId: MutableState<Int?> = mutableStateOf(null)
    val errorMessageId: State<Int?> = _errorMessageId

    fun getResults() {
        viewModelScope.launch {
            try {
                _resultsState.value =
                    ResultsState.Success(getResultsUseCase(UserInstance.user.value!!.userId, 0))
            } catch (e: Exception) {
                if (!_isRefreshing.value)
                    _resultsState.value =
                        ResultsState.Error(errorConversion(e))
                else _errorMessageId.value = errorConversion(e)
            }
            _isRefreshing.value = false
        }
    }

    fun getNextPage() {
        if (_resultsState.value !is ResultsState.Success)
            return

        viewModelScope.launch {
            try {
                val list = (_resultsState.value as ResultsState.Success).data
                val newData = getResultsUseCase(UserInstance.user.value!!.userId, list.size)
                _resultsState.value = ResultsState.Success(listOf(list, newData).flatten())
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
        }
    }

    fun refresh() {
        if (_resultsState.value == ResultsState.Loading)
            return

        _isRefreshing.value = true
        getResults()
    }

    fun clearError() {
        _errorMessageId.value = null
    }
}