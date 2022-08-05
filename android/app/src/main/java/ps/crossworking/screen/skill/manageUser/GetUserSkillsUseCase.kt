package ps.crossworking.screen.skill.manageUser

import ps.crossworking.model.Skill
import ps.crossworking.repository.ISkillRepository
import javax.inject.Inject

interface IGetUserSkillsUseCase {
    suspend operator fun invoke(userId: String): List<Skill>
}

class GetUserSkillsUseCase @Inject constructor(
    val repo: ISkillRepository
) : IGetUserSkillsUseCase {

    override suspend fun invoke(userId: String): List<Skill> {
        return repo.getUserSkills(userId)
    }
}
