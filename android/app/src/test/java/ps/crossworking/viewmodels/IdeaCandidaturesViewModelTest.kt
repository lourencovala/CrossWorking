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
import ps.crossworking.mockRepositories.CandidatureRepositoryMock
import ps.crossworking.screen.idea.candidatures.*
import ps.crossworking.testCandidate

@OptIn(ExperimentalCoroutinesApi::class)
class IdeaCandidaturesViewModelTest {
    private lateinit var candidaturesRepo: CandidatureRepositoryMock
    private lateinit var ideaCandidaturesViewModel: IdeaCandidaturesViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        candidaturesRepo = CandidatureRepositoryMock()
        ideaCandidaturesViewModel = IdeaCandidaturesViewModel(
            GetIdeaCandidaturesUseCase(candidaturesRepo),
            UpdateCandidateUseCase(candidaturesRepo)
        )

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get idea candidates`() {
        runTest {
            candidaturesRepo.startWithDate(ideaId = "test")
            ideaCandidaturesViewModel.getIdeaCandidates(ideaId = "test")
            advanceUntilIdle()
            assertEquals(IdeaCandidaturesState.Success::class, ideaCandidaturesViewModel.state.value::class)
            assertEquals(testCandidate, (ideaCandidaturesViewModel.state.value as IdeaCandidaturesState.Success).data[0])
        }
    }

    @Test
    fun `exception while get idea candidates for first time`() {
        runTest {
            val exception = IdeaNotFoundException()
            candidaturesRepo.startWithDate(ideaId = "test")
            candidaturesRepo.prepareException(exception)
            ideaCandidaturesViewModel.getIdeaCandidates(ideaId = "test")
            advanceUntilIdle()
            assertEquals(IdeaCandidaturesState.Error::class, ideaCandidaturesViewModel.state.value::class)
            assertEquals(errorConversion(exception), (ideaCandidaturesViewModel.state.value as IdeaCandidaturesState.Error).errorMessageId)
        }
    }

    @Test
    fun `exception while refreshing idea candidates`() {
        runTest {
            val exception = IdeaNotFoundException()
            candidaturesRepo.startWithDate(ideaId = "test")
            ideaCandidaturesViewModel.getIdeaCandidates(ideaId = "test")
            advanceUntilIdle()
            candidaturesRepo.prepareException(exception)
            ideaCandidaturesViewModel.refresh(ideaId = "test")
            advanceUntilIdle()
            assertEquals(errorConversion(exception), ideaCandidaturesViewModel.errorMessageId.value)
        }
    }

    @Test
    fun `update candidate`() {
        runTest {
            candidaturesRepo.startWithDate(ideaId = "test")
            ideaCandidaturesViewModel.getIdeaCandidates(ideaId = "test")
            ideaCandidaturesViewModel.updateCandidate(ideaId = "test", candidateId = testCandidate.user.userId, true)
            advanceUntilIdle()
            assertEquals(IdeaCandidaturesState.Success::class, ideaCandidaturesViewModel.state.value::class)
            assertEquals("accepted", (ideaCandidaturesViewModel.state.value as IdeaCandidaturesState.Success).data[0].status)
        }
    }

    @Test
    fun `update candidate exception`() {
        runTest {
            val exception = IdeaNotFoundException()
            candidaturesRepo.startWithDate(ideaId = "test")
            ideaCandidaturesViewModel.getIdeaCandidates(ideaId = "test")
            advanceUntilIdle()
            candidaturesRepo.prepareException(exception)
            ideaCandidaturesViewModel.updateCandidate(ideaId = "test", candidateId = testCandidate.user.userId, true)
            advanceUntilIdle()
            assertEquals(IdeaCandidaturesState.Success::class, ideaCandidaturesViewModel.state.value::class)
            assertEquals(errorConversion(exception), ideaCandidaturesViewModel.errorMessageId.value)
        }
    }

}