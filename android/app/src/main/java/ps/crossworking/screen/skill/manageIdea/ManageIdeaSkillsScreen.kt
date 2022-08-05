package ps.crossworking.screen.skill.manageIdea

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ps.crossworking.screen.skill.SkillScreen
import ps.crossworking.ui.composables.ErrorScreen
import ps.crossworking.ui.composables.LoadingScreen

@Composable
fun ManageIdeaSkillScreen(
    viewModel: ManageIdeaSkillsViewModel = hiltViewModel(),
    ideaId: String,
    isEditable: Boolean,
    goToAddSkill: ((ideaId: String) -> Unit)?,
    onComplete: ((ideaId: String) -> Unit)?
) {
    val state by remember { viewModel.state }
    val isRefreshing by remember { viewModel.isRefreshing }
    val updatedErrorMessageId by remember { viewModel.errorMessageId }

    DisposableEffect(key1 = Unit) {
        if (viewModel.state.value !is LoadState.Success)
            viewModel.getScreenInfo(ideaId)
        else
            viewModel.refreshing(ideaId)
        onDispose {
            if (state is LoadState.Completed)
                viewModel.restoreInitialState()
        }
    }

    if (state is LoadState.FullManaged) {
        if (onComplete != null) {
            onComplete(ideaId)
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
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { viewModel.refreshing(ideaId) }
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
                        isDeletable = isEditable,
                        goToAddSkill = {
                            if (goToAddSkill != null) {
                                goToAddSkill(ideaId)
                            }
                        },
                        onComplete = { viewModel.endManagement() },
                        deleteSkill = { viewModel.deleteSkill(ideaId, it) }
                    )
                }

                is LoadState.Error -> {
                    ErrorScreen(errorMessage = stringResource(id = value.errorMessageId))
                }
            }
        }
    }
}