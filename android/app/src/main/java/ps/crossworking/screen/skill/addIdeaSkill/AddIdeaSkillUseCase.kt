package ps.crossworking.screen.skill.addIdeaSkill

import ps.crossworking.repository.ISkillRepository
import javax.inject.Inject

interface IAddIdeaSkillUseCase {
    suspend operator fun invoke(
        ideaId: String,
        skillName: String,
        about: String,
        categoryId: String
    )
}

class AddIdeaSkillUseCase @Inject constructor(
    val repository: ISkillRepository
) : IAddIdeaSkillUseCase {

    override suspend fun invoke(
        ideaId: String,
        skillName: String,
        about: String,
        categoryId: String
    ) {
        repository.addIdeaSkill(ideaId, skillName, about, categoryId)
    }
}
