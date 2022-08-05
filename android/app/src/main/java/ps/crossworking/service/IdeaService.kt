package ps.crossworking.service

import ps.crossworking.dto.IdeaCreateDto
import ps.crossworking.dto.IdeaUpdateDto
import ps.crossworking.dto.ShortIdeaListDto
import ps.crossworking.model.Idea
import retrofit2.Response
import retrofit2.http.*

interface IdeaService {

    @POST("users/{userId}/ideas")
    suspend fun createIdea(@Path("userId") userId: String, @Body ideaInput: IdeaCreateDto): Idea

    @GET("ideas/{ideaId}")
    suspend fun getIdea(@Path("ideaId") ideaId: String): Idea

    @PUT("ideas/{ideaId}")
    suspend fun editIdea(@Path("ideaId") ideaId: String, @Body ideaUpdate: IdeaUpdateDto): Idea

    @DELETE("ideas/{ideaId}")
    suspend fun deleteIdea(@Path("ideaId") ideaId: String): Response<Unit>

    @GET("ideas")
    suspend fun getRecentIdeas(
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int
    ): ShortIdeaListDto

    @GET("users/{userId}/ideas")
    suspend fun getUserIdeas(
        @Path("userId") userId: String,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int
    ): ShortIdeaListDto
}
