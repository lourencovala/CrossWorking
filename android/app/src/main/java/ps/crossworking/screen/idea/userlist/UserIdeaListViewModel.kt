package ps.crossworking.screen.idea.userlist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.model.ShortIdea
import javax.inject.Inject

sealed class ListState {
    object Loading : ListState()
    data class Success(val data: List<ShortIdea>) : ListState()
    data class Error(val errorMessageId: Int) : ListState()
}

@HiltViewModel
class UserIdeaListViewModel @Inject constructor(
    val getUserIdeas: IGetUserIdeasUseCase
) : ViewModel() {

    private val _listState: MutableState<ListState> = mutableStateOf(ListState.Loading)
    val listState: State<ListState> = _listState

    private val _isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isRefreshing: State<Boolean> = _isRefreshing

    private val _errorMessageId: MutableState<Int?> = mutableStateOf(null)
    val errorMessageId: State<Int?> = _errorMessageId

    fun getList(userId: String) {
        viewModelScope.launch {
            try {
                _listState.value = ListState.Success(getUserIdeas(userId, 0))
            } catch (e: Exception) {
                if (!isRefreshing.value)
                    _listState.value = ListState.Error(errorConversion(e))
                else
                    _errorMessageId.value = errorConversion(e)
            }
            _isRefreshing.value = false
        }
    }

    fun getNextPage(userId: String) {
        if (_listState.value !is ListState.Success)
            return
        viewModelScope.launch {
            try {
                val list = (_listState.value as ListState.Success).data
                val newData = getUserIdeas.invoke(userId, list.size)
                _listState.value = ListState.Success(listOf(list, newData).flatten())
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
        }
    }

    fun refresh(userId: String) {
        if (_listState.value is ListState.Loading)
            return

        _isRefreshing.value = true
        getList(userId)
    }

    fun clearError() {
        _errorMessageId.value = null
    }
}