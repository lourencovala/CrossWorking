package ps.crossworking.screen.idea.details

import ps.crossworking.model.Candidature
import ps.crossworking.repository.ICandidatureRepository
import javax.inject.Inject

interface IApplyToIdeaUseCase {
    suspend operator fun invoke(userId: String, ideaId: String): Candidature
}

class ApplyToIdeaUseCase @Inject constructor(
    val repository: ICandidatureRepository
) : IApplyToIdeaUseCase {
    override suspend fun invoke(userId: String, ideaId: String): Candidature {
        return repository.applyToIdea(userId, ideaId)
    }
}
