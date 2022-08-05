package ps.crossworking.mockRepositories

import okhttp3.MediaType
import okhttp3.ResponseBody
import ps.crossworking.errorException
import ps.crossworking.exceptions.IdeaNotFoundException
import ps.crossworking.model.Candidature
import ps.crossworking.model.Idea
import ps.crossworking.model.ShortIdea
import ps.crossworking.model.User
import ps.crossworking.repository.IIdeaRepository
import ps.crossworking.testIdea
import retrofit2.HttpException
import retrofit2.Response

class IdeaRepositoryMock: IIdeaRepository {

    val mem = HashMap<String, Idea>()

    private var excep: Exception? = null

    private var exceptionOnDelete = false

    fun startWithDate(ideaId: String) {
        mem[ideaId] = testIdea
    }

    fun prepareException(exception: Exception) {
        excep = exception
    }

    fun prepareExceptionOnDelete(exception: Exception ) {
        exceptionOnDelete = true
        excep = exception
    }
    override suspend fun createIdea(
        userId: String,
        title: String,
        smallDescription: String,
        description: String
    ): Idea {
        if (excep != null && !exceptionOnDelete)
            throw excep as Exception
        mem[testIdea.ideaId] = Idea(testIdea.ideaId, title, smallDescription, description, testIdea.user,10, emptyList())
        return mem[testIdea.ideaId]!!
    }

    override suspend fun getIdea(ideaId: String): Idea {
        if (excep != null && !exceptionOnDelete)
            throw excep as Exception
        return mem[ideaId]!!
    }

    override suspend fun editIdea(
        ideaId: String,
        title: String,
        smallDescription: String,
        description: String
    ): Idea {
        if (excep != null && !exceptionOnDelete)
            throw excep as Exception
        val elem = mem[ideaId] ?: throw errorException

        mem[ideaId] = Idea(ideaId, title, smallDescription, description, elem.user, elem.days, elem.skills)
        return mem[ideaId]!!
    }

    override suspend fun deleteIdea(ideaId: String) {
        if (exceptionOnDelete) {
            throw IdeaNotFoundException()
        }
        mem.remove(ideaId)
    }

    override suspend fun getFeed(pageIndex: Int, pageSize: Int): List<ShortIdea> {
        if (excep != null && !exceptionOnDelete)
            throw excep as Exception
        return mem.map {
            ShortIdea(it.value.ideaId, it.value.title, it.value.smallDescription, it.value.days, it.value.user, it.value.skills)
        }
    }

    override suspend fun getUserIdeas(userId: String, pageIndex: Int, pageSize: Int): List<ShortIdea> {
        if (excep != null && !exceptionOnDelete)
            throw excep as Exception
        return mem.map {
            ShortIdea(it.value.ideaId, it.value.title, it.value.smallDescription, it.value.days, it.value.user, it.value.skills)
        }
    }
}