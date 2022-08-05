package ps.crossworking.screen.user.profile.other

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
fun OtherProfileScreen(
    viewModel: OtherProfileViewModel = hiltViewModel(),
    userId: String,
    goToSkillList: (userId: String) -> Unit,
    goToIdeaList: (userId: String) -> Unit
) {
    val state by remember { viewModel.profileState }
    val isRefreshing by remember { viewModel.isRefreshing }
    val errorMessageId by remember { viewModel.errorMessageId }

    DisposableEffect(key1 = Unit) {
        if (state !is ProfileState.Success)
            viewModel.getProfile(userId)
        onDispose { }
    }

    if (errorMessageId != null) {
        Toast.makeText(
            LocalContext.current,
            stringResource(id = errorMessageId!!),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.clearError()
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refresh(userId) },
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
        },
    ) {
        when (val value: ProfileState = state) {
            is ProfileState.Loading -> {
                LoadingScreen()
            }

            is ProfileState.Success -> {
                Profile(value.data, goToSkillList, goToIdeaList)
            }

            is ProfileState.Error -> ErrorScreen(errorMessage = stringResource(id = value.errorMessageId))
        }
    }
}

@Composable
fun Profile(
    user: User,
    goToSkillList: (userId: String) -> Unit,
    goToIdeaList: (userId: String) -> Unit
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
            BigRedButton(text = stringResource(id = R.string.profile_idea_list_button)) {
                goToIdeaList(user.userId)
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (user.skills.isNotEmpty() || user.userId == UserInstance.user.value!!.userId) {
                BigRedButton(text = stringResource(id = R.string.skill_list_button)) {
                    goToSkillList(user.userId)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}