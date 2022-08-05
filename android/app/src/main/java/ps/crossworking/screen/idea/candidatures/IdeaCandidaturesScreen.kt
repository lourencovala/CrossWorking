package ps.crossworking.screen.idea.candidatures

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
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
import ps.crossworking.model.Candidate
import ps.crossworking.ui.composables.*
import ps.crossworking.ui.theme.buttonText
import ps.crossworking.ui.theme.outline

@Composable
fun IdeaCandidatesScreen(
    viewModel: IdeaCandidaturesViewModel = hiltViewModel(),
    ideaId: String,
    onUserClick: (String) -> Unit
) {
    val candidatesState by remember { viewModel.state }
    val isRefreshing by remember { viewModel.isRefreshing }
    val errorMessageId by remember { viewModel.errorMessageId }

    DisposableEffect(key1 = Unit) {
        if (candidatesState !is IdeaCandidaturesState.Success)
            viewModel.getIdeaCandidates(ideaId)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
        ) {
            when (val value: IdeaCandidaturesState = candidatesState) {
                is IdeaCandidaturesState.Loading -> {
                    LoadingScreen()
                }
                is IdeaCandidaturesState.Success -> {
                    IdeaCandidates(viewModel, ideaId, value.data, onUserClick) {
                        viewModel.getNextPage(ideaId)
                    }
                }
                is IdeaCandidaturesState.Error -> ErrorScreen(errorMessage = stringResource(id = value.errorMessageId))
            }
        }
    }
}

@Composable
fun IdeaCandidates(
    viewModel: IdeaCandidaturesViewModel,
    ideaId: String,
    candidates: List<Candidate>,
    onUserClick: (String) -> Unit,
    fetchNextPage: () -> Unit
) {
    if (candidates.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val listState = rememberLazyListState()

            LazyColumn(
                contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                items(
                    items = candidates,
                    key = { candidate -> candidate.user.userId }
                ) { candidate ->
                    IdeaCandidatesEntry(viewModel, ideaId, candidate, onUserClick)
                }
            }

            val needFetchNextPage by remember {
                derivedStateOf {
                    val layoutInfo = listState.layoutInfo
                    val totalItemCount = layoutInfo.totalItemsCount
                    val visibleItemCount =
                        (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

                    visibleItemCount > totalItemCount - 3
                }
            }

            LaunchedEffect(needFetchNextPage) {
                if (needFetchNextPage)
                    fetchNextPage()
            }
        }
    } else {
        EmptyListScreen(text = stringResource(id = R.string.no_candidatures))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IdeaCandidatesEntry(
    viewModel: IdeaCandidaturesViewModel,
    ideaId: String,
    candidate: Candidate,
    onUserClick: (String) -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.padding(
            start = 8.dp,
            end = 8.dp,
            bottom = 8.dp
        ),
        border = BorderStroke(1.dp, color = MaterialTheme.colors.outline),
        shape = RoundedCornerShape(33.dp),
        backgroundColor = MaterialTheme.colors.background,
        onClick = { onUserClick(candidate.user.userId) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 20.dp,
                    bottom = 20.dp,
                    start = 20.dp,
                    end = 20.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ClickableBorderedProfilePicture(
                            imageUrl = candidate.user.profileImage!!,
                            size = 40.dp
                        ) { onUserClick(candidate.user.userId) }
                        Text(
                            text = candidate.user.name!!,
                            style = mediumBoldMainColorTextStyle(),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "${stringResource(R.string.days_since_created_text)} ${candidate.daysSinceCreatedDate} ${
                            stringResource(
                                id = R.string.days_ago_text
                            )
                        }",
                        style = normalTextStyle(),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    when (candidate.status) {
                        "pending" -> {
                            Button(
                                onClick = {
                                    viewModel.updateCandidate(
                                        ideaId,
                                        candidate.user.userId,
                                        true
                                    )
                                },
                                shape = RoundedCornerShape(32.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.accept_candidate_button),
                                    style = normalTextStyle()
                                )
                            }
                            Spacer(modifier = Modifier.size(12.dp))
                            Button(
                                onClick = {
                                    viewModel.updateCandidate(
                                        ideaId,
                                        candidate.user.userId,
                                        false
                                    )
                                },
                                shape = RoundedCornerShape(32.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.decline_candidate_button),
                                    style = normalTextStyle()
                                )
                            }
                        }
                        "accepted" -> {
                            Row {
                                Button(
                                    onClick = {
                                        context.startActivity(Intent(Intent.ACTION_SENDTO).apply {
                                            data = Uri.parse("mailto:")
                                            putExtra(
                                                Intent.EXTRA_EMAIL,
                                                arrayOf(candidate.user.email)
                                            )
                                        })
                                    },
                                    shape = RoundedCornerShape(32.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Email,
                                        contentDescription = "email",
                                        tint = MaterialTheme.colors.buttonText
                                    )
                                }
                            }
                        }
                        "declined" -> {
                            Text(
                                text = stringResource(R.string.candidature_declined),
                                style = normalTextStyle()
                            )
                        }
                    }
                }
            }
        }
    }
}
