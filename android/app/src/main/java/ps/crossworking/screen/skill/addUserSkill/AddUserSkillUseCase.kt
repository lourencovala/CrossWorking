package ps.crossworking.screen.skill.addUserSkill

import ps.crossworking.repository.ISkillRepository
import javax.inject.Inject

interface IAddUserSkillUseCase {
    suspend operator fun invoke(
        userId: String,
        skillName: String,
        about: String,
        categoryId: String
    )
}

class AddUserSkillUseCase @Inject constructor(
    val repo: ISkillRepository
) : IAddUserSkillUseCase {

    override suspend fun invoke(
        userId: String,
        skillName: String,
        about: String,
        categoryId: String
    ) {
        repo.addUserSkill(userId, skillName, about, categoryId)
    }
}
