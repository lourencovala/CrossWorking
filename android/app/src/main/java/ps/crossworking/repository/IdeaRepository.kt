package ps.crossworking.repository

import ps.crossworking.dto.IdeaCreateDto
import ps.crossworking.dto.IdeaUpdateDto
import ps.crossworking.exceptions.*
import ps.crossworking.model.Idea
import ps.crossworking.model.ShortIdea
import ps.crossworking.service.IdeaService
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

interface IIdeaRepository {

    suspend fun createIdea(
        userId: String,
        title: String,
        smallDescription: String,
        description: String
    ): Idea

    suspend fun getIdea(ideaId: String): Idea

    suspend fun editIdea(
        ideaId: String,
        title: String,
        smallDescription: String,
        description: String
    ): Idea

    suspend fun deleteIdea(ideaId: String)
    suspend fun getFeed(pageIndex: Int, pageSize: Int): List<ShortIdea>
    suspend fun getUserIdeas(userId: String, pageIndex: Int, pageSize: Int): List<ShortIdea>
}

class IdeaRepository @Inject constructor(
    private val service: IdeaService
) : IIdeaRepository {

    override suspend fun createIdea(
        userId: String,
        title: String,
        smallDescription: String,
        description: String
    ): Idea {
        try {
            return service.createIdea(userId, IdeaCreateDto(title, smallDescription, description))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        400 -> throw IdeaInvalidRequestException()
                        404 -> throw UserNotFoundException()
                        409 -> throw IdeaAlreadyExistsException()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw  ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun getIdea(ideaId: String): Idea {
        try {
            return service.getIdea(ideaId)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw IdeaNotFoundException()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw  ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun editIdea(
        ideaId: String,
        title: String,
        smallDescription: String,
        description: String
    ): Idea {
        try {
            return service.editIdea(ideaId, IdeaUpdateDto(title, smallDescription, description))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        400 -> throw IdeaInvalidRequestException()
                        404 -> throw IdeaNotFoundException()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw  ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun deleteIdea(ideaId: String) {
        try {
            service.deleteIdea(ideaId)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw IdeaNotFoundException()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw  ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun getFeed(pageIndex: Int, pageSize: Int): List<ShortIdea> {
        try {
            return service.getRecentIdeas(pageIndex, pageSize).ideas
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw IdeaNotFoundException()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw  ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun getUserIdeas(
        userId: String,
        pageIndex: Int,
        pageSize: Int
    ): List<ShortIdea> {
        try {
            return service.getUserIdeas(userId, pageIndex, pageSize).ideas
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw IdeaNotFoundException()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw  ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }
}
