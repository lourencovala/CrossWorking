package ps.crossworking.screen.skill

import ps.crossworking.model.Category
import ps.crossworking.repository.ISkillRepository
import javax.inject.Inject

interface IGetCategoryUseCase {
    suspend operator fun invoke(): List<Category>
}


class GetCategoryUseCase @Inject constructor(
    val repo: ISkillRepository
) : IGetCategoryUseCase {

    override suspend fun invoke(): List<Category> {
        return repo.getCategories()
    }
}
