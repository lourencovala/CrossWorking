package ps.crossworking.viewmodels

import junit.framework.TestCase
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

import ps.crossworking.screen.skill.manageIdea.ManageIdeaSkillsViewModel
import ps.crossworking.screen.skill.manageUser.DeleteUserSkillUseCase
import ps.crossworking.screen.skill.manageUser.GetUserSkillsUseCase
import ps.crossworking.screen.skill.manageUser.LoadState
import ps.crossworking.screen.skill.manageUser.ManageUserSkillsViewModel
import ps.crossworking.testSkill

@OptIn(ExperimentalCoroutinesApi::class)
class ManageUserSkillsViewModelTest {

    private lateinit var skillRepo: SkillRepositoryMock
    private lateinit var manageUserSkillsViewModel: ManageUserSkillsViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        skillRepo = SkillRepositoryMock()
        manageUserSkillsViewModel = ManageUserSkillsViewModel(
            GetUserSkillsUseCase(skillRepo),
            DeleteUserSkillUseCase(skillRepo)
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
            manageUserSkillsViewModel.getScreenInfo(userId = "test")
            advanceUntilIdle()
            TestCase.assertEquals(LoadState.Success::class, manageUserSkillsViewModel.state.value::class)
            TestCase.assertEquals(testSkill,(manageUserSkillsViewModel.state.value as LoadState.Success).data[0])
        }
    }

    @Test
    fun `exception while getting skills list` () {
        runTest {
            val exception = IdeaNotFoundException()
            skillRepo.prepareException(exception)
            manageUserSkillsViewModel.getScreenInfo(userId = "test")
            advanceUntilIdle()
            TestCase.assertEquals(LoadState.Error::class, manageUserSkillsViewModel.state.value::class)
            TestCase.assertEquals(errorConversion(exception),(manageUserSkillsViewModel.state.value as LoadState.Error).errorMessageId)
        }
    }

    @Test
    fun `exception while refreshing` () {
        runTest {
            val exception = IdeaNotFoundException()
            manageUserSkillsViewModel.getScreenInfo(userId = "test")
            advanceUntilIdle()
            skillRepo.prepareException(exception)
            manageUserSkillsViewModel.refreshing(userId = "test")
            advanceUntilIdle()
            TestCase.assertEquals(errorConversion(exception),manageUserSkillsViewModel.errorMessageId.value)
        }
    }

    @Test
    fun `exception while deleting skill`() {
        runTest {
            val exception = IdeaNotFoundException()
            advanceUntilIdle()
            skillRepo.prepareException(exception)
            manageUserSkillsViewModel.deleteSkill(userId = "test", skillId = "test")
            advanceUntilIdle()
            TestCase.assertEquals(errorConversion(exception),manageUserSkillsViewModel.errorMessageId.value)
        }
    }
}