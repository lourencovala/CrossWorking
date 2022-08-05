package ps.crossworking.screen.idea.feed

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.model.ShortIdea
import java.lang.System.currentTimeMillis
import javax.inject.Inject

sealed class FeedState {
    object Loading : FeedState()
    data class Success(val data: List<ShortIdea>, val timeOfGet: Long) : FeedState()
    data class Error(val errorMessageId: Int) : FeedState()
    data class LeftScreen(val data: List<ShortIdea>, val timeOfGet: Long) : FeedState()
}

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeed: IGetFeedUseCase
) : ViewModel() {

    private val _feedState: MutableState<FeedState> = mutableStateOf(FeedState.Loading)
    val feedState: State<FeedState> = _feedState

    private val _isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isRefreshing: State<Boolean> = _isRefreshing

    private val _errorMessageId: MutableState<Int?> = mutableStateOf(null)
    val errorMessageId: State<Int?> = _errorMessageId

    fun getFeed() {
        viewModelScope.launch {
            try {
                _feedState.value = FeedState.Success(getFeed(0), currentTimeMillis())
            } catch (e: Exception) {
                if (!isRefreshing.value)
                    _feedState.value = FeedState.Error(errorConversion(e))

                _errorMessageId.value = errorConversion(e)

            }
            _isRefreshing.value = false
        }
    }

    fun getNextPage() {
        if (_feedState.value !is FeedState.Success)
            return

        viewModelScope.launch {
            try {
                val list = (_feedState.value as FeedState.Success).data
                val newData = getFeed(list.size)
                _feedState.value =
                    FeedState.Success(listOf(list, newData).flatten(), currentTimeMillis())
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
        }
    }

    fun refresh() {
        if (_feedState.value is FeedState.Loading)
            return

        _isRefreshing.value = true
        getFeed()
    }

    /**
     * When leaving to a screen based on an interaction in FeedScreen.
     * Change state and save data.
     */
    fun handleLeave(): Boolean {
        if (_feedState.value !is FeedState.Success)
            return false

        val state = (_feedState.value as FeedState.Success)
        _feedState.value = FeedState.LeftScreen(state.data, state.timeOfGet)

        return true
    }

    /**
     * Handles return from known location.
     * If data is older than wanted time, refresh.
     */
    fun cameBack() {
        if (_feedState.value !is FeedState.LeftScreen)
            return

        val state = (_feedState.value as FeedState.LeftScreen)
        val currentTime = currentTimeMillis()

        if (currentTime - state.timeOfGet >= 300_000)
            refresh()
        else _feedState.value = FeedState.Success(state.data, state.timeOfGet)
    }

    /**
     * Handles return from unknown location.
     * These locations are other screens not related to feed, e.g. buttons in bottom bar.
     * If data is older than wanted time, refresh.
     */
    fun cameFromUnknown() {
        if (_feedState.value !is FeedState.Success)
            return

        val state = (_feedState.value as FeedState.Success)
        val currentTime = currentTimeMillis()

        if (currentTime - state.timeOfGet >= 300_000)
            refresh()
    }

    fun clearError() {
        _errorMessageId.value = null
    }
}