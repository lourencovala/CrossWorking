package ps.crossworking.dao

import org.jdbi.v3.sqlobject.statement.SqlQuery
import ps.crossworking.model.CategoryOutput

interface CategoryDao {

    @SqlQuery("select * from category")
    fun getCategories(): List<CategoryOutput>
}
