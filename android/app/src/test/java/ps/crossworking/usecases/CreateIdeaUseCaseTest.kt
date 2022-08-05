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
import ps.crossworking.screen.idea.userlist.GetUserIdeasUseCase
import ps.crossworking.testIdea
import ps.crossworking.testIdeaShort

class CreateIdeaUseCaseTest {
    private lateinit var createIdeaUseCase: CreateIdeaUseCase
    private lateinit var repo: IdeaRepositoryMock

    @Before
    fun setUp() {
        repo = IdeaRepositoryMock()
        repo.startWithDate(testIdea.ideaId)
        createIdeaUseCase = CreateIdeaUseCase(repo)
    }

    @Test
    fun `create idea`() {
        runBlocking {
            assertEquals(testIdea, createIdeaUseCase(testIdea.user.userId, testIdea.title, testIdea.smallDescription, testIdea.description))
        }

    }

    @Test(expected = IdeaTitleTooLongException::class)
    fun `create idea with title too long`() {
        runBlocking {
            assertEquals(testIdea,
                createIdeaUseCase(
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
                createIdeaUseCase(
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