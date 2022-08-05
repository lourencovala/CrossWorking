package ps.crossworking.viewmodels

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import ps.crossworking.common.UserInstance
import ps.crossworking.exceptions.UserNotFoundException
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.mockRepositories.CandidatureRepositoryMock
import ps.crossworking.screen.user.results.GetResultsUseCase
import ps.crossworking.screen.user.results.ResultsState
import ps.crossworking.screen.user.results.ResultsViewModel
import ps.crossworking.testCandidature
import ps.crossworking.testUser

@OptIn(ExperimentalCoroutinesApi::class)
class ResultsViewModelTest {
    private lateinit var candidatureRepo: CandidatureRepositoryMock
    private lateinit var resultsViewModel: ResultsViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        candidatureRepo = CandidatureRepositoryMock()
        resultsViewModel =  ResultsViewModel(
            GetResultsUseCase(candidatureRepo)
        )

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get results`() {
        runTest {
            UserInstance.user.value = testUser
            resultsViewModel.getResults()
            advanceUntilIdle()
            assertEquals(ResultsState.Success::class, resultsViewModel.resultsState.value::class)
            assertEquals(testCandidature, (resultsViewModel.resultsState.value as ResultsState.Success).data[0])
        }
    }

    @Test
    fun `exception while getting results`() {
        runTest {
            val exception = UserNotFoundException()
            UserInstance.user.value = testUser
            candidatureRepo.prepareException(exception)
            resultsViewModel.getResults()
            advanceUntilIdle()
            assertEquals(ResultsState.Error::class, resultsViewModel.resultsState.value::class)
            assertEquals(errorConversion(exception), (resultsViewModel.resultsState.value as ResultsState.Error).errorMessageId)
        }
    }

    @Test
    fun `exception while refreshing results`() {
        runTest {
            val exception = UserNotFoundException()
            UserInstance.user.value = testUser
            resultsViewModel.getResults()
            advanceUntilIdle()
            candidatureRepo.prepareException(exception)
            resultsViewModel.refresh()
            advanceUntilIdle()
            assertEquals(errorConversion(exception), resultsViewModel.errorMessageId.value)
        }
    }
}