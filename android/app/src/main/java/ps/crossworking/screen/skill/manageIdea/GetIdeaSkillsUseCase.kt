package ps.crossworking.screen.skill.manageIdea

import ps.crossworking.model.Skill
import ps.crossworking.repository.ISkillRepository
import javax.inject.Inject

interface IGetIdeaSkillsUseCase {
    suspend operator fun invoke(ideaId: String): List<Skill>
}

class GetIdeaSkillsUseCase @Inject constructor(
    val repo: ISkillRepository
) : IGetIdeaSkillsUseCase {

    override suspend fun invoke(ideaId: String): List<Skill> {
        return repo.getIdeaSkills(ideaId)
    }
}
