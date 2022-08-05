package ps.crossworking.service

import ps.crossworking.dto.SkillCreateDto
import ps.crossworking.dto.SkillListDto
import retrofit2.Response
import retrofit2.http.*

interface SkillService {

    @POST("users/{userId}/skills")
    suspend fun addUserSkill(@Path("userId") userId: String, @Body skill: SkillCreateDto)

    @GET("users/{userId}/skills")
    suspend fun getUserSkills(@Path("userId") userId: String): SkillListDto

    @DELETE("users/{userId}/skills/{skillId}")
    suspend fun deleteUserSkill(
        @Path("userId") userId: String,
        @Path("skillId") skillId: String
    ): Response<Unit>

    @GET("ideas/{ideaId}/skills")
    suspend fun getIdeaSkill(@Path("ideaId") ideaId: String): SkillListDto

    @DELETE("ideas/{ideaId}/skills/{skillId}")
    suspend fun deleteIdeaSkill(
        @Path("ideaId") ideaId: String,
        @Path("skillId") skillId: String
    ): Response<Unit>

    @POST("ideas/{ideaId}/skills")
    suspend fun addIdeaSkill(@Path("ideaId") ideaId: String, @Body skill: SkillCreateDto)
}
