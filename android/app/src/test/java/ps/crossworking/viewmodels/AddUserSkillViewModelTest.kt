package ps.crossworking.viewmodels

import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import ps.crossworking.common.UserInstance
import ps.crossworking.exceptions.SkillAlreadyExistsException
import ps.crossworking.exceptions.UnknownException
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.mockRepositories.SkillRepositoryMock
import ps.crossworking.screen.skill.GetCategoryUseCase
import ps.crossworking.screen.skill.addUserSkill.AddUserSkillUseCase
import ps.crossworking.screen.skill.addUserSkill.AddUserSkillViewModel
import ps.crossworking.screen.skill.addUserSkill.AddUserSkillsState
import ps.crossworking.testCategory
import ps.crossworking.testSkill
import ps.crossworking.testUser

@OptIn(ExperimentalCoroutinesApi::class)
class AddUserSkillViewModelTest {

    private lateinit var skillRepository: SkillRepositoryMock
    private lateinit var addUserSkillViewModel: AddUserSkillViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        skillRepository = SkillRepositoryMock()
        addUserSkillViewModel = AddUserSkillViewModel(
            GetCategoryUseCase(skillRepository),
            AddUserSkillUseCase(skillRepository),
        )

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get categories`() {
        runTest {
            addUserSkillViewModel.fetchCategories()
            advanceUntilIdle()
            TestCase.assertEquals(AddUserSkillsState.FetchedCategories::class, addUserSkillViewModel.state.value::class)
            TestCase.assertEquals(testCategory, (addUserSkillViewModel.state.value as AddUserSkillsState.FetchedCategories).categories[0])
        }
    }

    @Test
    fun `exception while getting categories`() {
        runTest {
            val exception = UnknownException()
            skillRepository.prepareException(exception)
            addUserSkillViewModel.fetchCategories()
            advanceUntilIdle()
            TestCase.assertEquals(AddUserSkillsState.Error::class, addUserSkillViewModel.state.value::class)
            TestCase.assertEquals(errorConversion(exception), (addUserSkillViewModel.state.value as AddUserSkillsState.Error).errorMessageId)
        }
    }

    @Test
    fun `add skill` () {
        runTest {
            UserInstance.user.value = testUser
            addUserSkillViewModel.addSkill( testSkill.name, testSkill.about!!, "test")
            advanceUntilIdle()
            TestCase.assertEquals(AddUserSkillsState.Added::class, addUserSkillViewModel.state.value::class)
        }
    }

    @Test
    fun `exception while adding skill` () {
        runTest {
            UserInstance.user.value = testUser
            val exception = SkillAlreadyExistsException()
            skillRepository.prepareException(exception)
            addUserSkillViewModel.addSkill( testSkill.name, testSkill.about!!, "test")
            advanceUntilIdle()
            TestCase.assertEquals(errorConversion(exception), addUserSkillViewModel.errorMessageId.value)
        }
    }
}