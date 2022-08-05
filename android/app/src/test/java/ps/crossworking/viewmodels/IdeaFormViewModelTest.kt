package ps.crossworking.viewmodels

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import ps.crossworking.common.UserInstance
import ps.crossworking.exceptions.IdeaAlreadyExistsException
import ps.crossworking.exceptions.IdeaNotFoundException
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.mockRepositories.IdeaRepositoryMock
import ps.crossworking.model.Idea
import ps.crossworking.screen.idea.GetIdeaUseCase
import ps.crossworking.screen.idea.form.CreateIdeaUseCase
import ps.crossworking.screen.idea.form.EditIdeaUseCase
import ps.crossworking.screen.idea.form.IdeaFormState
import ps.crossworking.screen.idea.form.IdeaFormViewModel
import ps.crossworking.testIdea

@OptIn(ExperimentalCoroutinesApi::class)
class IdeaFormViewModelTest {

    private lateinit var ideaRepo: IdeaRepositoryMock
    private lateinit var ideaFormViewModel: IdeaFormViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        ideaRepo = IdeaRepositoryMock()
        ideaFormViewModel = IdeaFormViewModel(
            CreateIdeaUseCase(ideaRepo),
            GetIdeaUseCase(ideaRepo),
            EditIdeaUseCase(ideaRepo)
        )

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test get idea`() {
        runTest {
            ideaRepo.startWithDate(ideaId = "test")
            ideaFormViewModel.getIdea(ideaId = "test")
            advanceUntilIdle()
            assertEquals(IdeaFormState.GotIdea::class, ideaFormViewModel.ideaFormState.value::class)
            assertEquals(testIdea, (ideaFormViewModel.ideaFormState.value as IdeaFormState.GotIdea).idea)
        }
    }

    @Test
    fun `exception when get idea`() {
        runTest {
            val exception = IdeaNotFoundException()
            ideaRepo.startWithDate(ideaId = "test")
            ideaRepo.prepareException(exception)
            ideaFormViewModel.getIdea(ideaId = "test")
            advanceUntilIdle()
            assertEquals(IdeaFormState.Error::class, ideaFormViewModel.ideaFormState.value::class)
            assertEquals(
                errorConversion(exception),
                (ideaFormViewModel.ideaFormState.value as IdeaFormState.Error).errorMessageId
            )
        }
    }

    @Test
    fun `create idea`() {
        runTest {
            UserInstance.user.value = testIdea.user
            ideaFormViewModel.createIdea(testIdea.title, testIdea.smallDescription, testIdea.description)
            advanceUntilIdle()
            assertEquals(IdeaFormState.CreatedIdea::class, ideaFormViewModel.ideaFormState.value::class)
            assertEquals(testIdea, (ideaFormViewModel.ideaFormState.value as IdeaFormState.CreatedIdea).idea )
        }
    }

    @Test
    fun `exception creating idea`() {
        runTest {
            val exception = IdeaAlreadyExistsException()
            UserInstance.user.value = testIdea.user
            ideaRepo.prepareException(exception)
            ideaFormViewModel.createIdea(testIdea.title, testIdea.smallDescription, testIdea.description)
            advanceUntilIdle()
            assertEquals(errorConversion(exception), ideaFormViewModel.errorMessageId.value)
        }
    }

    @Test
    fun `edit idea`() {
        runTest {
            ideaRepo.startWithDate(ideaId = "test")
            ideaFormViewModel.getIdea(ideaId = "test")
            advanceUntilIdle()
            ideaFormViewModel.editIdea("edited", testIdea.smallDescription, testIdea.description)
            advanceUntilIdle()
            assertEquals(IdeaFormState.EditedIdea::class, ideaFormViewModel.ideaFormState.value::class)
            assertEquals(
                Idea(
                    testIdea.ideaId,
                    "edited",
                    testIdea.smallDescription,
                    testIdea.description,
                    testIdea.user,
                    testIdea.days,
                    testIdea.skills)
                ,
                (ideaFormViewModel.ideaFormState.value as IdeaFormState.EditedIdea).idea
            )
        }
    }

    @Test
    fun `exception editing idea`() {
        runTest {
            val exception = IdeaNotFoundException()
            ideaRepo.startWithDate(ideaId = "test")
            ideaFormViewModel.getIdea(ideaId = "test")
            advanceUntilIdle()
            ideaRepo.prepareException(exception)
            ideaFormViewModel.editIdea("edited", testIdea.smallDescription, testIdea.description)
            advanceUntilIdle()
            assertEquals(errorConversion(exception), ideaFormViewModel.errorMessageId.value)
        }
    }

    @Test
    fun `restore start state`() {
        runTest {
            ideaFormViewModel.restoreInitialState()
            assertEquals(IdeaFormState.Loading::class, ideaFormViewModel.ideaFormState.value::class)
        }
    }

}