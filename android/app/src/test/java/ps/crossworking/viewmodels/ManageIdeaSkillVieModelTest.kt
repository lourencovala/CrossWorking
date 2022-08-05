package ps.crossworking.viewmodels

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import ps.crossworking.exceptions.IdeaNotFoundException
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.mockRepositories.SkillRepositoryMock
import ps.crossworking.screen.skill.manageIdea.DeleteIdeaSkillUseCase
import ps.crossworking.screen.skill.manageIdea.GetIdeaSkillsUseCase
import ps.crossworking.screen.skill.manageIdea.LoadState
import ps.crossworking.screen.skill.manageIdea.ManageIdeaSkillsViewModel
import ps.crossworking.testSkill

@OptIn(ExperimentalCoroutinesApi::class)
class ManageIdeaSkillVieModelTest {

    private lateinit var skillRepo: SkillRepositoryMock
    private lateinit var manageIdeaSkillVieModel: ManageIdeaSkillsViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        skillRepo = SkillRepositoryMock()
        manageIdeaSkillVieModel = ManageIdeaSkillsViewModel(
            GetIdeaSkillsUseCase(skillRepo),
            DeleteIdeaSkillUseCase(skillRepo)
        )

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get skills list` () {
        runTest {
            manageIdeaSkillVieModel.getScreenInfo(ideaId = "test")
            advanceUntilIdle()
            assertEquals(LoadState.Success::class, manageIdeaSkillVieModel.state.value::class)
            assertEquals(testSkill,(manageIdeaSkillVieModel.state.value as LoadState.Success).data[0])
        }
    }

    @Test
    fun `exception while getting skills list` () {
        runTest {
            val exception = IdeaNotFoundException()
            skillRepo.prepareException(exception)
            manageIdeaSkillVieModel.getScreenInfo(ideaId = "test")
            advanceUntilIdle()
            assertEquals(LoadState.Error::class, manageIdeaSkillVieModel.state.value::class)
            assertEquals(errorConversion(exception),(manageIdeaSkillVieModel.state.value as LoadState.Error).errorMessageId)
        }
    }

    @Test
    fun `exception while refreshing` () {
        runTest {
            val exception = IdeaNotFoundException()
            manageIdeaSkillVieModel.getScreenInfo(ideaId = "test")
            advanceUntilIdle()
            skillRepo.prepareException(exception)
            manageIdeaSkillVieModel.refreshing(ideaId = "test")
            advanceUntilIdle()
            assertEquals(errorConversion(exception),manageIdeaSkillVieModel.errorMessageId.value)
        }
    }

    @Test
    fun `exception while deleting skill`() {
        runTest {
            val exception = IdeaNotFoundException()
            advanceUntilIdle()
            skillRepo.prepareException(exception)
            manageIdeaSkillVieModel.deleteSkill(ideaId = "test", skillId = "test")
            advanceUntilIdle()
            assertEquals(errorConversion(exception),manageIdeaSkillVieModel.errorMessageId.value)
        }
    }
}