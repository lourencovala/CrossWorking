package ps.crossworking.model

import ps.crossworking.dto.CategoryApiOutput
import java.util.*

/**
 * Internal representation for Category output.
 */
data class CategoryOutput(
    val categoryId: UUID,
    val name: String,
) {
    fun toExternal() = CategoryApiOutput(categoryId, name)
}
