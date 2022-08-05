package ps.crossworking.screen.user.results

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import ps.crossworking.model.CandidatureResult
import ps.crossworking.ui.composables.*
import ps.crossworking.ui.theme.outline

@Composable
fun ResultsScreen(
    viewModel: ResultsViewModel = hiltViewModel(),
    onIdeaClick: (ideaId: String) -> Unit
) {
    val resultsState by remember { viewModel.resultsState }
    val isRefreshing by remember { viewModel.isRefreshing }
    val errorMessageId by remember { viewModel.errorMessageId }

    DisposableEffect(key1 = Unit) {
        if (resultsState !is ResultsState.Success)
            viewModel.getResults()
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
        onRefresh = { viewModel.refresh() },
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
        },
        modifier = Modifier.fillMaxSize()
    ) {
        when (val value: ResultsState = resultsState) {
            is ResultsState.Loading -> {
                LoadingScreen()
            }
            is ResultsState.Success -> {
                ResultsList(value.data, onIdeaClick) {
                    viewModel.getNextPage()
                }
            }
            is ResultsState.Error -> {
                ErrorScreen(errorMessage = stringResource(id = value.errorMessageId))
            }
        }
    }
}

@Composable
fun ResultsList(
    results: List<CandidatureResult>,
    onIdeaClick: (ideaId: String) -> Unit,
    fetchNextPage: () -> Unit
) {
    if (results.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val listState = rememberLazyListState()

            LazyColumn(
                contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
            ) {
                items(
                    items = results,
                    key = { result -> result.idea.ideaId }
                ) { result ->
                    ResultEntry(result, onIdeaClick)
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
fun ResultEntry(result: CandidatureResult, onIdeaClick: (ideaId: String) -> Unit) {
    Card(
        modifier = Modifier.padding(
            start = 8.dp,
            end = 8.dp,
            bottom = 8.dp
        ),
        border = BorderStroke(1.dp, color = MaterialTheme.colors.outline),
        shape = RoundedCornerShape(33.dp),
        backgroundColor = MaterialTheme.colors.background,
        onClick = { onIdeaClick(result.idea.ideaId) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 12.dp,
                    bottom = 12.dp,
                    start = 12.dp,
                    end = 12.dp
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = result.idea.title,
                    style = mediumMainColorTextStyle()
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when (result.status) {
                        "accepted" -> stringResource(id = R.string.accepted_text)
                        "rejected" -> stringResource(id = R.string.rejected_text)
                        else -> stringResource(id = R.string.pending_text)
                    } + ", ${result.daysSinceLastUpdate} ${
                        stringResource(
                            id = R.string.days_text
                        )
                    }",
                    style = normalTextStyle(),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
