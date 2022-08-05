package ps.crossworking.screen.user.profile.own

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import ps.crossworking.R
import ps.crossworking.common.UserInstance
import ps.crossworking.model.User
import ps.crossworking.ui.composables.*

@Composable
fun OwnProfileScreen(
    viewModel: OwnProfileViewModel = hiltViewModel(),
    goToUserForm: () -> Unit,
    goToIdeaList: (String) -> Unit,
    goToSkillList: (userId: String) -> Unit,
    goToResults: () -> Unit,
    goToAuthRoute: () -> Unit
) {
    val isRefreshing by remember { viewModel.isRefreshing }
    val isLoggedOut by remember { viewModel.isLoggedOut }
    val errorMessageId by remember { viewModel.errorMessageId }
    val takingAction by remember { viewModel.takingAction }

    DisposableEffect(key1 = Unit) {
        viewModel.getProfile()
        onDispose {
            if (isLoggedOut is LogoutState.Completed)
                viewModel.restoreInitialState()
            viewModel.endAction()
        }
    }

    when (isLoggedOut) {
        is LogoutState.LoggedOut -> {
            goToAuthRoute()
            viewModel.makeComplete()
            return
        }

        is LogoutState.Completed -> {
            return
        }

        else -> {}
    }

    if (errorMessageId != null) {
        Toast.makeText(
            LocalContext.current,
            stringResource(id = errorMessageId!!),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.clearError()
    }

    val user: User = UserInstance.user.value!!

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refresh() },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                scale = true,
                contentColor = MaterialTheme.colors.primary,
                backgroundColor = MaterialTheme.colors.background,
                shape = RoundedCornerShape(33.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                BorderedProfilePicture(imageUrl = user.profileImage!!, size = 150.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = user.name!!,
                    style = bigBoldMainColorTextStyle()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = user.about ?: "",
                    style = normalTextStyle(),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp)
                )
            }

            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 18.dp, end = 18.dp),
                shape = RoundedCornerShape(33.dp),
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
            ) {
                SkillsList(skills = user.skills.map { it.name })
            }

            Column {
                BigRedButton(
                    text = stringResource(id = R.string.edit_button),
                    isEnable = !takingAction
                ) {
                    viewModel.startAction()
                    goToUserForm()
                }
                Spacer(modifier = Modifier.height(12.dp))
                BigRedButton(
                    text = stringResource(id = R.string.profile_idea_list_button),
                    isEnable = !takingAction
                ) {
                    viewModel.startAction()
                    goToIdeaList(user.userId)
                }
                Spacer(modifier = Modifier.height(12.dp))
                BigRedButton(
                    text = stringResource(id = R.string.skill_list_button),
                    isEnable = !takingAction
                ) {
                    viewModel.startAction()
                    goToSkillList(user.userId)
                }
                Spacer(modifier = Modifier.height(12.dp))
                BigRedButton(
                    text = stringResource(id = R.string.profile_results_button),
                    isEnable = !takingAction
                ) {
                    viewModel.startAction()
                    goToResults()
                }
                Spacer(modifier = Modifier.height(12.dp))
                BigRedButton(
                    text = stringResource(id = R.string.profile_logout_button),
                    isEnable = !takingAction
                ) {
                    viewModel.logOut()
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
