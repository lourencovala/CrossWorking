package ps.crossworking.service

import ps.crossworking.dto.CategoryListDto
import retrofit2.http.GET

interface CategoryService {

    @GET("categories")
    suspend fun getCategories(): CategoryListDto
}
