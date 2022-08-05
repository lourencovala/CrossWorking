package ps.crossworking.viewmodels

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import ps.crossworking.exceptions.IdeaNotFoundException
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.mockRepositories.IdeaRepositoryMock
import ps.crossworking.screen.idea.feed.FeedState
import ps.crossworking.screen.idea.feed.FeedViewModel
import ps.crossworking.screen.idea.feed.GetFeedUseCase
import ps.crossworking.testIdeaShort

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    private lateinit var ideaRepo: IdeaRepositoryMock
    private lateinit var feedViewModel: FeedViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        ideaRepo = IdeaRepositoryMock()
        feedViewModel = FeedViewModel(
            GetFeedUseCase(ideaRepo)
        )

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get feed first time`() {
        runTest {
            ideaRepo.startWithDate(ideaId = "test")
            feedViewModel.getFeed()
            advanceUntilIdle()
            assertEquals(FeedState.Success::class, feedViewModel.feedState.value::class)
            assertEquals(testIdeaShort, (feedViewModel.feedState.value as FeedState.Success).data[0])
        }
    }

    @Test
    fun `get feed first time with exception`() {
        runTest {
            val exception = IdeaNotFoundException()
            ideaRepo.startWithDate(ideaId = "test")
            ideaRepo.prepareException(exception)
            feedViewModel.getFeed()
            advanceUntilIdle()
            assertEquals(FeedState.Error::class, feedViewModel.feedState.value::class)
            assertEquals(
                errorConversion(exception),
                (feedViewModel.feedState.value as FeedState.Error).errorMessageId
            )
        }
    }

    @Test
    fun `refresh with exception`() {
        runTest {
            val exception = IdeaNotFoundException()
            ideaRepo.startWithDate(ideaId = "test")
            feedViewModel.getFeed()
            advanceUntilIdle()
            ideaRepo.prepareException(exception)
            feedViewModel.refresh()
            advanceUntilIdle()
            assertEquals(
                errorConversion(exception),
                feedViewModel.errorMessageId.value
            )
        }
    }

    @Test
    fun `get next page`() {
        runTest {
            ideaRepo.startWithDate(ideaId = "test")
            feedViewModel.getFeed()
            advanceUntilIdle()
            feedViewModel.getNextPage()
            assertEquals(FeedState.Success::class, feedViewModel.feedState.value::class)
            assertEquals(testIdeaShort, (feedViewModel.feedState.value as FeedState.Success).data[0])
        }
    }

}