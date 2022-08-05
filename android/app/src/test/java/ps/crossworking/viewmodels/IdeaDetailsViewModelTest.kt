package ps.crossworking.viewmodels

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import ps.crossworking.common.UserInstance
import ps.crossworking.exceptions.CandidateOrIdeaNotFoundException
import ps.crossworking.exceptions.IdeaNotFoundException
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.mockRepositories.CandidatureRepositoryMock
import ps.crossworking.mockRepositories.IdeaRepositoryMock
import ps.crossworking.screen.idea.GetIdeaUseCase
import ps.crossworking.screen.idea.details.*
import ps.crossworking.testCandidate
import ps.crossworking.testIdea

@OptIn(ExperimentalCoroutinesApi::class)
class IdeaDetailsViewModelTest {

    private lateinit var ideaRepo: IdeaRepositoryMock
    private lateinit var candidateRepo: CandidatureRepositoryMock
    private lateinit var ideaDetailsViewModel: IdeaDetailsViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        ideaRepo = IdeaRepositoryMock()
        candidateRepo = CandidatureRepositoryMock()
        ideaDetailsViewModel = IdeaDetailsViewModel(
            GetIdeaUseCase(ideaRepo),
            DeleteIdeaUseCase(ideaRepo),
            ApplyToIdeaUseCase(candidateRepo),
            GetCandidatureStatusUseCase(candidateRepo),
            UndoApplyIdeaUseCase(candidateRepo)
        )

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get idea` () {
        runTest {
            UserInstance.user.value = testIdea.user
            ideaRepo.startWithDate(testIdea.ideaId)
            ideaDetailsViewModel.getIdea(testIdea.ideaId)
            advanceUntilIdle()
            assertEquals(
                IdeaDetailsState.GotIdeaAndIsOwner::class,
                ideaDetailsViewModel.ideaState.value::class
            )
            assertEquals(
                testIdea,
                (ideaDetailsViewModel.ideaState.value as IdeaDetailsState.GotIdeaAndIsOwner).idea
            )

        }
    }

    @Test
    fun `get idea with exception`() {
        runTest {
            val exception = IdeaNotFoundException()
            ideaRepo.startWithDate(testIdea.ideaId)
            ideaRepo.prepareException(exception)
            ideaDetailsViewModel.getIdea(testIdea.ideaId)
            advanceUntilIdle()
            assertEquals(
                IdeaDetailsState.Error::class,
                ideaDetailsViewModel.ideaState.value::class
            )
            assertEquals(
                errorConversion(exception),
                (ideaDetailsViewModel.ideaState.value as IdeaDetailsState.Error).errorMessageId
            )
        }
    }

    @Test
    fun `is refreshing`() {
        runTest {
            ideaRepo.startWithDate(testIdea.ideaId)
            ideaDetailsViewModel.getIdea(testIdea.ideaId)
            advanceUntilIdle()
            ideaDetailsViewModel.refresh(testIdea.ideaId)
            advanceUntilIdle()
            assertFalse(ideaDetailsViewModel.isRefreshing.value)
        }
    }

    @Test
    fun `refreshing exception`() {
        runTest {
            val exception = IdeaNotFoundException()
            ideaRepo.startWithDate(testIdea.ideaId)
            ideaDetailsViewModel.getIdea(testIdea.ideaId)
            advanceUntilIdle()
            ideaRepo.prepareException(exception)
            ideaDetailsViewModel.refresh(testIdea.ideaId)
            advanceUntilIdle()
            assertEquals(errorConversion(exception), ideaDetailsViewModel.errorMessageId.value)
        }
    }

    @Test
    fun `delete idea`() {
        runTest {
            UserInstance.user.value = testIdea.user
            ideaRepo.startWithDate(testIdea.ideaId)
            ideaDetailsViewModel.getIdea(testIdea.ideaId)
            advanceUntilIdle()
            ideaDetailsViewModel.delete()
            advanceUntilIdle()
            assertEquals(
                IdeaDetailsState.Deleted::class,
                ideaDetailsViewModel.ideaState.value::class
            )
        }
    }

    @Test
    fun `delete idea exception`() {
        runTest {
            val exception = IdeaNotFoundException()
            UserInstance.user.value = testIdea.user
            ideaRepo.startWithDate(testIdea.ideaId)
            ideaRepo.prepareExceptionOnDelete(exception)
            ideaDetailsViewModel.getIdea(testIdea.ideaId)
            advanceUntilIdle()
            ideaDetailsViewModel.delete()
            advanceUntilIdle()
            assertEquals(
                errorConversion(exception),
                ideaDetailsViewModel.errorMessageId.value
            )
        }
    }

    @Test
    fun `apply to idea`() {
        runTest {
            UserInstance.user.value = testCandidate.user
            candidateRepo.startWithDate(testIdea.ideaId)
            ideaDetailsViewModel.applyToIdea(testIdea.ideaId)
            advanceUntilIdle()

        }
    }

    @Test
    fun `apply to idea exception`() {
        runTest {
            UserInstance.user.value = testCandidate.user
            val exception = CandidateOrIdeaNotFoundException()
            candidateRepo.startWithDate(testIdea.ideaId)
            candidateRepo.prepareException(exception)
            ideaDetailsViewModel.applyToIdea(testIdea.ideaId)
            advanceUntilIdle()
            assertEquals(
                errorConversion(exception),
                ideaDetailsViewModel.errorMessageId.value
            )
        }
    }

    @Test
    fun `undo apply`() {
        runTest {
            UserInstance.user.value = testCandidate.user
            candidateRepo.startWithDate(testIdea.ideaId)
            ideaDetailsViewModel.undoApply(testIdea.ideaId)
            advanceUntilIdle()

        }
    }

    @Test
    fun `undo apply to idea exception`() {
        runTest {
            UserInstance.user.value = testCandidate.user
            val exception = CandidateOrIdeaNotFoundException()
            candidateRepo.startWithDate(testIdea.ideaId)
            candidateRepo.prepareException(exception)
            ideaDetailsViewModel.undoApply(testIdea.ideaId)
            advanceUntilIdle()
            assertEquals(
                errorConversion(exception),
                ideaDetailsViewModel.errorMessageId.value
            )
        }
    }
}