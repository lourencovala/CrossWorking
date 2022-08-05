package ps.crossworking.repository

import ps.crossworking.dto.CandidatureCreateDto
import ps.crossworking.dto.CandidatureUpdateDto
import ps.crossworking.exceptions.*
import ps.crossworking.model.Candidate
import ps.crossworking.model.Candidature
import ps.crossworking.model.CandidatureResult
import ps.crossworking.service.CandidatureService
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

interface ICandidatureRepository {
    suspend fun applyToIdea(userId: String, ideaId: String): Candidature
    suspend fun getResults(userId: String, pageIndex: Int, pageSize: Int): List<CandidatureResult>
    suspend fun getIdeaCandidates(ideaId: String, pageIndex: Int, pageSize: Int): List<Candidate>
    suspend fun updateCandidate(ideaId: String, candidateId: String, isAccepted: Boolean): Candidate
    suspend fun getCandidatureStatus(userId: String, ideaId: String): Candidature?
    suspend fun undoApply(userId: String, ideaId: String)
}

class CandidatureRepository @Inject constructor(
    val service: CandidatureService
) : ICandidatureRepository {

    override suspend fun applyToIdea(userId: String, ideaId: String): Candidature {
        try {
            return service.applyToIdea(userId, CandidatureCreateDto(ideaId))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        400 -> throw RequestApiException()
                        404 -> throw CandidateOrIdeaNotFoundException()
                        409 -> throw AlreadyCandidateException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun getResults(
        userId: String,
        pageIndex: Int,
        pageSize: Int
    ): List<CandidatureResult> {
        try {
            return service.getResults(userId, pageIndex, pageSize).results
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

    override suspend fun getIdeaCandidates(
        ideaId: String,
        pageIndex: Int,
        pageSize: Int
    ): List<Candidate> {
        try {
            return service.getIdeaCandidates(ideaId, pageIndex, pageSize).candidates
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw  IdeaNotFoundException()
                        in 400..499 -> throw  RequestApiException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw  UnknownException()
                    }
                }
                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }

    }

    override suspend fun updateCandidate(
        ideaId: String,
        candidateId: String,
        isAccepted: Boolean
    ): Candidate {
        try {
            return service.updateCandidate(
                ideaId,
                candidateId,
                CandidatureUpdateDto(if (isAccepted) "accepted" else "declined")
            )
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw CandidateOrIdeaNotFoundException()
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

    override suspend fun getCandidatureStatus(userId: String, ideaId: String): Candidature? {
        try {
            return service.getCandidatureStatus(userId, ideaId)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> return null
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

    override suspend fun undoApply(userId: String, ideaId: String) {
        try {
            service.undoApply(userId, ideaId)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw CandidateOrIdeaNotFoundException()
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
