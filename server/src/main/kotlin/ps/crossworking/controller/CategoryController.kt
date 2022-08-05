package ps.crossworking.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ps.crossworking.common.CATEGORIES_PATH
import ps.crossworking.dto.CategoryListApiOutput
import ps.crossworking.service.CategoryService

/**
 * Controller for category routes.
 */
@RestController
@RequestMapping(CATEGORIES_PATH)
class CategoryController(val service: CategoryService) {

    @GetMapping
    fun getCategories() = CategoryListApiOutput(
        service.getCategories().map {
            it.toExternal()
        }
    )
}
