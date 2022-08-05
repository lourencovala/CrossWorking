package ps.crossworking.mockRepositories

import okhttp3.MediaType
import okhttp3.ResponseBody
import ps.crossworking.*
import ps.crossworking.dto.SkillCreateDto
import ps.crossworking.model.Category
import ps.crossworking.model.Skill
import ps.crossworking.repository.ISkillRepository
import retrofit2.HttpException
import retrofit2.Response

class SkillRepositoryMock: ISkillRepository {

    val mem = mutableMapOf("12345" to testUser)
    val memIdea = mutableMapOf(testIdea.ideaId to testIdea)

    var exception: Exception? = null

    fun prepareException(e: Exception) {
        exception = e
    }

    override suspend fun addUserSkill(userId: String, skillName: String, about: String, categoryId: String) {
        if (exception != null)
            throw exception as Exception
        mem[userId]
    }

    override suspend fun deleteUserSkill(userId: String, skillId: String) {
        if (exception != null)
            throw exception as Exception
    }

    override suspend fun getUserSkills(userId: String): List<Skill> {
        if (exception != null)
            throw exception as Exception
        return listOf(testSkill)
    }

    override suspend fun getCategories(): List<Category> {
        if (exception != null)
            throw exception as Exception
        return listOf(testCategory)
    }

    override suspend fun addIdeaSkill(ideaId: String, skillName: String, about: String, categoryId: String) {
        if (exception != null)
            throw exception as Exception
        memIdea[ideaId]
    }

    override suspend fun getIdeaSkills(ideaId: String): List<Skill> {
        if (exception != null)
            throw exception as Exception
        return listOf(testSkill)
    }

    override suspend fun deleteIdeaSkill(ideaId: String, skillId: String) {
        if (exception != null)
            throw exception as Exception
    }
}