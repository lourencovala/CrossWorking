package ps.crossworking.screen.idea.candidatures

import ps.crossworking.model.Candidate
import ps.crossworking.repository.ICandidatureRepository
import javax.inject.Inject

interface IUpdateCandidateUseCase {
    suspend operator fun invoke(ideaId: String, candidateId: String, isAccepted: Boolean): Candidate
}

class UpdateCandidateUseCase @Inject constructor(
    val repository: ICandidatureRepository
) : IUpdateCandidateUseCase {

    override suspend fun invoke(
        ideaId: String,
        candidateId: String,
        isAccepted: Boolean
    ): Candidate {
        return repository.updateCandidate(ideaId, candidateId, isAccepted)
    }
}
