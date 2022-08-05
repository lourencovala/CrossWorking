package ps.crossworking.service

import ps.crossworking.dto.UserCreateDto
import ps.crossworking.dto.UserUpdateDto
import ps.crossworking.model.User
import retrofit2.http.*

interface UserService {

    @POST("users")
    suspend fun createUser(@Body user: UserCreateDto): User

    @PUT("users/{userId}")
    suspend fun addNameAndPict(@Path("userId") userId: String, @Body user: UserUpdateDto): User

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): User
}
