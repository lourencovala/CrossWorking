package ps.crossworking.service

import ps.crossworking.dto.CandidateListDto
import ps.crossworking.dto.CandidatureCreateDto
import ps.crossworking.dto.CandidatureResultListDto
import ps.crossworking.dto.CandidatureUpdateDto
import ps.crossworking.model.Candidate
import ps.crossworking.model.Candidature
import retrofit2.Response
import retrofit2.http.*

interface CandidatureService {

    @POST("users/{userId}/candidatures")
    suspend fun applyToIdea(
        @Path("userId") userId: String,
        @Body body: CandidatureCreateDto
    ): Candidature

    @GET("users/{userId}/results")
    suspend fun getResults(
        @Path("userId") userId: String,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int
    ): CandidatureResultListDto

    @GET("ideas/{ideaId}/candidates")
    suspend fun getIdeaCandidates(
        @Path("ideaId") ideaId: String,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int
    ): CandidateListDto

    @PUT("ideas/{ideaId}/candidates/{candidateId}")
    suspend fun updateCandidate(
        @Path("ideaId") ideaId: String,
        @Path("candidateId") candidateId: String,
        @Body status: CandidatureUpdateDto
    ): Candidate

    @GET("users/{userId}/candidatures/{ideaId}")
    suspend fun getCandidatureStatus(
        @Path("userId") userId: String,
        @Path("ideaId") ideaId: String
    ): Candidature

    @DELETE("users/{userId}/candidatures/{ideaId}")
    suspend fun undoApply(
        @Path("userId") userId: String,
        @Path("ideaId") ideaId: String
    ): Response<Unit>
}
