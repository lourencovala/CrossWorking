package ps.crossworking.screen.idea.userlist

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
import ps.crossworking.model.ShortIdea
import ps.crossworking.ui.composables.*
import ps.crossworking.ui.theme.outline

@Composable
fun UserIdeaListScreen(
    viewModel: UserIdeaListViewModel = hiltViewModel(),
    userId: String,
    onIdeaClick: (ideaId: String) -> Unit
) {
    val listState by remember { viewModel.listState }
    val isRefreshing by remember { viewModel.isRefreshing }
    val updatedErrorMessageId by remember { viewModel.errorMessageId }

    DisposableEffect(key1 = Unit) {
        if (listState !is ListState.Success)
            viewModel.getList(userId)
        onDispose { }
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
        onRefresh = { viewModel.refresh(userId) },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val value: ListState = listState) {
                is ListState.Loading -> {
                    LoadingScreen()
                }

                is ListState.Success -> {
                    IdeaList(value.data, onIdeaClick) { viewModel.getNextPage(userId) }
                }

                is ListState.Error -> {
                    ErrorScreen(errorMessage = stringResource(id = value.errorMessageId))
                }
            }
        }
    }
}

@Composable
fun IdeaList(
    list: List<ShortIdea>,
    onIdeaClick: (ideaId: String) -> Unit,
    fetchNextPage: () -> Unit
) {
    if (list.isNotEmpty()) {
        val listState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
            state = listState
        ) {
            items(
                items = list,
                key = { idea -> idea.ideaId }
            ) { idea ->
                SmallIdeaWithoutUser(idea, onIdeaClick)
            }
        }

        val needFetchNextPage by remember {
            derivedStateOf {
                val layoutInfo = listState.layoutInfo
                val totalItemCount = layoutInfo.totalItemsCount
                val visibleItemCount = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

                visibleItemCount > totalItemCount - 3
            }
        }

        LaunchedEffect(needFetchNextPage) {
            if (needFetchNextPage)
                fetchNextPage()
        }
    } else {
        EmptyListScreen(text = stringResource(id = R.string.user_has_no_ideas))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SmallIdeaWithoutUser(idea: ShortIdea, onIdeaClick: (ideaId: String) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(
            bottom = 8.dp,
            start = 8.dp,
            end = 8.dp
        ),
        shape = RoundedCornerShape(33.dp),
        backgroundColor = MaterialTheme.colors.background,
        border = BorderStroke(1.dp, MaterialTheme.colors.outline),
        onClick = { onIdeaClick(idea.ideaId) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 12.dp,
                        start = 4.dp,
                        end = 4.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = idea.title,
                        style = mediumBoldMainColorTextStyle()
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${idea.days}d",
                        style = mediumBoldMainColorTextStyle()
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        end = 12.dp
                    ),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = idea.smallDescription,
                    style = normalTextStyle()
                )
            }

            SmallSkillDisplay(idea.skills)
        }
    }
}
