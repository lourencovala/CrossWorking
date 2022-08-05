package ps.crossworking.dto

import java.util.*

/**
 * API representation for list of Categories.
 */
data class CategoryListApiOutput(
    val categories: List<CategoryApiOutput>
)

/**
 * API representation for Category output.
 */
data class CategoryApiOutput(
    val categoryId: UUID,
    val name: String,
)
