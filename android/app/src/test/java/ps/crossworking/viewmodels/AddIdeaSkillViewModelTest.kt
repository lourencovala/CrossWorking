package ps.crossworking.viewmodels

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import ps.crossworking.exceptions.SkillAlreadyExistsException
import ps.crossworking.exceptions.UnknownException
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.mockRepositories.SkillRepositoryMock
import ps.crossworking.screen.skill.GetCategoryUseCase
import ps.crossworking.screen.skill.addIdeaSkill.AddIdeaSkillUseCase
import ps.crossworking.screen.skill.addIdeaSkill.AddIdeaSkillViewModel
import ps.crossworking.screen.skill.addIdeaSkill.AddIdeaSkillsState
import ps.crossworking.testCategory
import ps.crossworking.testIdea
import ps.crossworking.testSkill

@OptIn(ExperimentalCoroutinesApi::class)
class AddIdeaSkillViewModelTest {
    private lateinit var skillRepository: SkillRepositoryMock
    private lateinit var addIdeaSkillViewModel: AddIdeaSkillViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        skillRepository = SkillRepositoryMock()
        addIdeaSkillViewModel = AddIdeaSkillViewModel(
            AddIdeaSkillUseCase(skillRepository),
            GetCategoryUseCase(skillRepository)
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
            addIdeaSkillViewModel.fetchCategories()
            advanceUntilIdle()
            assertEquals(AddIdeaSkillsState.FetchedCategories::class, addIdeaSkillViewModel.state.value::class)
            assertEquals(testCategory, (addIdeaSkillViewModel.state.value as AddIdeaSkillsState.FetchedCategories).categories[0])
        }
    }

    @Test
    fun `exception while getting categories`() {
        runTest {
            val exception = UnknownException()
            skillRepository.prepareException(exception)
            addIdeaSkillViewModel.fetchCategories()
            advanceUntilIdle()
            assertEquals(AddIdeaSkillsState.Error::class, addIdeaSkillViewModel.state.value::class)
            assertEquals(errorConversion(exception), (addIdeaSkillViewModel.state.value as AddIdeaSkillsState.Error).errorMessageId)
        }
    }

    @Test
    fun `add skill` () {
        runTest {
            addIdeaSkillViewModel.addSkill(testIdea.ideaId, testSkill.name, testSkill.about!!, "test")
            advanceUntilIdle()
            assertEquals(AddIdeaSkillsState.Added::class, addIdeaSkillViewModel.state.value::class)
        }
    }

    @Test
    fun `exception while adding skill` () {
        runTest {
            val exception = SkillAlreadyExistsException()
            skillRepository.prepareException(exception)
            addIdeaSkillViewModel.addSkill(testIdea.ideaId, testSkill.name, testSkill.about!!, "test")
            advanceUntilIdle()
            assertEquals(errorConversion(exception), addIdeaSkillViewModel.errorMessageId.value)
        }
    }
}