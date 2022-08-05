package ps.crossworking.screen.skill.manageUser

import ps.crossworking.repository.ISkillRepository
import javax.inject.Inject

interface IDeleteUserSkillUseCase {
    suspend operator fun invoke(userId: String, skillId: String)
}

class DeleteUserSkillUseCase @Inject constructor(
    val repo: ISkillRepository
) : IDeleteUserSkillUseCase {

    override suspend fun invoke(userId: String, skillId: String) {
        return repo.deleteUserSkill(userId, skillId)
    }
}
