package ps.crossworking.repository

import ps.crossworking.dto.SkillCreateDto
import ps.crossworking.exceptions.*
import ps.crossworking.model.Category
import ps.crossworking.model.Skill
import ps.crossworking.service.CategoryService
import ps.crossworking.service.SkillService
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

interface ISkillRepository {
    suspend fun addUserSkill(userId: String, skillName: String, about: String, categoryId: String)
    suspend fun deleteUserSkill(userId: String, skillId: String)
    suspend fun getUserSkills(userId: String): List<Skill>
    suspend fun getCategories(): List<Category>
    suspend fun addIdeaSkill(ideaId: String, skillName: String, about: String, categoryId: String)
    suspend fun getIdeaSkills(ideaId: String): List<Skill>
    suspend fun deleteIdeaSkill(ideaId: String, skillId: String)
}

class SkillRepository @Inject constructor(
    private val serviceSkill: SkillService,
    private val serviceCategory: CategoryService
) : ISkillRepository {

    override suspend fun addUserSkill(
        userId: String,
        skillName: String,
        about: String,
        categoryId: String
    ) {
        try {
            return serviceSkill.addUserSkill(userId, SkillCreateDto(skillName, about, categoryId))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw UserNotFoundException()
                        409 -> throw  SkillAlreadyExistsException()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun deleteUserSkill(userId: String, skillId: String) {
        try {
            serviceSkill.deleteUserSkill(userId, skillId)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw SkillOrUserNotFoundException()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun getUserSkills(userId: String): List<Skill> {
        try {
            return serviceSkill.getUserSkills(userId).skills
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw UserNotFoundException()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun getCategories(): List<Category> {
        try {
            return serviceCategory.getCategories().categories
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun addIdeaSkill(
        ideaId: String,
        skillName: String,
        about: String,
        categoryId: String
    ) {
        try {
            serviceSkill.addIdeaSkill(ideaId, SkillCreateDto(skillName, about, categoryId))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw IdeaNotFoundException()
                        409 -> throw  SkillAlreadyExistsException()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun getIdeaSkills(ideaId: String): List<Skill> {
        try {
            return serviceSkill.getIdeaSkill(ideaId).skills
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw IdeaNotFoundException()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun deleteIdeaSkill(ideaId: String, skillId: String) {
        try {
            serviceSkill.deleteIdeaSkill(ideaId, skillId)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw SkillOrIdeaNotFoundException()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }
}