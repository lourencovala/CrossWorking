package ps.crossworking.screen.skill.manageUser

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ps.crossworking.common.UserInstance
import ps.crossworking.screen.skill.SkillScreen
import ps.crossworking.ui.composables.ErrorScreen
import ps.crossworking.ui.composables.LoadingScreen

@Composable
fun ManageUserSkillsScreen(
    userId: String,
    viewModel: ManageUserSkillsViewModel = hiltViewModel(),
    onComplete: (() -> Unit)?,
    goToAddSkill: (userId: String) -> Unit
) {
    val state by remember { viewModel.state }
    val isRefreshing by remember { viewModel.isRefreshing }
    val updatedErrorMessageId by remember { viewModel.errorMessageId }

    DisposableEffect(key1 = Unit) {
        if (viewModel.state.value !is LoadState.Success)
            viewModel.getScreenInfo(userId)
        else
            viewModel.refreshing(userId)
        onDispose {
            if (state is LoadState.Completed)
                viewModel.restoreInitialState()
        }
    }

    if (state is LoadState.FullManaged) {
        if (onComplete != null) {
            onComplete()
        }
        viewModel.makeComplete()
        return
    }

    if (updatedErrorMessageId != null) {
        Toast.makeText(
            LocalContext.current,
            stringResource(id = updatedErrorMessageId!!),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.clearError()
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refreshing(userId) },
        indicator = { refreshState, trigger ->
            SwipeRefreshIndicator(
                // Pass the SwipeRefreshState + trigger through
                state = refreshState,
                refreshTriggerDistance = trigger,
                // Enable the scale animation
                scale = true,
                // Change the color and shape
                contentColor = MaterialTheme.colors.primary,
                backgroundColor = MaterialTheme.colors.background,
                shape = RoundedCornerShape(33.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (val value: LoadState = state) {
                is LoadState.Loading -> {
                    LoadingScreen()
                }

                is LoadState.Success -> {
                    SkillScreen(
                        skills = value.data,
                        isDeletable = userId == UserInstance.user.value?.userId,
                        goToAddSkill = { goToAddSkill(userId) },
                        onComplete = { viewModel.endManagement() }
                    ) {
                        viewModel.deleteSkill(userId, it)
                    }
                }

                is LoadState.Error -> {
                    ErrorScreen(errorMessage = stringResource(id = value.errorMessageId))
                }
            }
        }
    }
}
