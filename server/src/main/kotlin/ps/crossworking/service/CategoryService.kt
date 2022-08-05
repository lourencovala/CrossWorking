package ps.crossworking.service

import org.springframework.stereotype.Component
import ps.crossworking.common.withExceptionHandler
import ps.crossworking.dao.CategoryDao
import ps.crossworking.model.CategoryOutput

/**
 * Business logic for Categories.
 */
@Component
class CategoryService(val dao: CategoryDao) {

    fun getCategories(): List<CategoryOutput> {
        return withExceptionHandler {
            dao.getCategories()
        }
    }
}
