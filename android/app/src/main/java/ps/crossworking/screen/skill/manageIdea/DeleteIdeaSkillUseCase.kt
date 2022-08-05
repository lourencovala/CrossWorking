package ps.crossworking.screen.skill.manageIdea

import ps.crossworking.repository.ISkillRepository
import javax.inject.Inject

interface IDeleteIdeaSkillUseCase {
    suspend operator fun invoke(ideaId: String, skillId: String)
}

class DeleteIdeaSkillUseCase @Inject constructor(
    val repo: ISkillRepository
) : IDeleteIdeaSkillUseCase {

    override suspend fun invoke(ideaId: String, skillId: String) {
        return repo.deleteIdeaSkill(ideaId, skillId)
    }
}
