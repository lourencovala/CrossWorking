package ps.crossworking.screen.idea.feed

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme.colors
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
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel(),
    onUserClick: (userId: String) -> Unit,
    onIdeaClick: (ideaId: String) -> Unit
) {
    val feedState by remember { viewModel.feedState }
    val isRefreshing by remember { viewModel.isRefreshing }
    val errorMessageId by remember { viewModel.errorMessageId }

    DisposableEffect(key1 = Unit) {
        when (feedState) {
            is FeedState.Loading -> viewModel.getFeed()
            is FeedState.Success -> viewModel.cameFromUnknown()
            is FeedState.Error -> viewModel.getFeed()
            is FeedState.LeftScreen -> viewModel.cameBack()
        }
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
        indicator = { refreshState, trigger ->
            SwipeRefreshIndicator(
                // Pass the SwipeRefreshState + trigger through
                state = refreshState,
                refreshTriggerDistance = trigger,
                // Enable the scale animation
                scale = true,
                // Change the color and shape
                contentColor = colors.primary,
                backgroundColor = colors.background,
                shape = RoundedCornerShape(33.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (val value: FeedState = feedState) {
                is FeedState.Loading -> {
                    LoadingScreen()
                }

                is FeedState.Success -> {
                    FeedList(
                        feed = value.data,
                        handleLeave = { viewModel.handleLeave() },
                        onUserClick = onUserClick,
                        onIdeaClick = onIdeaClick
                    ) {
                        viewModel.getNextPage()
                    }
                }

                is FeedState.Error -> ErrorScreen(errorMessage = stringResource(id = value.errorMessageId))
            }
        }
    }
}

@Composable
fun FeedList(
    feed: List<ShortIdea>,
    handleLeave: () -> Boolean,
    onUserClick: (userId: String) -> Unit,
    onIdeaClick: (ideaId: String) -> Unit,
    fetchNextPage: () -> Unit
) {
    if (feed.isNotEmpty()) {
        val listState = rememberLazyListState()

        LazyColumn(contentPadding = PaddingValues(bottom = 12.dp), state = listState) {
            items(
                items = feed,
                key = { idea -> idea.ideaId }
            ) { idea ->
                SingleIdea(idea, handleLeave, onUserClick, onIdeaClick)
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
        EmptyListScreen(text = stringResource(id = R.string.empty_feed))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SingleIdea(
    idea: ShortIdea,
    handleLeave: () -> Boolean,
    onUserClick: (userId: String) -> Unit,
    onIdeaClick: (ideaId: String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 8.dp,
                start = 8.dp,
                end = 8.dp
            ),
        shape = RoundedCornerShape(33.dp),
        backgroundColor = colors.background,
        border = BorderStroke(1.dp, colors.outline),
        onClick = {
            if (handleLeave())
                onIdeaClick(idea.ideaId)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ClickableBorderedProfilePicture(
                        imageUrl = idea.user.profileImage!!,
                        size = 40.dp
                    ) {
                        if (handleLeave())
                            onUserClick(idea.user.userId)
                    }
                    Text(
                        text = idea.user.name!!,
                        style = mediumBoldMainColorTextStyle(),
                        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${idea.days}d",
                        style = mediumBoldMainColorTextStyle(),
                        modifier = Modifier.padding(top = 2.dp, end = 16.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 4.dp,
                        start = 12.dp,
                        end = 12.dp
                    ),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = idea.title,
                    style = mediumBoldMainColorTextStyle()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
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