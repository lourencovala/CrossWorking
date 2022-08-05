package ps.crossworking.usecases

import junit.framework.Assert
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import ps.crossworking.exceptions.IdeaSmallDescriptionTooLongException
import ps.crossworking.exceptions.IdeaTitleTooLongException
import ps.crossworking.mockRepositories.IdeaRepositoryMock
import ps.crossworking.model.ShortIdea
import ps.crossworking.screen.idea.form.CreateIdeaUseCase
import ps.crossworking.screen.idea.form.EditIdeaUseCase
import ps.crossworking.screen.idea.userlist.GetUserIdeasUseCase
import ps.crossworking.testIdea
import ps.crossworking.testIdeaShort

class EditIdeaUseCaseTest {
    private lateinit var editIdeaUseCase: EditIdeaUseCase
    private lateinit var repo: IdeaRepositoryMock

    @Before
    fun setUp() {
        repo = IdeaRepositoryMock()
        repo.startWithDate(testIdea.ideaId)
        editIdeaUseCase = EditIdeaUseCase(repo)
    }

    @Test
    fun `edit idea`() {
        runBlocking {
            assertEquals(testIdea, editIdeaUseCase(testIdea.ideaId, testIdea.title, testIdea.smallDescription, testIdea.description))
        }

    }

    @Test(expected = IdeaTitleTooLongException::class)
    fun `create idea with title too long`() {
        runBlocking {
            assertEquals(testIdea,
                editIdeaUseCase(
                    testIdea.user.userId,
                    "this is a title too long throw exception",
                    testIdea.smallDescription,
                    testIdea.description
                )
            )
        }
    }

    @Test(expected = IdeaSmallDescriptionTooLongException::class)
    fun `create idea with small description too long`() {
        runBlocking {
            assertEquals(testIdea,
                editIdeaUseCase(
                    testIdea.user.userId,
                    testIdea.title,
                    "this is a very long description that will " +
                            "make use case throw exception",
                    testIdea.description
                )
            )
        }
    }
}