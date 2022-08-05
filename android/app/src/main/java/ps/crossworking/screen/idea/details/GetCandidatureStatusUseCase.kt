package ps.crossworking.screen.idea.details

import ps.crossworking.model.Candidature
import ps.crossworking.repository.ICandidatureRepository
import javax.inject.Inject

interface IGetCandidatureStatusUseCase {
    suspend operator fun invoke(userId: String, ideaId: String): Candidature?
}

class GetCandidatureStatusUseCase @Inject constructor(
    val repository: ICandidatureRepository
) : IGetCandidatureStatusUseCase {

    override suspend fun invoke(userId: String, ideaId: String): Candidature? {
        return repository.getCandidatureStatus(userId, ideaId)
    }
}
