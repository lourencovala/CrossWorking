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
import ps.crossworking.screen.idea.userlist.GetUserIdeasUseCase
import ps.crossworking.screen.idea.userlist.ListState
import ps.crossworking.screen.idea.userlist.UserIdeaListViewModel
import ps.crossworking.testIdea
import ps.crossworking.testIdeaShort

@OptIn(ExperimentalCoroutinesApi::class)
class UserIdeaListViewModelTest {
    private lateinit var ideaRepo: IdeaRepositoryMock
    private lateinit var userIdeaListViewModel: UserIdeaListViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        ideaRepo = IdeaRepositoryMock()
        userIdeaListViewModel = UserIdeaListViewModel(
            GetUserIdeasUseCase(ideaRepo)
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
            userIdeaListViewModel.getList(testIdea.user.userId)
            advanceUntilIdle()
            assertEquals(ListState.Success::class, userIdeaListViewModel.listState.value::class)
            assertEquals(testIdeaShort, (userIdeaListViewModel.listState.value as ListState.Success).data[0])
        }
    }

    @Test
    fun `get feed first time with exception`() {
        runTest {
            val exception = IdeaNotFoundException()
            ideaRepo.startWithDate(ideaId = "test")
            ideaRepo.prepareException(exception)
            userIdeaListViewModel.getList(testIdea.user.userId)
            advanceUntilIdle()
            assertEquals(ListState.Error::class, userIdeaListViewModel.listState.value::class)
            assertEquals(errorConversion(exception), (userIdeaListViewModel.listState.value as ListState.Error).errorMessageId)
        }
    }

    @Test
    fun `refresh with exception`() {
        runTest {
            val exception = IdeaNotFoundException()
            ideaRepo.startWithDate(ideaId = "test")
            userIdeaListViewModel.getList(testIdea.user.userId)
            advanceUntilIdle()
            ideaRepo.prepareException(exception)
            userIdeaListViewModel.refresh(testIdea.user.userId)
            advanceUntilIdle()
            assertEquals(
                errorConversion(exception),
                userIdeaListViewModel.errorMessageId.value
            )
        }
    }

    @Test
    fun `get next page`() {
        runTest {
            ideaRepo.startWithDate(ideaId = "test")
            userIdeaListViewModel.getList(testIdea.user.userId)
            advanceUntilIdle()
            userIdeaListViewModel.getNextPage(testIdea.user.userId)
            assertEquals(ListState.Success::class, userIdeaListViewModel.listState.value::class)
            assertEquals(testIdeaShort, (userIdeaListViewModel.listState.value as ListState.Success).data[0])
        }
    }
}