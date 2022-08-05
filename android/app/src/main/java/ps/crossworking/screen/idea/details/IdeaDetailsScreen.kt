package ps.crossworking.screen.idea.details

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ps.crossworking.R
import ps.crossworking.model.Candidature
import ps.crossworking.model.Idea
import ps.crossworking.ui.composables.*
import ps.crossworking.ui.theme.outline

@Composable
fun IdeaDetailsScreen(
    viewModel: IdeaDetailsViewModel = hiltViewModel(),
    ideaId: String,
    goBack: () -> Unit,
    onUserClick: (userId: String) -> Unit,
    goToIdeaCandidates: (ideaId: String) -> Unit,
    onEditClick: (ideaId: String) -> Unit,
    onViewSkillsClick: (ideaId: String, ownIdea: Boolean) -> Unit
) {
    val ideaState by remember { viewModel.ideaState }
    val isRefreshing by remember { viewModel.isRefreshing }
    val takingAction by remember { viewModel.takingAction }
    val updatedErrorMessageId by remember { viewModel.errorMessageId }

    DisposableEffect(key1 = Unit) {
        if (ideaState is IdeaDetailsState.Loading)
            viewModel.getIdea(ideaId)

        onDispose {
            if (ideaState is IdeaDetailsState.Completed)
                viewModel.restoreInitialState()
            viewModel.endAction()
        }
    }

    if (ideaState is IdeaDetailsState.Deleted) {
        goBack()
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
        onRefresh = { viewModel.refresh(ideaId) },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                // Pass the SwipeRefreshState + trigger through
                state = state,
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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val value: IdeaDetailsState = ideaState) {
                is IdeaDetailsState.Loading -> {
                    LoadingScreen()
                }
                is IdeaDetailsState.GotIdeaAndLoadingCandidature -> {
                    IdeaDetails(
                        viewModel,
                        value.idea,
                        null,
                        "loading",
                        takingAction,
                        onUserClick,
                        goToIdeaCandidates,
                        onEditClick,
                        onViewSkillsClick
                    )
                }

                is IdeaDetailsState.GotIdeaAndCandidature -> {
                    IdeaDetails(
                        viewModel,
                        value.idea,
                        value.candidature,
                        "got",
                        takingAction,
                        onUserClick,
                        goToIdeaCandidates,
                        onEditClick,
                        onViewSkillsClick
                    )
                }

                is IdeaDetailsState.GotIdeaAndIsOwner -> {
                    IdeaDetails(
                        viewModel,
                        value.idea,
                        null,
                        "owner",
                        takingAction,
                        onUserClick,
                        goToIdeaCandidates,
                        onEditClick,
                        onViewSkillsClick
                    )
                }

                is IdeaDetailsState.GotIdeaButNoCandidature -> {
                    IdeaDetails(
                        viewModel,
                        value.idea,
                        null,
                        "failed",
                        takingAction,
                        onUserClick,
                        goToIdeaCandidates,
                        onEditClick,
                        onViewSkillsClick
                    )
                }

                is IdeaDetailsState.Error -> ErrorScreen(errorMessage = stringResource(id = value.errorMessageId))

                else -> {}
            }
        }
    }
}

@Composable
fun IdeaDetails(
    viewModel: IdeaDetailsViewModel,
    idea: Idea,
    candidature: Candidature?,
    obtainingCandidatureStatus: String,
    takingAction: Boolean,
    onUserClick: (userId: String) -> Unit,
    goToIdeaCandidatures: (ideaId: String) -> Unit,
    onEditClick: (ideaId: String) -> Unit,
    onViewSkillsClick: (ideaId: String, ownIdea: Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, start = 12.dp, end = 12.dp)
                .clip(CircleShape)
                .clickable { onUserClick(idea.user.userId) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            BorderedProfilePicture(
                imageUrl = idea.user.profileImage!!,
                size = 46.dp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = idea.user.name!!,
                style = mediumBoldMainColorTextStyle(),
                modifier = Modifier.padding(start = 24.dp, top = 2.dp)
            )
        }

        Row(
            modifier = Modifier.padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = idea.title,
                style = bigBoldMainColorTextStyle()
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
        ) {
            Text(
                text = idea.smallDescription,
                style = mediumTextStyle()
            )
        }
        Divider(
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Card(
                modifier = Modifier
                    .padding(top = 14.dp, start = 14.dp, end = 14.dp),
                shape = RoundedCornerShape(33.dp),
                backgroundColor = MaterialTheme.colors.background,
                border = BorderStroke(2.dp, MaterialTheme.colors.outline)
            ) {
                Column(
                    modifier = Modifier
                        .padding(34.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = idea.description,
                        style = normalTextStyle()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        SkillsList(skills = idea.skills.map { it.name })
        Spacer(modifier = Modifier.height(20.dp))

        if (obtainingCandidatureStatus == "owner") {
            BigRedButton(
                text = stringResource(id = R.string.idea_candidatures_button),
                isEnable = !takingAction
            ) {
                viewModel.startAction()
                goToIdeaCandidatures(idea.ideaId)
            }
            Spacer(modifier = Modifier.height(12.dp))
            BigRedButton(
                text = stringResource(id = R.string.edit_button),
                isEnable = !takingAction
            ) {
                viewModel.startAction()
                onEditClick(idea.ideaId)
            }
            Spacer(modifier = Modifier.height(12.dp))
            BigRedButton(
                text = stringResource(id = R.string.skill_list_button),
                isEnable = !takingAction
            ) {
                viewModel.startAction()
                onViewSkillsClick(idea.ideaId, true)
            }
            Spacer(modifier = Modifier.height(12.dp))
            BigRedButton(
                text = stringResource(id = R.string.delete_button),
                isEnable = !takingAction
            ) {
                viewModel.delete()
            }
        } else {
            if (idea.skills.isNotEmpty()) {
                BigRedButton(
                    text = stringResource(id = R.string.skill_list_button),
                    isEnable = !takingAction
                ) {
                    viewModel.startAction()
                    onViewSkillsClick(idea.ideaId, false)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            BigRedButton(
                text = candidature?.status?.replaceFirstChar { it.uppercase() }
                    ?: if (obtainingCandidatureStatus == "loading") stringResource(id = R.string.loading_text)
                    else stringResource(id = R.string.apply_button),
                isEnable = !takingAction && candidature == null
            ) {
                viewModel.applyToIdea(idea.ideaId)
            }
            if (candidature?.status == "pending") {
                Spacer(modifier = Modifier.height(12.dp))
                BigRedButton(
                    text = stringResource(id = R.string.undo_candidature_button)
                ) {
                    viewModel.undoApply(idea.ideaId)
                }
            }

            if (obtainingCandidatureStatus == "got" && candidature != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${stringResource(id = R.string.last_update_text)} ${candidature.daysSinceLastUpdate} ${
                        stringResource(
                            id = R.string.days_text
                        )
                    }",
                    style = normalTextStyle(),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}